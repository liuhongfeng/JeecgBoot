package org.jeecg.modules.fucci.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.admin.order.entity.FcFishOrder;
import org.jeecg.modules.admin.order.mapper.FcFishOrderMapper;
import org.jeecg.modules.admin.staff.entity.FcFishStaff;
import org.jeecg.modules.admin.staff.service.IFcFishStaffService;
import org.jeecg.modules.admin.vip.entity.FcFishVip;
import org.jeecg.modules.admin.vip.service.IFcFishVipService;
import org.jeecg.modules.fucci.common.constant.FucciConstant;
import org.jeecg.modules.fucci.config.FucciProperties;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundStaffOrderDateVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundStaffOrderVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderPayResultVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;
import org.jeecg.modules.fucci.service.IFucciOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhf
 * @date 2025-04-27
 * @describe
 */
@Slf4j
@Service
@AllArgsConstructor
public class FucciOrderServiceImpl implements IFucciOrderService {

    private ISysUserService sysUserService;
    private IFcFishVipService fcFishVipService;
    private IFcFishStaffService fcFishStaffService;
    private FcFishOrderMapper fcFishOrderMapper;
    private FucciProperties fucciProperties;
    private WxPayService wxService;

    @Override
    public IPage<FucciOrderVO> list(HttpServletRequest request, Integer pageNo, Integer pageSize) {
        // 查询用户信息
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 分页查询用户预约信息数据
        Page<FucciOrderVO> page = new Page<>(pageNo, pageSize);
        return fcFishOrderMapper.getOrderByUserId(page, sysUser.getId());
    }

    @Override
    public FucciOrderVO details(String id) {
        return fcFishOrderMapper.getOrderById(id);
    }

    @Override
    public JSONObject staffOrderDateList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        List<FucciGroundStaffOrderVO> groundStaffOrderVOList = new ArrayList<>();
        // 查询用户信息
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 查询用户是否为工作人员（工作人员只能服务一个钓场）
        LambdaQueryWrapper<FcFishStaff> staffQueryWrapper = new LambdaQueryWrapper<>();
        staffQueryWrapper.eq(FcFishStaff::getUserId, sysUser.getId());
        FcFishStaff fcFishStaff = fcFishStaffService.getOne(staffQueryWrapper);
        if (null == fcFishStaff) {
            throw new JeecgBootException("非钓场工作人员，无法查看钓场预约数据");
        }
        // 查询工作人员所在钓场今天之后的所有预约信息
        List<FucciGroundStaffOrderDateVO> groundStaffOrderDateVOS = fcFishOrderMapper.getOrderByGroundId(fcFishStaff.getGroundId());
        if (null != groundStaffOrderDateVOS) {
            // 按预约日期进行分组
            Map<Date, List<FucciGroundStaffOrderDateVO>> groundStaffOrderDateMap = groundStaffOrderDateVOS.stream()
                    .collect(Collectors.groupingBy(FucciGroundStaffOrderDateVO::getDate));
            // 转成 FucciGroundStaffOrderVO 对象数组，并按 orderDate 倒序排序
            groundStaffOrderVOList = groundStaffOrderDateMap.entrySet().stream().map(entry -> {
                FucciGroundStaffOrderVO groundStaffOrderVO = new FucciGroundStaffOrderVO();
                groundStaffOrderVO.setOrderDate(entry.getKey());
                groundStaffOrderVO.setOrderCount(entry.getValue().size());
                groundStaffOrderVO.setOrderDateList(entry.getValue());
                return groundStaffOrderVO;
            }).sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())).collect(Collectors.toList());
        }
        jsonObject.put("records", groundStaffOrderVOList);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FucciOrderPayResultVO payTransactions(String orderId) {
        FucciOrderPayResultVO orderPayResultVO = new FucciOrderPayResultVO();
        try {
            WxPayOrderQueryV3Result wxPayOrderQueryV3Result = wxService.queryOrderV3(null, orderId);
            log.info("微信支付================查询订单结果：{}", JSONObject.toJSONString(wxPayOrderQueryV3Result));
            if (null != wxPayOrderQueryV3Result) {
                BeanUtils.copyProperties(wxPayOrderQueryV3Result, orderPayResultVO);
                // 订单状态为 SUCCESS，表示支付成功
                if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(wxPayOrderQueryV3Result.getTradeState())) {
                    // 查询是否存在「预约状态为 3:已预约（订单待支付）」的预约订单数据
                    LambdaQueryWrapper<FcFishOrder> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    orderLambdaQueryWrapper.eq(FcFishOrder::getId, wxPayOrderQueryV3Result.getOutTradeNo());
                    orderLambdaQueryWrapper.eq(FcFishOrder::getStatus, "3");
                    FcFishOrder dbOrder = fcFishOrderMapper.selectOne(orderLambdaQueryWrapper);
                    if (null != dbOrder) {
                        // 更新预约订单状态为 1:已预约（支付完成）
                        FcFishOrder order = new FcFishOrder();
                        order.setId(wxPayOrderQueryV3Result.getOutTradeNo());
                        order.setStatus("1");
                        fcFishOrderMapper.updateById(order);
                        // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）（VIP 会员用户下单支付成功，会员次数减1）
                        LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
                        vipQueryWrapper.eq(FcFishVip::getUserId, dbOrder.getUserId());
                        FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
                        if (null != fcFishVip && fcFishVip.getCount() > 0) {
                            // 会员次数减1
                            fcFishVip.setCount(fcFishVip.getCount() - 1);
                            fcFishVipService.updateById(fcFishVip);
                        }
                    }
                }
            }
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        }
        return orderPayResultVO;
    }

    @Override
    public void payClose(String orderId) {
        // 查询「预约状态为 3:已预约（订单待支付）」的超时订单数据
        LambdaQueryWrapper<FcFishOrder> fcFishOrderWrapper = new LambdaQueryWrapper<>();
        fcFishOrderWrapper.eq(FcFishOrder::getStatus, "3");
        if (StringUtils.isNotEmpty(orderId)) {
            // 查询指定订单数据
            fcFishOrderWrapper.eq(FcFishOrder::getId, orderId);
        } else {
            // 查询所有超时订单数据
            fcFishOrderWrapper.le(FcFishOrder::getCreateTime, DateUtil.offset(new Date(), DateField.MINUTE, -15));
        }
        List<FcFishOrder> orderList = fcFishOrderMapper.selectList(fcFishOrderWrapper);
        log.info("已预约（订单待支付）数据量================>>：" + orderList.size());
        for (FcFishOrder order : orderList) {
            log.info("超时预约订单================>>：" + order.getId());
            try {
                // 微信支付-关闭订单处理
                wxService.closeOrderV3(order.getId());
            } catch (WxPayException e) {
                throw new RuntimeException(e);
            }
            // 更新预约订单状态为 4:已取消预约（未支付，超时关闭订单）
            FcFishOrder fcFishOrder = new FcFishOrder();
            fcFishOrder.setId(order.getId());
            fcFishOrder.setStatus("4");
            fcFishOrderMapper.updateById(fcFishOrder);
        }
    }

    /**
     * 取消预约
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderId) {
        // 环境访问地址
        String path = fucciProperties.getPath();
        // 查询「预约状态为 3:已预约（订单待支付）」的超时订单数据
        LambdaQueryWrapper<FcFishOrder> fcFishOrderWrapper = new LambdaQueryWrapper<>();
        fcFishOrderWrapper.eq(FcFishOrder::getId, orderId);
        FcFishOrder order = fcFishOrderMapper.selectOne(fcFishOrderWrapper);
        log.info("预约订单================>>：" + order.getId());
        if (!order.getStatus().equals("1") && !order.getStatus().equals("3")) {
            throw new JeecgBootException("当前订单状态，无法取消预约");
        }
        // 更新预约订单状态
        String updateStatus;
        if (order.getStatus().equals("1")) {
            // 2:已取消预约（已退款）
            updateStatus = "2";
        } else {
            // 4:已取消预约（未支付，超时关闭订单）
            updateStatus = "4";
        }
        FcFishOrder fcFishOrder = new FcFishOrder();
        fcFishOrder.setId(order.getId());
        fcFishOrder.setStatus(updateStatus);
        fcFishOrderMapper.updateById(fcFishOrder);
        // 查询用户是否为 VIP 会员用户（会员用户取消预约，会员次数加1）
        LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
        vipQueryWrapper.eq(FcFishVip::getUserId, order.getUserId());
        FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
        if (null != fcFishVip) {
            // 会员次数加1
            fcFishVip.setCount(fcFishVip.getCount() + 1);
            fcFishVipService.updateById(fcFishVip);
        }
        // 如果更新订单状态为 2:已取消预约（已退款），那么需要进行退款处理
        if (updateStatus.equals("2")) {
            log.info("微信支付================取消预约订单，进行退款处理：{}", JSONObject.toJSONString(order));
            WxPayRefundV3Request request = new WxPayRefundV3Request();
            // 商户订单号
            request.setOutTradeNo(order.getId());
            // 商户退款单号 = 商户订单号 + _refund
            request.setOutRefundNo(order.getId() + "_refund");
            request.setReason("预约订单取消退款");
            // 退款结果通知地址
            request.setNotifyUrl(path + FucciConstant.REFUND_NOTIFY_URI);
            // 】订单退款金额信息
            WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
            amount.setRefund(BaseWxPayRequest.yuan2Fen(order.getFare()));
            amount.setTotal(BaseWxPayRequest.yuan2Fen(order.getFare()));
            amount.setCurrency("CNY");
            request.setAmount(amount);
            log.info("微信支付================取消预约订单，进行退款处理请求参数：{}", JSONObject.toJSONString(request));
            try {
                // 微信支付-申请退款处理
                WxPayRefundV3Result refundV3Result = wxService.refundV3(request);
                log.info("微信支付================取消预约订单，进行退款处理结果：{}", JSONObject.toJSONString(refundV3Result));
            } catch (WxPayException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> payNotifySuccess(HttpServletRequest request, String notifyData) {
        SignatureHeader header = getRequestHeader(request);
        try {
            WxPayNotifyV3Result res = wxService.parseOrderNotifyV3Result(notifyData, header);
            WxPayNotifyV3Result.DecryptNotifyResult decryptRes = res.getResult();
            log.info("微信支付================支付成功回调通知结果：{}", JSONObject.toJSONString(decryptRes));
            if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(decryptRes.getTradeState())) {
                log.info("微信支付================支付成功回调通知结果====商户订单号：{}", decryptRes.getOutTradeNo());
                // 查询是否存在「预约状态为 3:已预约（订单待支付）」的预约订单数据
                LambdaQueryWrapper<FcFishOrder> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                orderLambdaQueryWrapper.eq(FcFishOrder::getId, decryptRes.getOutTradeNo());
                orderLambdaQueryWrapper.eq(FcFishOrder::getStatus, "3");
                FcFishOrder dbOrder = fcFishOrderMapper.selectOne(orderLambdaQueryWrapper);
                if (null != dbOrder) {
                    // 更新预约订单状态为 1:已预约（支付完成）
                    FcFishOrder order = new FcFishOrder();
                    order.setId(decryptRes.getOutTradeNo());
                    order.setStatus("1");
                    fcFishOrderMapper.updateById(order);
                    // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）（VIP 会员用户下单支付成功，会员次数减1）
                    LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
                    vipQueryWrapper.eq(FcFishVip::getUserId, dbOrder.getUserId());
                    FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
                    if (null != fcFishVip && fcFishVip.getCount() > 0) {
                        // 会员次数减1
                        fcFishVip.setCount(fcFishVip.getCount() - 1);
                        fcFishVipService.updateById(fcFishVip);
                    }
                }
                // 成功返回200/204，body 无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                // 失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            // 失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        }
    }

    @Override
    public ResponseEntity<String> payNotifyRefund(HttpServletRequest request, String notifyData) {
        SignatureHeader header = getRequestHeader(request);
        try {
            WxPayRefundNotifyV3Result res = wxService.parseRefundNotifyV3Result(notifyData, header);
            WxPayRefundNotifyV3Result.DecryptNotifyResult decryptRes = res.getResult();
            log.info("微信支付================退款回调通知结果：{}", JSONObject.toJSONString(decryptRes));
            if (WxPayConstants.RefundStatus.SUCCESS.equals(decryptRes.getRefundStatus())) {
                log.info("微信支付================退款回调通知结果====商户订单号：{}", decryptRes.getOutTradeNo());
                // 成功返回200/204，body无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                // 失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            // 失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        }
    }

    /**
     * 组装请求头的签名信息
     *
     * @param request 请求信息
     * @return 请求头的签名信息
     */
    private SignatureHeader getRequestHeader(HttpServletRequest request) {
        // 获取通知签名
        String signature = request.getHeader("Wechatpay-Signature");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(signature);
        signatureHeader.setNonce(nonce);
        signatureHeader.setSerial(serial);
        signatureHeader.setTimeStamp(timestamp);
        return signatureHeader;
    }

}
