package org.jeecg.modules.admin.ground.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;

import java.util.List;

/**
 * @Description: 钓场船只
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
public interface IFcFishBoatService extends IService<FcFishBoat> {

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FcFishBoat>
     */
    public List<FcFishBoat> selectByMainId(String mainId);
}
