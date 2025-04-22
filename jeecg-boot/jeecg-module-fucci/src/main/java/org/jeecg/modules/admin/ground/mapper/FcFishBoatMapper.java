package org.jeecg.modules.admin.ground.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;

import java.util.List;

/**
 * @Description: 钓场船只
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
public interface FcFishBoatMapper extends BaseMapper<FcFishBoat> {

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FcFishBoat>
     */
    public List<FcFishBoat> selectByMainId(@Param("mainId") String mainId);
}
