package org.jeecg.modules.admin.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.admin.order.entity.FcFishOrder;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundBoatOrderVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundOrderDateVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundStaffOrderDateVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;
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

    /**
     * 分页查询用户预约信息列表数据
     *
     * @param page   分页
     * @param userId 用户ID
     * @return 用户预约信息列表数据
     */
    IPage<FucciOrderVO> getOrderByUserId(Page<FucciOrderVO> page, String userId);

    /**
     * 查询预约完成详情信息
     *
     * @param id 预约id
     * @return 预约完成详情信息
     */
    FucciOrderVO getOrderById(String id);

    /**
     * 工作人员-查询钓场预约日期列表数据
     *
     * @param groundId 钓场id
     * @return 钓场预约日期列表数据
     */
    List<FucciGroundStaffOrderDateVO> getOrderByGroundId(String groundId);

}
