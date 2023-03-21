package com.peteralbus;

import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.entity.Estimate;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.service.EstimateService;
import com.peteralbus.util.EstimateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class EarthquickApplicationTests
{
    @Autowired
    EstimateService estimateService;
    @Autowired
    EstimateUtil estimateUtil;
    @Autowired
    EarthquakeInfoService earthquakeInfoService;
    @Test
    void contextLoads()
    {
//        long startTime = System.currentTimeMillis();
//        Map<String, Object> mapParameter = new HashMap<String, Object>();
//        mapParameter.put("earthquakeId",16);
//        EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
//        double magnitude = earthquakeInfo.getMagnitude(),
//                highIntensity = earthquakeInfo.getHighIntensity(),
//                longitude = earthquakeInfo.getLongitude(),
//                latitude = earthquakeInfo.getLatitude(),
//                longRadius = earthquakeInfo.getIntensityLineList().get(2).getLongRadius(),
//                shortRadius = earthquakeInfo.getIntensityLineList().get(2).getShortRadius();
//        System.out.println(longRadius);
//        LocalDateTime earthquakeTime = earthquakeInfo.getEarthquakeTime();
//        //将角度转换为弧度。
//        double radians = Math.toRadians(latitude);
//        double minLongitude = longitude-shortRadius/(111-Math.cos(radians)),
//                maxLongitude = longitude+shortRadius/(111-Math.cos(radians)),
//                minLatitude = latitude-longRadius/111,
//                maxLatitude = latitude+longRadius/111;
//        int population=(int)estimateService.getPopulation(minLongitude,maxLongitude,minLatitude,maxLatitude);
//        double death=estimateUtil.deathPredict(earthquakeInfo.getEarthquakeId(),population,magnitude,highIntensity,earthquakeTime,longitude,latitude);
//        long endTime = System.currentTimeMillis();
//        long usedTime = (endTime-startTime)/1000;
//        System.out.println(death + " " + usedTime);
//        getPopulation(100.7342,25.5736);
//        getPopulation(99.9586,25.6753);
//        getPopulation(100.1926,25.9121);
//        getPopulation(100.1885,25.9252);
//        getPopulation(99.7768,25.3342);
//        getPopulation(99.9586,25.6752);
//        getPopulation(100.3105,25.6775);
//        getPopulation(100.1209,25.7921);
//        getPopulation(100.4937,25.3366);
    }

    void getPopulation(double longitude,double latitude)
    {
        double minLongitude = longitude-0.1,
                maxLongitude = longitude+0.1,
                minLatitude = latitude-0.1,
                maxLatitude = latitude+0.1;
        System.out.println(estimateService.getPopulation(minLongitude, maxLongitude, minLatitude, maxLatitude));
    }
}
