package com.peteralbus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peteralbus.entity.EarthquakeInfo;
import com.peteralbus.entity.IntensityLine;
import com.peteralbus.mapper.EarthquakeInfoMapper;
import com.peteralbus.mapper.IntensityLineMapper;
import com.peteralbus.service.EarthquakeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * The type Earthquake info service.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
@Service
public class EarthquakeInfoServiceImpl implements EarthquakeInfoService
{
    /**
     * The Earthquake info mapper.
     */
    EarthquakeInfoMapper earthquakeInfoMapper;
    /**
     * The Intensity line mapper.
     */
    IntensityLineMapper intensityLineMapper;

    /**
     * Sets earthquake info mapper.
     *
     * @param earthquakeInfoMapper the earthquake info mapper
     */
    @Autowired
    public void setEarthquakeInfoMapper(EarthquakeInfoMapper earthquakeInfoMapper)
    {
        this.earthquakeInfoMapper = earthquakeInfoMapper;
    }

    /**
     * Sets intensity line mapper.
     *
     * @param intensityLineMapper the intensity line mapper
     */
    @Autowired
    public void setIntensityLineMapper(IntensityLineMapper intensityLineMapper)
    {
        this.intensityLineMapper = intensityLineMapper;
    }

    @Override
    public List<EarthquakeInfo> queryInfoWithLine(Map<String, Object> parameter)
    {
        return earthquakeInfoMapper.queryInfoWithLine(parameter);
    }

    @Override
    public int insertEarthquakeInfo(EarthquakeInfo earthquakeInfo)
    {
        int result = 0;
        result = earthquakeInfoMapper.insert(earthquakeInfo);
        if (result == 0)
        {
            return result;
        }
        for (IntensityLine inensityLine : earthquakeInfo.getIntensityLineList())
        {
            inensityLine.setEarthquakeId(earthquakeInfo.getEarthquakeId());
            result = intensityLineMapper.insert(inensityLine);
        }
        return result;
    }

    @Override
    public int deleteEarthquakeInfo(EarthquakeInfo earthquakeInfo)
    {
        int result = 0;
        QueryWrapper<IntensityLine> intensityLineQueryWrapper = new QueryWrapper<>();
        intensityLineQueryWrapper.eq("earthquake_id", earthquakeInfo.getEarthquakeId());
        result = intensityLineMapper.delete(intensityLineQueryWrapper);
        if (result > 0)
        {
            result = earthquakeInfoMapper.deleteById(earthquakeInfo);
        }
        return result;
    }
}
