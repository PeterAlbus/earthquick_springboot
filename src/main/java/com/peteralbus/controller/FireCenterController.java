package com.peteralbus.controller;

import com.peteralbus.entity.*;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.service.EstimateService;
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
    @Autowired
    EstimateService estimateService;
    @Autowired
    EarthquakeInfoService earthquakeInfoService;
    @RequestMapping("/getAllFireCenter")
    public List<FireCenter> getAllFireCenter(){
        return fireCenterService.getAllFireCenter();
    }

    //获取一个地震区域的消防站位置
    public List<FireCenter> getOnePlaceAllFireCenter(Long earthquakeId){
        List<FireCenter> fireCenters=getAllFireCenter();
        List<FireCenter> fireCenterList=new ArrayList<>();
        Map<String,Object>mapParameter=new HashMap<>();
        mapParameter.put("earthquakeId",earthquakeId);
        EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        for(FireCenter fireCenter:fireCenters){
            double distanceTwoPlaces=getDistance(fireCenter.getFireLon(),fireCenter.getFireLat(),earthquakeInfo.getLongitude(),earthquakeInfo.getLatitude());
            List<IntensityLine> intensityLineList=earthquakeInfo.getIntensityLineList();
            if(distanceTwoPlaces<intensityLineList.get(intensityLineList.size()-1).getLongRadius())
            {
                fireCenterList.add(fireCenter);
            }
        }
        System.out.println(fireCenterList);
        return fireCenterList;
    }
    @RequestMapping("/getFireCenterResult")
    public List<Double> getFireCenterIntensity(){
        List<Double> FireIntensity=new ArrayList<>();
        List<FireCenter> fireCenters= fireCenterService.getAllFireCenter();//这里需要获取比较新的火点数据
        for(FireCenter fireCenter:fireCenters){
            FireIntensity.add(estimateController.getPointIntensity(16L, fireCenter.getFireLon(), fireCenter.getFireLat()));
        }
        return FireIntensity;
    }
    @RequestMapping("/getOneFireCenter")
    public FireCenter getOneFireCenter(int id){
        return fireCenterService.getAllFireCenter().get(id);
    }
    @RequestMapping("/findFireCenterNearby")
    public List<FireWeight> getFireCenterWeight(Long earthquakeId){
        List<FireCenter> OnePlaceAllFireCenter=getOnePlaceAllFireCenter(earthquakeId);
        List<Double> FireCenterIntensityArr=new ArrayList<>();
        for(FireCenter fireCenter:OnePlaceAllFireCenter){
            FireCenterIntensityArr.add(estimateController.getPointIntensity(16L, fireCenter.getFireLon(), fireCenter.getFireLat()));
        }
        int FireCenterIntensityArSize=FireCenterIntensityArr.size();
        System.out.print("FireCenterIntensityArSize长度为"+FireCenterIntensityArSize);
        if(FireCenterIntensityArSize==0){
            return null;
        }
        Map<Integer,Integer>map = new HashMap<Integer,Integer>();
        for(int i=0;i<FireCenterIntensityArSize;i++){
            map.put(i,OnePlaceAllFireCenter.get(i).getFireId());
        }
        double [][] arr=new double [10][FireCenterIntensityArSize];
        //通过earthquakeId获得想要的地区的估计结果
        Estimate estimate=estimateService.queryAnalyzeById(earthquakeId);
        int temp=0;
        for(double f:FireCenterIntensityArr){
            arr[0][temp]=f;//属性1，对于资助点1、2、3的值,因为基本上属性是固定的，可能资助点的数量会增加,假定属性最大值为10个
            arr[1][temp]=estimate.getPopulation()*f+100*Math.random();
            arr[2][temp]=Math.log(estimate.getPredictDeath()*estimate.getPredictEconomy())+100*Math.random();
            temp++;
        }
//        arr[1]=new double[] {0.847,0.63,0.921};
//        arr[2]=new double[] {524,439,842};
        double [][]arrNew=new double[10][FireCenterIntensityArSize];
        double [][]arrNew1=new double[10][FireCenterIntensityArSize];
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
//            arrayStrict.put(i+1,w[i]);
              arrayStrict.put(i,w[i]);
        }
        List<Map.Entry<Integer,Double>> entrys=new ArrayList<>(arrayStrict.entrySet());
        Collections.sort(entrys, new MyComparator());
        //输出排序后的键值对
        int count=0;
        for(Map.Entry<Integer,Double> entry:entrys){
            topTen.put(entry.getKey(),entry.getValue());
            FireCenter resultFireCenter=getOneFireCenter(map.get(entry.getKey()));
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
    private static final double EARTH_RADIUS = 6378.137;
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s =  s * EARTH_RADIUS;
        return s;
    }
}
