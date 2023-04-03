package com.peteralbus.controller;

import com.peteralbus.entity.*;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.service.EstimateService;
import com.peteralbus.service.FireCenterService;
import com.peteralbus.util.EstimateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * The type Fire center controller.
 */
@CrossOrigin
@RestController
public class FireCenterController {
    private FireCenterService fireCenterService;
    private EstimateController estimateController;
    private EstimateService estimateService;
    private EarthquakeInfoService earthquakeInfoService;
    private EstimateUtil estimateUtil;
    /**
     * The Max fire center count.
     */
    static Integer MAX_FIRE_CENTER_COUNT = 20;

    /**
     * Sets fire center service.
     *
     * @param fireCenterService the fire center service
     */
    @Autowired
    public void setFireCenterService(FireCenterService fireCenterService) {
        this.fireCenterService = fireCenterService;
    }

    /**
     * Sets estimate controller.
     *
     * @param estimateController the estimate controller
     */
    @Autowired
    public void setEstimateController(EstimateController estimateController) {
        this.estimateController = estimateController;
    }

    /**
     * Sets estimate service.
     *
     * @param estimateService the estimate service
     */
    @Autowired
    public void setEstimateService(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    /**
     * Sets earthquake info service.
     *
     * @param earthquakeInfoService the earthquake info service
     */
    @Autowired
    public void setEarthquakeInfoService(EarthquakeInfoService earthquakeInfoService) {
        this.earthquakeInfoService = earthquakeInfoService;
    }

    /**
     * Sets estimate util.
     *
     * @param estimateUtil the estimate util
     */
    @Autowired
    public void setEstimateUtil(EstimateUtil estimateUtil) {
        this.estimateUtil = estimateUtil;
    }

    /**
     * Gets all fire center.
     *
     * @return the all fire center
     */
    @RequestMapping("/getAllFireCenter")
    public List<FireCenter> getAllFireCenter() {
        return fireCenterService.getAllFireCenter();
    }

    /**
     * 获取一个地震区域的消防站位置
     *
     * @param earthquakeId the earthquake id
     * @return the one place all fire center
     */
    public List<FireCenter> getOnePlaceAllFireCenter(Long earthquakeId) {
        List<FireCenter> fireCenters = fireCenterService.getAllFireCenter();
        List<FireCenter> fireCenterList = new ArrayList<>();
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("earthquakeId", earthquakeId);
        EarthquakeInfo earthquakeInfo = earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        for (FireCenter fireCenter : fireCenters) {
            double distanceTwoPlaces = getDistance(fireCenter.getFireLon(), fireCenter.getFireLat(), earthquakeInfo.getLongitude(), earthquakeInfo.getLatitude());
            List<IntensityLine> intensityLineList = earthquakeInfo.getIntensityLineList();
            if (distanceTwoPlaces < intensityLineList.get(intensityLineList.size() - 1).getLongRadius()) {
                fireCenterList.add(fireCenter);
            }
        }
        return fireCenterList;
    }

    /**
     * Gets one fire center.
     *
     * @param id the id
     * @return the one fire center
     */
    @RequestMapping("/getOneFireCenter")
    public FireCenter getOneFireCenter(int id) {
        return fireCenterService.getAllFireCenter().get(id);
    }

    /**
     * 根据地震ID，寻找附近的消防站，并随机选取fireCenterCount个进行物资分配计算.
     *
     * @param earthquakeId    地震ID
     * @param fireCenterCount 物资分配的救援点数量
     * @return FireWeight，救援点权重，包含救援点信息
     */
    @RequestMapping("/findFireCenterNearby")
    public List<FireWeight> getFireCenterWeight(Long earthquakeId, Integer fireCenterCount) {
        if (fireCenterCount > MAX_FIRE_CENTER_COUNT) {
            // 自行修改封装后的返回值,错误：资助点数量不能过多
            return null;
        }
        List<FireCenter> fireCenterList = getOnePlaceAllFireCenter(earthquakeId);
        List<FireCenter> randomFireCenterList = new ArrayList<>();
        Random random = new Random();
        // 随机选取 fireCenterCount 个消防站
        for (int i = 0; i < fireCenterCount; i++) {
            if(fireCenterList.size()==0){
                break;
            }
            int index = random.nextInt(fireCenterList.size());
            randomFireCenterList.add(fireCenterList.get(index));
            fireCenterList.remove(index);
        }
        int randomFireCenterListSize = randomFireCenterList.size();
        if(randomFireCenterList.size()==0) {
            // 自行修改封装后的返回值,错误：该地区没有找到消防站
            return null;
        }
        double[][] arr = new double[randomFireCenterListSize][10];
        int temp = 0;
        //通过earthquakeId获得想要的地区的估计结果
        Estimate estimate = estimateService.queryAnalyzeById(earthquakeId);
        // 依次设定每个救援点的指标，最多可以设置10个指标
        for (FireCenter fireCenter : randomFireCenterList) {
            Double intensity = estimateController.getPointIntensity(earthquakeId, fireCenter.getFireLon(), fireCenter.getFireLat());
            arr[temp][0] = intensity * 100;
            arr[temp][1] = estimate.getPopulation() * intensity + 100 * Math.random();
            arr[temp][2] = Math.log(estimate.getPredictDeath() * estimate.getPredictEconomy()) + 100 * Math.random();
            temp++;
        }
        Double[] weights = estimateUtil.entropyMethod(arr,randomFireCenterListSize,3,new ArrayList<Integer>(Arrays.asList(0,1,2)));
        List<FireWeight> resultFireWeightLists = new ArrayList<>();
        for (int i=0;i<randomFireCenterListSize;i++) {
            FireWeight fireWeight = new FireWeight();
            fireWeight.setFireId(randomFireCenterList.get(i).getFireId());
            fireWeight.setFireLat(randomFireCenterList.get(i).getFireLat());
            fireWeight.setFireLon(randomFireCenterList.get(i).getFireLon());
            fireWeight.setFireName(randomFireCenterList.get(i).getFireName());
            fireWeight.setFireAddress(randomFireCenterList.get(i).getFireAddress());
            fireWeight.setFireCenterWeight(weights[i]);
            resultFireWeightLists.add(fireWeight);
        }
        // 返回值自行封装
        return resultFireWeightLists;
    }



    private static final double EARTH_RADIUS = 6378.137;

    /**
     * Gets distance.
     *
     * @param longitude1 the longitude 1
     * @param latitude1  the latitude 1
     * @param longitude2 the longitude 2
     * @param latitude2  the latitude 2
     * @return the distance
     */
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
        s = s * EARTH_RADIUS;
        return s;
    }
}
