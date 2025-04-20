package org.jeecg.modules.admin.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.admin.order.entity.FcFishOrder;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundBoatOrderVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundOrderDateVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 钓场船只预约
 * @Author: jeecg-boot
 * @Date: 2025-04-20
 * @Version: V1.0
 */
@Repository
public interface FcFishOrderMapper extends BaseMapper<FcFishOrder> {

    /**
     * 查询钓场未来 30 天钓场船只预约数据
     *
     * @param groundId 钓场id
     * @return 钓场未来 30 天钓场船只预约数据
     */
    List<FucciGroundOrderDateVO> queryGroundOrderDateList(@Param("groundId") String groundId);

    /**
     * 按日期查询钓场的具体船只预约信息
     *
     * @param groundId  钓场id
     * @param orderDate 预约日期
     * @return 钓场的具体船只预约信息
     */
    List<FucciGroundBoatOrderVO> queryGroundBoatOrderListByDate(@Param("groundId") String groundId, @Param("orderDate") String orderDate);

}
