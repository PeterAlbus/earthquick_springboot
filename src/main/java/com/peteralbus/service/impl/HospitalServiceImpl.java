package com.peteralbus.service.impl;

import com.peteralbus.entity.Hospital;
import com.peteralbus.mapper.HospitalMapper;
import com.peteralbus.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalMapper hospitalMapper;
    @Override
    public List<Hospital> findAllHospital() {
        return hospitalMapper.findAllHospital();
    }
}
