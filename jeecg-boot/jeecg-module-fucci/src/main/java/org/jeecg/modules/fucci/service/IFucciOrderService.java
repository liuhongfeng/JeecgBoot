package org.jeecg.modules.fucci.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderPayResultVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-27
 * @describe
 */
public interface IFucciOrderService {

    /**
     * 分页查询用户预约信息列表数据
     *
     * @param request  请求信息
     * @param pageNo   页码
     * @param pageSize 每页查询数量
     * @return 用户预约信息列表数据
     */
    IPage<FucciOrderVO> list(HttpServletRequest request, Integer pageNo, Integer pageSize);

    /**
     * 查询预约完成详情信息
     *
     * @param id 预约id
     * @return 预约完成详情信息
     */
    FucciOrderVO details(String id);

    /**
     * 工作人员-查询钓场预约日期列表数据
     *
     * @param request 请求信息
     * @return 钓场预约日期列表数据
     */
    JSONObject staffOrderDateList(HttpServletRequest request);

    /**
     * 微信支付-按商户订单号查询订单接口
     *
     * @param orderId 订单ID
     * @return 订单支付结果
     */
    FucciOrderPayResultVO payTransactions(String orderId);

    /**
     * 微信支付-按商户订单号关闭订单接口（支付超时订单处理）
     *
     * @param orderId 订单ID
     */
    void payClose(String orderId);

    /**
     * 微信支付-支付成功回调通知接口
     *
     * @param request    请求信息
     * @param notifyData 回调通知数据
     * @return 返回信息
     */
    ResponseEntity<String> payNotifySuccess(HttpServletRequest request, String notifyData);

    /**
     * 微信支付-退款结果回调通知接口
     *
     * @param request    请求信息
     * @param notifyData 回调通知数据
     * @return 返回信息
     */
    ResponseEntity<String> payNotifyRefund(HttpServletRequest request, String notifyData);

}
