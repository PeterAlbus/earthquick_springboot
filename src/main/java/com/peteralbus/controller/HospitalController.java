package com.peteralbus.controller;

import com.peteralbus.entity.Distance;
import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.entity.Hospital;
import com.peteralbus.entity.IntensityLine;
import com.peteralbus.service.EarthquakeInfoService;
import com.peteralbus.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Hospital controller.
 *
 * @author PeterAlbus
 */
@CrossOrigin
@RestController
public class HospitalController {

    /**
     * The Hospital service.
     */
    HospitalService hospitalService;
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
     * Sets hospital service.
     *
     * @param hospitalService the hospital service
     */
    @Autowired
    public void setHospitalService(HospitalService hospitalService)
    {
        this.hospitalService = hospitalService;
    }

    /**
     * Find all hospital list.
     *
     * @return the list
     */
    @RequestMapping("/findAllHospital")
    public List<Hospital> findAllHospital(){
    return hospitalService.findAllHospital();
    }


    /**
     * Find hospital nearby list.
     *
     * @param earthquakeId the earthquake id
     * @return the list
     */
    @RequestMapping("/findHospitalNearby")
    public List<Hospital> findHospitalNearby(Long earthquakeId)
    {
        List<Hospital> hospitals=hospitalService.findAllHospital();
        List<Hospital> hospitalList=new ArrayList<>();
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("earthquakeId",earthquakeId);
        EarthquakeInfo earthquakeInfo=earthquakeInfoService.queryInfoWithLine(mapParameter).get(0);
        for (Hospital hospital : hospitals)
        {
            double distanceTwoPlaces = getDistance(hospital.getLon(), hospital.getLat(), earthquakeInfo.getLongitude(), earthquakeInfo.getLatitude());
            List<IntensityLine> intensityLineList=earthquakeInfo.getIntensityLineList();
            if(distanceTwoPlaces<intensityLineList.get(intensityLineList.size()-1).getLongRadius())
            {
                hospitalList.add(hospital);
            }
        }
        return hospitalList;
    }

    /**
     * Calculate distance distance.
     *
     * @param lng the lng
     * @param lat the lat
     * @return the distance
     */
    @RequestMapping("/calculateDistance")
    public Distance calculateDistance(double lng, double lat)
    {
        //????????????????????????????????????Hospital??????????????????????????????
        //return getDistance(121.446014,31.215937,121.446028464238,31.2158502442799 );
        List<Hospital> hospitals=hospitalService.findAllHospital();
        Double minDistance=Double.MAX_VALUE;
        Distance distance=new Distance();
        for (Hospital hospital : hospitals)
        {
            Double distanceTwoPlaces = getDistance(hospital.getLon(), hospital.getLat(), lng, lat);
            minDistance = Math.min(minDistance, distanceTwoPlaces);
            if (minDistance.equals(distanceTwoPlaces))
            {
                distance.setDistance(minDistance);
                distance.setEndLon(hospital.getLon());
                distance.setEndLat(hospital.getLat());
                distance.setEndName(hospital.getName());
                distance.setEndAddress(hospital.getAddress());
            }
        }
        distance.setStartLon(lng);
        distance.setStartLat(lat);
        return distance;
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
        // ??????
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // ??????
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // ????????????
        double a = lat1 - lat2;
        // ????????????
        double b = lng1 - lng2;
        // ???????????????????????????
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // ?????????????????????, ????????????: ??????
        s =  s * EARTH_RADIUS;
        return s;
    }

    /**
     * Gets distance 2.
     *
     * @param longitude1 the longitude 1
     * @param latitude1  the latitude 1
     * @param longitude2 the longitude 2
     * @param latitude2  the latitude 2
     * @return the distance 2
     */
    public static double getDistance2(double longitude1, double latitude1, double longitude2, double latitude2)
    {
        //Haversine???????????????????????????????????????????????????????????????????????????atan2???
        double latDistance = Math.toRadians(longitude1 - longitude2);
        double lngDistance = Math.toRadians(latitude1 - latitude2);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(longitude1)) * Math.cos(Math.toRadians(longitude2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * EARTH_RADIUS;
    }
}
