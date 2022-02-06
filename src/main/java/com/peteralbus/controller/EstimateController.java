package com.peteralbus.controller;

import com.peteralbus.entity.Estimate;
import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.service.EstimateService;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.util.EstimateUtil;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Estimate controller.
 *
 * @author PeterAlbus
 */
@CrossOrigin
@RestController
@RequestMapping("/estimate")
public class EstimateController {
    /**
     * The Estimate util.
     */
    EstimateUtil estimateUtil;

    /**
     * The Estimate service.
     */
    EstimateService estimateService;
    /**
     * The Earthquake info service.
     */
    EarthquakeInfoService earthquakeInfoService;

    /**
     * Sets earthquake info service.
     *
     * @param earthquakeInfoService the earthquake info service
     */
    @Autowired
    public void setEarthquakeInfoService(EarthquakeInfoService earthquakeInfoService)
    {
        this.earthquakeInfoService = earthquakeInfoService;
    }

    /**
     * Sets estimate util.
     *
     * @param estimateUtil the estimate util
     */
    @Autowired
    public void setEstimateUtil(EstimateUtil estimateUtil)
    {
        this.estimateUtil = estimateUtil;
    }

    /**
     * Sets estimate service.
     *
     * @param estimateService the service of estimate
     */
    @Autowired
    public void setAnalyzeService(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    /**
     * Gets estimate result.
     *
     * @param earthquakeId the earthquake info id
     * @return the estimate result
     */
    @GetMapping("/getAnalyzeResult")
    public Estimate getPredictResult(Long earthquakeId){
        System.out.println(earthquakeId);
        int count = estimateService.queryAnalyze(earthquakeId);
        Estimate estimate = new Estimate();
        if(count==0)
        {
            Map<String, Object> mapParameter = new HashMap<String, Object>();
            mapParameter.put("earthquakeId",earthquakeId);
            EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
            double magnitude = earthquakeInfo.getMagnitude(),
                    highIntensity = earthquakeInfo.getHighIntensity(),
                    longitude = earthquakeInfo.getLongitude(),
                    latitude = earthquakeInfo.getLatitude(),
                    longRadius = earthquakeInfo.getIntensityLineList().get(2).getLongRadius(),
                    shortRadius = earthquakeInfo.getIntensityLineList().get(2).getShortRadius();
            System.out.println(longRadius);
            LocalDateTime earthquakeTime = earthquakeInfo.getEarthquakeTime();
            //将角度转换为弧度。
            double radians = Math.toRadians(latitude);
            double minLongitude = longitude-shortRadius/(111-Math.cos(radians)),
                    maxLongitude = longitude+shortRadius/(111-Math.cos(radians)),
                    minLatitude = latitude-longRadius/111,
                    maxLatitude = latitude+longRadius/111;
            int population=(int)estimateService.getPopulation(minLongitude,maxLongitude,minLatitude,maxLatitude);
            estimate.setPredictDeath(estimateUtil.deathPredict(earthquakeInfo.getEarthquakeId(),population,magnitude,highIntensity,earthquakeTime,longitude,latitude));
            estimate.setPredictEconomy(estimateUtil.economyPredict(earthquakeInfo.getHighIntensity()));
            estimate.setEarthquakeId(earthquakeId);
            estimate.setPopulation(population);
            LocalDateTime date = LocalDateTime.now();
            estimate.setGmtCreate(date);
            int result=estimateService.insertAnalyze(estimate);
        }
        else
        {
            estimate = estimateService.queryAnalyzeById(earthquakeId);
        }
        return estimate;
    }

    /**
     * Gets Point intensity.
     *
     * @param earthquakeId the earthquake id
     * @param longitude    the longitude
     * @param latitude     the latitude
     * @return the intensity
     */
    @GetMapping("/getPointIntensity")
    public Double getPointIntensity(Long earthquakeId,Double longitude,Double latitude)
    {
        Double intensity= (double) 0;
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        EarthquakeInfo earthquakeInfo = earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        GlobalCoordinates source = new GlobalCoordinates(latitude, longitude);
        GlobalCoordinates target = new GlobalCoordinates(earthquakeInfo.getLatitude(), earthquakeInfo.getLongitude());
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, source, target);
        Double meter = geoCurve.getEllipsoidalDistance();
        Double dividingLine=105.1;
        if(earthquakeInfo.getLongitude()<dividingLine)
        {
            intensity=earthquakeInfo.getIntensityByDistance(5.253,1.398,4.164,26,0,2.019,1.398,2.943,8,0,meter);
        }
        else
        {
            intensity=earthquakeInfo.getIntensityByDistance(5.019,1.446,4.136,24,0,2.240,1.446,3.070,9,0,meter);
        }
        return intensity;
    }
}
