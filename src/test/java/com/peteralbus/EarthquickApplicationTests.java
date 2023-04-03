package com.peteralbus;

import com.peteralbus.controller.FireCenterController;
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
import java.util.*;

@SpringBootTest
class EarthquickApplicationTests
{
    @Autowired
    EstimateService estimateService;
    @Autowired
    EstimateUtil estimateUtil;
    @Autowired
    EarthquakeInfoService earthquakeInfoService;

    @Autowired
    FireCenterController fireCenterController;
    @Test
    void contextLoads()
    {
        System.out.println(fireCenterController.getFireCenterWeight(16L, 12));
//        double[][] data =  {{6.962645,67101.0,730.3979,1.3},
//                {5.643831,50732.0,170.8517,1.7},
//                {7.775079,39755.0,89.2054,1.0},
//                {6.568763,67101.0,294.3379,1.0},
//                {6.552100,67101.0,281.1741,1.9},
//                {6.669364,43274.0,41.5144,1.2},
//                {7.774892,39755.0,89.2054,1.4},
//                {6.511145,67101.0,518.4596,1.4},
//                {6.966898,67101.0,382.3772,1.7},
//                {5.894564,35641.0,374.3219,1.2}};
//        List<Integer> list = new ArrayList<>();
//        list.add(0);
//        list.add(2);
//        list.add(3);
//        System.out.println(Arrays.toString(estimateUtil.entropyMethod(data, 10, 4, list)));
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
