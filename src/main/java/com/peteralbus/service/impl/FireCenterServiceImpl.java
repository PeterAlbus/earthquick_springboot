package com.peteralbus.service.impl;

import com.peteralbus.entity.FireCenter;
import com.peteralbus.mapper.FireCenterMapper;
import com.peteralbus.service.FireCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireCenterServiceImpl implements FireCenterService {

    FireCenterMapper fireCenterMapper;

    @Autowired
    public void setFireCenterMapper(FireCenterMapper fireCenterMapper) {
        this.fireCenterMapper = fireCenterMapper;
    }

    @Override
    public List<FireCenter> getAllFireCenter() {
        return fireCenterMapper.selectList(null);
    }
}
