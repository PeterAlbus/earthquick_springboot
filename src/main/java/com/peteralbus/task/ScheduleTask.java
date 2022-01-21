package com.peteralbus.task;


import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.service.EarthquakeInfoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Schedule Task.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
@Component
public class ScheduleTask
{
    EarthquakeInfoService earthquakeInfoService;

    @Autowired
    public void setEarthquakeInfoService(EarthquakeInfoService earthquakeInfoService)
    {
        this.earthquakeInfoService = earthquakeInfoService;
    }

    @Scheduled(cron = "0 55 23 * * ?")
    public void getNewEarthquake()
    {
        String url = "http://www.ceic.ac.cn/speedsearch";
        try
        {
            Document doc = Jsoup.connect(url).get();
            Element element = doc.select("table").first();
            Elements els = element.select("tr");
            int skip=1;
            for (Element el : els)
            {
                if(skip==1)
                {
                    skip=0;
                    continue;
                }
                Elements ele = el.select("td");
                Double magnitude=Double.parseDouble(ele.get(0).text());
                Double longitude=Double.parseDouble(ele.get(3).text());
                Double latitude=Double.parseDouble(ele.get(2).text());
                Double depth=Double.parseDouble(ele.get(4).text());
                String earthquakeName=ele.get(5).text();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime time=LocalDateTime.parse(ele.get(1).text(),dtf);
                EarthquakeInfo earthquakeInfo=new EarthquakeInfo();
                earthquakeInfo.setEarthquakeName(earthquakeName);
                earthquakeInfo.setLatitude(latitude);
                earthquakeInfo.setLongitude(longitude);
                earthquakeInfo.setLatitude(latitude);
                earthquakeInfo.setMagnitude(magnitude);
                earthquakeInfo.setEarthquakeTime(time);
                earthquakeInfo.setDepth(depth);
                Double dividingLine=105.1;
                if(earthquakeInfo.getLongitude()<dividingLine)
                {
                    earthquakeInfo.getIntensity(5.253,1.398,4.164,26,0,2.019,1.398,2.943,8,0);
                }
                else
                {
                    earthquakeInfo.getIntensity(5.019,1.446,4.136,24,0,2.240,1.446,3.070,9,0);
                }
                if(earthquakeInfo.getMagnitude()>4.0)
                {
                    earthquakeInfoService.insertEarthquakeInfo(earthquakeInfo);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
