package com.peteralbus.controller;

import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.util.EstimateUtil;
import com.peteralbus.util.GeoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Earthquake info controller.
 *
 * @author PeterAlbus
 */
@Api(value = "api of earthquake info")
@CrossOrigin
@RestController
@RequestMapping("/earthquakeInfo")
public class EarthquakeInfoController
{
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
     * Gets all earthquake.
     *
     * @return the all earthquake
     */
    @ApiOperation(value = "get all earthquake")
    @GetMapping("/getAllEarthquake")
    List<EarthquakeInfo> getAllEarthquake()
    {
        return earthquakeInfoService.queryInfoWithLine(null);
    }

    /**
     * Gets earthquake by id.
     *
     * @param earthquakeId the earthquake info id
     * @return the earthquake by id
     */
    @GetMapping("/getEarthquakeById")
    EarthquakeInfo getEarthquakeById(Long earthquakeId)
    {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        return earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
    }

    /**
     * Gets earthquake by condition.
     *
     * @param area   the area
     * @param start the start time(string)
     * @param end   the end time(string)
     * @return the earthquake by condition
     */
    @GetMapping("/getEarthquakeByCondition")
    List<EarthquakeInfo> getEarthquakeByCondition(String area, String start, String end)
    {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime=LocalDateTime.parse(start,df);
        LocalDateTime endTime=LocalDateTime.parse(end,df);
        List<EarthquakeInfo> result=new ArrayList<>();
        List<EarthquakeInfo> earthquakeInfoList=earthquakeInfoService.queryInfoWithLine(null);
        for(EarthquakeInfo earthquakeInfo:earthquakeInfoList)
        {
            if(area.equals(GeoUtil.getCountry(earthquakeInfo.getLongitude(), earthquakeInfo.getLatitude()))||"any".equals(area))
            {
                if(startTime.isBefore(earthquakeInfo.getEarthquakeTime())&&endTime.isAfter(earthquakeInfo.getEarthquakeTime()))
                {
                    result.add(earthquakeInfo);
                }
            }
        }
        return result;
    }

    /**
     * Add earthquake string.
     *
     * @param earthquakeInfo the earthquake info
     * @return the string
     */
    @RequestMapping("/addEarthquake")
    public String addEarthquake(EarthquakeInfo earthquakeInfo)
    {
        Double dividingLine=105.0;
        if(earthquakeInfo.getLongitude()<dividingLine)
        {
            /*西部地区*/
            earthquakeInfo.getIntensity(5.253,1.398,4.164,26,0,2.019,1.398,2.943,8,0);
        }
        else
        {
            /*东部地区*/
            earthquakeInfo.getIntensity(5.019,1.446,4.136,24,0,2.240,1.446,3.070,9,0);
        }
        int result=earthquakeInfoService.insertEarthquakeInfo(earthquakeInfo);
        if(result==0)
        {
            return "fail";
        }
        else
        {
            return "success";
        }
    }

    /**
     * Delete earthquake string.
     *
     * @param earthquakeId the earthquake id
     * @return the string
     */
    @RequestMapping("/deleteById")
    public String deleteEarthquake(Long earthquakeId)
    {
        EarthquakeInfo earthquakeInfo=this.getEarthquakeById(earthquakeId);
        int result=earthquakeInfoService.deleteEarthquakeInfo(earthquakeInfo);
        if(result>0)
        {
            return "success";
        }
        else
        {
            return "fail";
        }
    }
}
