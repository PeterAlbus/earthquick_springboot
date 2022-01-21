package com.peteralbus.service.impl;


import com.peteralbus.service.IntensityLineService;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Intensity line service.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
public class IntensityLineServiceImpl implements IntensityLineService
{
    @Override
    public List<Double[]> getMultiLineStringWktToJson(String wkt)
    {
        String toTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = toTailWkt.split("\\(", 2);
        String[] strList = strHead[1].split("\\),\\(");
        List<Double[]> list = new ArrayList<Double[]>();
        for (String value : strList)
        {
            String item = value.trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");
            for (String s : items)
            {
                String jItem = s.trim();
                String[] jItems = jItem.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};
                list.add(listResult);
            }
        }
        return list;
    }
}
