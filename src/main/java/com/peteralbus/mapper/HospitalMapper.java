package com.peteralbus.mapper;

import com.peteralbus.entity.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HospitalMapper {

    public List<Hospital> findAllHospital();

}
