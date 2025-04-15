package org.jeecg.modules.admin.ground.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecg.modules.admin.ground.entity.FcFishGround;
import org.jeecg.modules.admin.ground.mapper.FcFishBoatMapper;
import org.jeecg.modules.admin.ground.mapper.FcFishGroundMapper;
import org.jeecg.modules.admin.ground.service.IFcFishGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 钓场信息
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
@Service
public class FcFishGroundServiceImpl extends ServiceImpl<FcFishGroundMapper, FcFishGround> implements IFcFishGroundService {

    @Autowired
    private FcFishGroundMapper fcFishGroundMapper;
    @Autowired
    private FcFishBoatMapper fcFishBoatMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(FcFishGround fcFishGround, List<FcFishBoat> fcFishBoatList) {
        fcFishGroundMapper.insert(fcFishGround);
        if (fcFishBoatList != null && fcFishBoatList.size() > 0) {
            for (FcFishBoat entity : fcFishBoatList) {
                // 外键设置
                entity.setGroundId(fcFishGround.getId());
                fcFishBoatMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(FcFishGround fcFishGround, List<FcFishBoat> fcFishBoatList) {
        fcFishGroundMapper.updateById(fcFishGround);

        // 1.先删除子表数据
        fcFishBoatMapper.deleteByMainId(fcFishGround.getId());

        // 2.子表数据重新插入
        if (fcFishBoatList != null && fcFishBoatList.size() > 0) {
            for (FcFishBoat entity : fcFishBoatList) {
                // 外键设置
                entity.setGroundId(fcFishGround.getId());
                fcFishBoatMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        fcFishBoatMapper.deleteByMainId(id);
        fcFishGroundMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            fcFishBoatMapper.deleteByMainId(id.toString());
            fcFishGroundMapper.deleteById(id);
        }
    }

}
