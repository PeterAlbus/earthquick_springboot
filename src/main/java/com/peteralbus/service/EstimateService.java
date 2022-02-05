package com.peteralbus.service;

import com.peteralbus.entity.Estimate;
import org.apache.ibatis.annotations.Param;

/**
 * The interface Estimate service.
 */
public interface EstimateService {
    /**
     * Get
     *
     * @param minLongitude the minimum Longitude
     * @param maxLongitude the maximum Longitude
     * @param minLatitude  the minimum Latitude
     * @param maxLatitude  the maximum Latitude
     * @return the double
     */
    double getPopulation(@Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude, @Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude);

    /**
     * Insert analyze int.
     *
     * @param estimate the estimate
     * @return the int
     */
    int insertAnalyze(Estimate estimate);

    /**
     * Query analyze by id estimate.
     *
     * @param earthquakeId the earthquake id
     * @return the estimate
     */
    Estimate queryAnalyzeById(long earthquakeId);

    /**
     * Query analyze int.
     *
     * @param earthquakeId the earthquake id
     * @return the int
     */
    int queryAnalyze(@Param("earthquakeId") long earthquakeId);
}
