package com.peteralbus.service;

import com.peteralbus.entity.Estimate;
import org.apache.ibatis.annotations.Param;

public interface EstimateService {
    /**
     * Get
     *
     * @param minLongitude the minimum Longitude
     * @param maxLongitude the maximum Longitude
     * @param minLatitude the minimum Latitude
     * @param maxLatitude the maximum Latitude
     * @return the double
     */
    double getPopulation(@Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude, @Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude);
    int insertAnalyze(Estimate estimate);
    Estimate queryAnalyzeById(long earthquakeId);
    int queryAnalyze(@Param("earthquakeId") long earthquakeId);
}
