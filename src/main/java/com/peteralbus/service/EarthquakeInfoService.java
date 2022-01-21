package com.peteralbus.service;

import com.peteralbus.entity.EarthquakeInfo;

import java.util.List;
import java.util.Map;


/**
 * The interface Earthquake info service.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
public interface EarthquakeInfoService
{
    /**
     * Query info with line list.
     *
     * @param parameter the parameter
     * @return the list
     */
    List<EarthquakeInfo> queryInfoWithLine(Map<String, Object> parameter);

    /**
     * Insert earthquake info int.
     *
     * @param earthquakeInfo the earthquake info
     * @return the int
     */
    int insertEarthquakeInfo(EarthquakeInfo earthquakeInfo);

    /**
     * Delete earthquake info int.
     *
     * @param earthquakeInfo the earthquake info
     * @return the int
     */
    int deleteEarthquakeInfo(EarthquakeInfo earthquakeInfo);
}
