package org.jeecg.modules.admin.ground.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecg.modules.admin.ground.entity.FcFishGround;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 钓场信息
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
public interface IFcFishGroundService extends IService<FcFishGround> {

    /**
     * 添加一对多
     *
     * @param fcFishGround
     * @param fcFishBoatList
     */
    public void saveMain(FcFishGround fcFishGround, List<FcFishBoat> fcFishBoatList);

    /**
     * 修改一对多
     *
     * @param fcFishGround
     * @param fcFishBoatList
     */
    public void updateMain(FcFishGround fcFishGround, List<FcFishBoat> fcFishBoatList);

    /**
     * 删除一对多
     *
     * @param id
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     *
     * @param idList
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

}
