package com.peteralbus.service.impl;

import com.peteralbus.entity.FireCenter;
import com.peteralbus.mapper.FireCenterMapper;
import com.peteralbus.service.FireCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Fire center service.
 * @author wuhon
 */
@Service
public class FireCenterServiceImpl implements FireCenterService {

    /**
     * The Fire center mapper.
     */
    FireCenterMapper fireCenterMapper;

    /**
     * Sets fire center mapper.
     *
     * @param fireCenterMapper the fire center mapper
     */
    @Autowired
    public void setFireCenterMapper(FireCenterMapper fireCenterMapper) {
        this.fireCenterMapper = fireCenterMapper;
    }

    @Override
    public List<FireCenter> getAllFireCenter() {
        return fireCenterMapper.selectList(null);
    }
}
