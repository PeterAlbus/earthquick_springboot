package com.peteralbus.service.impl;

import com.peteralbus.entity.Estimate;
import com.peteralbus.mapper.EstimateMapper;
import com.peteralbus.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstimateServiceImpl implements EstimateService {

    EstimateMapper estimateMapper;
    @Autowired
    public void setAnalyzeMapper(EstimateMapper estimateMapper){
        this.estimateMapper = estimateMapper;
    }
    @Override
    public double getPopulation(Double minLongitude, Double maxLongitude, Double minLatitude, Double maxLatitude) {
        return estimateMapper.getPopulation(minLongitude,maxLongitude,minLatitude,maxLatitude);
    }
    @Override
    public int insertAnalyze(Estimate analyze){
        int result = 0;
        result = estimateMapper.insert(analyze);
        if (result == 0)
        {
            return result;
        }
        return result;
    }

    @Override
    public Estimate queryAnalyzeById(long earthquakeId) {
        return estimateMapper.queryAnalyzeById(earthquakeId);
    }

    @Override
    public int queryAnalyze(long earthquakeId) {
        return estimateMapper.queryAnalyze(earthquakeId);
    }
}
