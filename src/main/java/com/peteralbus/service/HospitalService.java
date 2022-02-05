package com.peteralbus.service;

import com.peteralbus.entity.Hospital;

import java.util.List;

/**
 * The interface Hospital service.
 * @author PeterAlbus
 */
public interface HospitalService {
    /**
     * Find all hospital list.
     *
     * @return the list
     */
    public List<Hospital> findAllHospital();
}
