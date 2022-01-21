package com.peteralbus.controller;

import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.util.EstimateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * The Estimate util.
     */
    EstimateUtil estimateUtil;
    /**
     * The Earthquake info service.
     */
    EarthquakeInfoService earthquakeInfoService;

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
    EarthquakeInfo getEarthquakeById(int earthquakeId)
    {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        return earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
    }

    /**
     * Gets death predict.
     *
     * @param earthquakeId the earthquake info id
     * @return the death predict
     */
    @GetMapping("/getDeathPredict")
    public double getDeathPredict(int earthquakeId)
    {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        int population=3133;
        return estimateUtil.deathPredict(population,earthquakeInfo.getMagnitude(),earthquakeInfo.getHighIntensity());
    }

    /**
     * Gets economy predict.
     *
     * @param earthquakeId the earthquake info id
     * @return the economy predict
     */
    @GetMapping("/getEconomyPredict")
    public double getEconomyPredict(int earthquakeId)
    {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        return estimateUtil.economyPredict(earthquakeInfo.getHighIntensity());
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
    public String deleteEarthquake(int earthquakeId)
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

    @RequestMapping("/233")
    public String test()
    {
        return "233";
    }
}
