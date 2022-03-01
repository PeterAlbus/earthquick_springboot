package com.peteralbus.controller;

import com.peteralbus.entity.FireCenter;
import com.peteralbus.entity.FireWeight;
import com.peteralbus.service.FireCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin
@RestController
public class FireCenterController {
    @Autowired
    FireCenterService fireCenterService;
    @Autowired
    EstimateController estimateController;
    @RequestMapping("/getAllFireCenter")
    public List<FireCenter> getAllFireCenter(){
        return fireCenterService.getAllFireCenter();
    }
    @RequestMapping("/getFireCenterResult")
    public List<Double> getFireCenterIntensity(){
        List<Double> FireIntensity=new ArrayList<>();
        List<FireCenter> fireCenters= fireCenterService.getAllFireCenter();
        for(FireCenter fireCenter:fireCenters){
            FireIntensity.add(estimateController.getPointIntensity(16L, fireCenter.getFireLon(), fireCenter.getFireLat()));
        }
        return FireIntensity;
    }
    @RequestMapping("/getOneFireCenter")
    public FireCenter getOneFireCenter(int id){
        return fireCenterService.getAllFireCenter().get(id);
    }
    @RequestMapping("/calculateWeight")
    public List<FireWeight> getFireCenterWeight(){
        double [][] arr=new double [10][988];
        List<Double> FireCenterIntensityArr=getFireCenterIntensity();
//        System.out.println(FireCenterIntensityArr);
        int temp=0;
        for(double f:FireCenterIntensityArr){
            arr[0][temp]=f;//属性1，对于资助点1、2、3的值,因为基本上属性是固定的，可能资助点的数量会增加,假定属性最大值为10个
            arr[1][temp]=101*f+100*Math.random();
            arr[2][temp]=Math.log(48.87775814060409*15.304510961044489)+100*Math.random();
            temp++;
        }
//        arr[1]=new double[] {0.847,0.63,0.921};
//        arr[2]=new double[] {524,439,842};
        double [][]arrNew=new double[10][988];
        double [][]arrNew1=new double[10][988];
        double sumProvide=0;
        double max;
        double min;
        int strictNum=arr[0].length;//代表有多少个救助点
        double k=1/(Math.log((double)strictNum));
        double[] e=new double[10];//第 j 项指标的熵值 ej
        double[] g=new double[10];//差异性系数  gi越大指标越重要
        for(int j=0;j<3;j++) {
            min=max=arr[j][0];
            sumProvide=0;
            for(int i=0;i<arr[j].length;i++) {
                if(arr[j][i]<min) {
                    min=arr[j][i];
                }
                if(arr[j][i]>max) {
                    max=arr[j][i];
                }
            }
            for(int i=0;i<arr[j].length;i++) {
                arrNew[j][i]=(arr[j][i]-min)/(max-min);//假设都是正向指标，正向指标的值越大，就说明该地方越需要救助
                arrNew1[j][i]=arrNew[j][i];
                sumProvide+=arrNew[j][i];
            }
            for(int i=0;i<arr[j].length;i++) {
                arrNew[j][i]=(arrNew[j][i]/sumProvide)+0.0000001;
            }
            double sumArrNewHang=0.0;
            for(int i=0;i<arr[j].length;i++) {
                //System.out.println("sum"+Math.log(arrNew[j][i]));
                sumArrNewHang+=(arrNew[j][i]*Math.log(arrNew[j][i]));
            }
            e[j]=(-k)*sumArrNewHang;
            g[j]=1-e[j];
        }
        double[] a=new double[10];//每个属性所占的权数
        double SumG=0.0;
        for(int i=0;i<g.length;i++) {
            SumG+=g[i];
        }
        for(int i=0;i<g.length;i++) {
            a[i]=g[i]/SumG;
        }
        double[]w=new double[strictNum];
        for(int j=0;j<strictNum;j++) {
            w[j]=0;
            for(int i=0;i<3;i++) {
                w[j]+=a[i]*arrNew1[i][j];
            }
//            w[j]=1-w[j];
        }
        Map arrayStrict=new HashMap();
        List<Double> arrayW1=new ArrayList<>();
        Map topTen=new LinkedHashMap();
        List <FireWeight> resultFireWeightLists=new ArrayList<>();
        for(int i=0;i<strictNum;i++){
            arrayW1.add(w[i]);
//            System.out.println("第+"+(i+1)+"个地区的权重值为"+w[i]);
            arrayStrict.put(i+1,w[i]);
        }
        List<Map.Entry<Integer,Double>> entrys=new ArrayList<>(arrayStrict.entrySet());
        Collections.sort(entrys, new MyComparator());
        //输出排序后的键值对
        int count=0;
        for(Map.Entry<Integer,Double> entry:entrys){
            topTen.put(entry.getKey(),entry.getValue());
            FireCenter resultFireCenter=getOneFireCenter(entry.getKey());
            FireWeight fireWeight=new FireWeight();
            fireWeight.setFireId(resultFireCenter.getFireId());
            fireWeight.setFireLat(resultFireCenter.getFireLat());
            fireWeight.setFireLon(resultFireCenter.getFireLon());
            fireWeight.setFireName(resultFireCenter.getFireName());
            fireWeight.setFireAddress(resultFireCenter.getFireAddress());
            fireWeight.setFireCenterWeight(entry.getValue());
            resultFireWeightLists.add(fireWeight);
            count++;
            if(count==10){
                break;
            }
        }

//        return arrayW1;
//        return topTen;
        return resultFireWeightLists;
    }
}
