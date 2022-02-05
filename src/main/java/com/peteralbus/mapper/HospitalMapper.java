package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.entity.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Hospital mapper.
 * @author PeterAlbus
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital>
{
}
