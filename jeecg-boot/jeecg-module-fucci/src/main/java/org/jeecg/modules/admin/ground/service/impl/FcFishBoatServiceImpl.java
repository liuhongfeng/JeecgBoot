package org.jeecg.modules.admin.ground.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecg.modules.admin.ground.mapper.FcFishBoatMapper;
import org.jeecg.modules.admin.ground.service.IFcFishBoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 钓场船只
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
@Service
public class FcFishBoatServiceImpl extends ServiceImpl<FcFishBoatMapper, FcFishBoat> implements IFcFishBoatService {

    @Autowired
    private FcFishBoatMapper fcFishBoatMapper;

    @Override
    public List<FcFishBoat> selectByMainId(String mainId) {
        return fcFishBoatMapper.selectByMainId(mainId);
    }
}
