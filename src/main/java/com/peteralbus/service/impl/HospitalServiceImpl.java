package com.peteralbus.service.impl;

import com.peteralbus.entity.Hospital;
import com.peteralbus.mapper.HospitalMapper;
import com.peteralbus.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Hospital service.
 * @author PeterAlbus
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    private HospitalMapper hospitalMapper;

    /**
     * Sets hospital mapper.
     *
     * @param hospitalMapper the hospital mapper
     */
    @Autowired
    public void setHospitalMapper(HospitalMapper hospitalMapper)
    {
        this.hospitalMapper = hospitalMapper;
    }

    @Override
    public List<Hospital> findAllHospital() {
        return hospitalMapper.selectList(null);
    }
}
