package com.peteralbus.service;

import java.util.List;


/**
 * The interface Intensity line service.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
public interface IntensityLineService
{
    /**
     * Gets multi line string wkt to json.
     *
     * @param wkt the wkt
     * @return the multi line string wkt to json
     */
    List<Double[]> getMultiLineStringWktToJson(String wkt);
}
