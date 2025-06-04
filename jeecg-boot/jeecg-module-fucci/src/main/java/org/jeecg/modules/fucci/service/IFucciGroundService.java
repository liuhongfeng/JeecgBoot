package org.jeecg.modules.fucci.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.fucci.pojo.dto.FucciGroundBoatOrderDTO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundDetailsVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundOrderVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderPayVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-15
 * @describe
 */
public interface IFucciGroundService {

    /**
     * 查询钓场列表信息
     *
     * @return 钓场列表信息
     */
    JSONObject list();

    /**
     * 查询钓场详情信息
     *
     * @param request 请求信息
     * @param id      钓场id
     * @return 钓场详情信息
     */
    FucciGroundDetailsVO details(HttpServletRequest request, String id);

    /**
     * 查询钓场船只预约信息
     *
     * @param request 请求信息
     * @param id      钓场id
     * @param date    预约日期
     * @return 钓场船只预约信息
     */
    FucciGroundOrderVO order(HttpServletRequest request, String id, String date);

    /**
     * 钓场船只确认预约处理
     *
     * @param request            请求信息
     * @param groundBoatOrderDTO 确认预约 DTO
     * @return 钓场船只预约下单返参数据
     */
    FucciOrderPayVO confirmOrder(HttpServletRequest request, FucciGroundBoatOrderDTO groundBoatOrderDTO);

}
