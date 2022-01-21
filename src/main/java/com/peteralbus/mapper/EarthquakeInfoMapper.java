package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.entity.EarthquakeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * The interface Earthquake info mapper.
 * @author PeterAlbus
 * Created on 2022/1/20.
 */
@Mapper
public interface EarthquakeInfoMapper extends BaseMapper<EarthquakeInfo>
{
    /**
     * Query info with line list.
     *
     * @param parameter the parameter which can contain earthquakeId,startIndex,pageSize
     * @return the list
     */
    List<EarthquakeInfo> queryInfoWithLine(Map<String, Object> parameter);
}
