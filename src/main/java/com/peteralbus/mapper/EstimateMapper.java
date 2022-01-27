package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.entity.Estimate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EstimateMapper extends BaseMapper<Estimate>
{
    Double getPopulation(@Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude, @Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude);
    Estimate queryAnalyzeById(@Param("earthquakeId") long earthquakeId);
    int queryAnalyze(@Param("earthquakeId") long earthquakeId);
}
