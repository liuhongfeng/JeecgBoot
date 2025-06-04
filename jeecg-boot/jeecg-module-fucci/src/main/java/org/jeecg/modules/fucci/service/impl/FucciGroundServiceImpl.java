package org.jeecg.modules.fucci.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecg.modules.admin.ground.entity.FcFishGround;
import org.jeecg.modules.admin.ground.service.IFcFishBoatService;
import org.jeecg.modules.admin.ground.service.IFcFishGroundService;
import org.jeecg.modules.admin.order.entity.FcFishOrder;
import org.jeecg.modules.admin.order.mapper.FcFishOrderMapper;
import org.jeecg.modules.admin.vip.entity.FcFishVip;
import org.jeecg.modules.admin.vip.service.IFcFishVipService;
import org.jeecg.modules.fucci.common.constant.FucciConstant;
import org.jeecg.modules.fucci.config.FucciProperties;
import org.jeecg.modules.fucci.pojo.dto.FucciGroundBoatOrderDTO;
import org.jeecg.modules.fucci.pojo.vo.*;
import org.jeecg.modules.fucci.service.IFucciGroundService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author lhf
 * @date 2025-04-15
 * @describe
 */
@Slf4j
@Service
@AllArgsConstructor
public class FucciGroundServiceImpl implements IFucciGroundService {

    private ISysUserService sysUserService;
    private IFcFishGroundService fcFishGroundService;
    private IFcFishBoatService fcFishBoatService;
    private IFcFishVipService fcFishVipService;
    private FcFishOrderMapper fcFishOrderMapper;
    private FucciProperties fucciProperties;
    private WxPayService wxService;

    @Override
    public JSONObject list() {
        // 环境访问地址
        String path = fucciProperties.getPath();
        JSONObject jsonObject = new JSONObject();
        List<FucciGroundVO> fucciGroundVOS = new ArrayList<>();
        // 查询营业中的钓场信息（status = 1）
        LambdaQueryWrapper<FcFishGround> fishGroundWrapper = new LambdaQueryWrapper<>();
        fishGroundWrapper.eq(FcFishGround::getStatus, "1");
        List<FcFishGround> fcFishGrounds = fcFishGroundService.list(fishGroundWrapper);
        fcFishGrounds.forEach(fcFishGround -> {
            FucciGroundVO fucciGroundVO = new FucciGroundVO();
            BeanUtils.copyProperties(fcFishGround, fucciGroundVO);
            if (StringUtils.isNotEmpty(path)) {
                // 完整图片访问地址
                fucciGroundVO.setHomeImage(path + FucciConstant.STATIC_PATH + fucciGroundVO.getHomeImage());
            }
            // TODO 月预约数量，暂时写固定值，后续修改
            fucciGroundVO.setMonthlyReservation(300);
            fucciGroundVOS.add(fucciGroundVO);
        });
        jsonObject.put("records", fucciGroundVOS);
        return jsonObject;
    }

    @Override
    public FucciGroundDetailsVO details(HttpServletRequest request, String id) {
        // 查询用户信息
        SysUser sysUser = null;
        String accessToken = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (StringUtils.isNotEmpty(accessToken)) {
            String username = JwtUtil.getUserNameByToken(request);
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getUsername, username);
            sysUser = sysUserService.getOne(queryWrapper);
        }
        // 环境访问地址
        String path = fucciProperties.getPath();
        FcFishGround fcFishGround = fcFishGroundService.getById(id);
        FucciGroundDetailsVO fucciGroundDetailsVO = new FucciGroundDetailsVO();
        BeanUtils.copyProperties(fcFishGround, fucciGroundDetailsVO);
        // 钓场详情图片数据
        String detailsImage = fcFishGround.getDetailsImage();
        List<String> detailsImages = new ArrayList<>(Arrays.asList(detailsImage.split(",")));
        if (StringUtils.isNotEmpty(path)) {
            // 完整图片访问地址
            detailsImages.replaceAll(image -> path + FucciConstant.STATIC_PATH + image);
        }
        fucciGroundDetailsVO.setDetailsImages(detailsImages);
        fucciGroundDetailsVO.setVip(false);
        if (null != sysUser) {
            // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getCount() > 0) {
                fucciGroundDetailsVO.setVip(true);
            }
        }
        return fucciGroundDetailsVO;
    }

    @Override
    public FucciGroundOrderVO order(HttpServletRequest request, String id, String date) {
        // 查询用户信息
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 钓场船只预约信息
        FucciGroundOrderVO fucciGroundOrderVO = new FucciGroundOrderVO();
        FcFishGround fcFishGround = fcFishGroundService.getById(id);
        BeanUtils.copyProperties(fcFishGround, fucciGroundOrderVO);
        fucciGroundOrderVO.setVip(false);
        if (null != sysUser) {
            // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getCount() > 0) {
                fucciGroundOrderVO.setVip(true);
            }
        }
        // 查询钓场未来 30 天钓场船只预约数据，将按日期分组后的数据转成 Map
        List<FucciGroundOrderDateVO> groundOrderDateList = fcFishOrderMapper.queryGroundOrderDateList(id);
        Map<String, Integer> groundOrderDateMap = groundOrderDateList.stream()
                .collect(Collectors.toMap(FucciGroundOrderDateVO::getOrderDate, FucciGroundOrderDateVO::getOrderQuantity, (v1, v2) -> v1));
        // 构造未来 30 天的预约量列表（含今天）
        List<FucciGroundOrderDateVO> groundOrderDateVOS = IntStream.range(0, 30)
                .mapToObj(i -> {
                    String orderDate = LocalDate.now().plusDays(i).toString();
                    Integer orderQuantity = groundOrderDateMap.getOrDefault(orderDate, 0);
                    FucciGroundOrderDateVO vo = new FucciGroundOrderDateVO();
                    vo.setOrderDate(orderDate);
                    vo.setOrderQuantity(orderQuantity);
                    return vo;
                }).collect(Collectors.toList());
        fucciGroundOrderVO.setGroundOrderDateList(groundOrderDateVOS);
        // 默认查询今天船只预约信息，可根据具体日期查询船只预约信息
        String queryDate = DateUtil.today();
        if (StringUtils.isNotEmpty(date)) {
            queryDate = date;
        }
        List<FucciGroundBoatOrderVO> groundBoatOrderList = fcFishOrderMapper.queryGroundBoatOrderListByDate(id, queryDate);
        fucciGroundOrderVO.setGroundBoatOrderList(groundBoatOrderList);
        return fucciGroundOrderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FucciOrderPayVO confirmOrder(HttpServletRequest request, FucciGroundBoatOrderDTO groundBoatOrderDTO) {
        // 钓场船只预约下单返参数据（新增预约和修改预约）
        FucciOrderPayVO orderPayVO = new FucciOrderPayVO();
        // 查询用户信息
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 1. 检查船只数据状态
        FcFishBoat fishBoat = fcFishBoatService.getById(groundBoatOrderDTO.getBoatId());
        if (null == fishBoat) {
            throw new JeecgBootException("船只不存在");
        }
        // 2. 检查钓场数据状态
        FcFishGround fishGround = fcFishGroundService.getById(fishBoat.getGroundId());
        if (null == fishGround) {
            throw new JeecgBootException("钓场不存在");
        }
        // 3. 检查船只是否已被预约，查询预约状态为：1:已预约（支付完成） 3:已预约（订单待支付）的数据
        LambdaQueryWrapper<FcFishOrder> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper.eq(FcFishOrder::getGroundId, fishGround.getId());
        orderLambdaQueryWrapper.eq(FcFishOrder::getDate, DateUtil.parse(groundBoatOrderDTO.getDate()));
        orderLambdaQueryWrapper.eq(FcFishOrder::getBoatId, fishBoat.getId());
        orderLambdaQueryWrapper.in(FcFishOrder::getStatus, "1", "3");
        Long count = fcFishOrderMapper.selectCount(orderLambdaQueryWrapper);
        if (count > 0) {
            throw new JeecgBootException("该日期此船只已被预约，请重新选择");
        }
        // 修改预约订单处理
        if (StringUtils.isNotEmpty(groundBoatOrderDTO.getId())) {
            // 查询预约订单数据
            FcFishOrder fcFishOrder = fcFishOrderMapper.selectById(groundBoatOrderDTO.getId());
            // 校验预约订单修改数据
            validateOrderModification(fcFishOrder, groundBoatOrderDTO);
            // 修改预约订单数据
            fcFishOrder.setDate(DateUtil.parse(groundBoatOrderDTO.getDate()));
            fcFishOrder.setBoatId(groundBoatOrderDTO.getBoatId());
            // 预约修改次数加 1
            fcFishOrder.setModifyCount(fcFishOrder.getModifyCount() + 1);
            fcFishOrderMapper.updateById(fcFishOrder);
            orderPayVO.setOrderId(fcFishOrder.getId());
            return orderPayVO;
        }
        // 新增预约订单处理
        boolean vip = false;
        if (null != sysUser) {
            // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getCount() > 0) {
                vip = true;
            }
        }
        // 检查票价是否正确
        if (vip) {
            // 匹配 VIP 价格
            if (groundBoatOrderDTO.getFare().compareTo(fishGround.getVipPrice()) != 0) {
                throw new JeecgBootException("票价有误，请重新进入此页面预约");
            }
        } else {
            // 匹配普通价格
            if (groundBoatOrderDTO.getFare().compareTo(fishGround.getPrice()) != 0) {
                throw new JeecgBootException("票价有误，请重新进入此页面预约");
            }
        }
        // 预约订单ID（雪花算法生成）也用做微信支付的「商户订单号」
        String snowflakeId = String.valueOf(IdUtil.getSnowflake(1, 1).nextId());
        log.info("微信支付================预约订单ID：{}", snowflakeId);
        // 调用微信支付-小程序下单接口
        WxPayUnifiedOrderV3Request orderV3Request = getWxPayUnifiedOrderV3Request(snowflakeId, groundBoatOrderDTO.getFare(), sysUser.getThirdId());
        WxPayUnifiedOrderV3Result.JsapiResult result;
        try {
            result = wxService.createOrderV3(TradeTypeEnum.JSAPI, orderV3Request);
            log.info("微信支付================预约订单下单结果：{}", JSONObject.toJSONString(result));
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        }
        // 保存钓场船只预约数据
        FcFishOrder fcFishOrder = new FcFishOrder();
        fcFishOrder.setId(snowflakeId);
        fcFishOrder.setUserId(sysUser.getId());
        fcFishOrder.setUsername(sysUser.getUsername());
        fcFishOrder.setRealname(sysUser.getRealname());
        fcFishOrder.setGroundId(fishGround.getId());
        fcFishOrder.setGroundName(fishGround.getName());
        fcFishOrder.setDate(DateUtil.parse(groundBoatOrderDTO.getDate()));
        fcFishOrder.setBoatId(fishBoat.getId());
        fcFishOrder.setBoatNumber(fishBoat.getBoatNumber());
        fcFishOrder.setName(groundBoatOrderDTO.getName());
        fcFishOrder.setPhone(groundBoatOrderDTO.getPhone());
        fcFishOrder.setFare(groundBoatOrderDTO.getFare());
        // 预约状态 1:已预约（支付完成） 2:已取消预约（已退款） 3:已预约（订单待支付） 4:已取消预约（超时关闭订单）
        fcFishOrder.setStatus("3");
        fcFishOrderMapper.insert(fcFishOrder);
        // 钓场船只预约下单返参数据
        orderPayVO.setOrderId(snowflakeId);
        BeanUtils.copyProperties(result, orderPayVO);
        return orderPayVO;
    }

    /**
     * 校验预约订单修改数据
     *
     * @param fcFishOrder        预约订单数据
     * @param groundBoatOrderDTO 预约船只订单数据
     */
    private void validateOrderModification(FcFishOrder fcFishOrder, FucciGroundBoatOrderDTO groundBoatOrderDTO) {
        if (null == fcFishOrder) {
            throw new JeecgBootException("预约订单不存在，无法修改");
        }
        if (!groundBoatOrderDTO.getName().equals(fcFishOrder.getName())) {
            throw new JeecgBootException("预约订单姓名不能修改");
        }
        if (!groundBoatOrderDTO.getPhone().equals(fcFishOrder.getPhone())) {
            throw new JeecgBootException("预约订单手机号不能修改");
        }
        if (groundBoatOrderDTO.getFare().compareTo(fcFishOrder.getFare()) != 0) {
            throw new JeecgBootException("预约订单票价不能修改");
        }
        // 检查【预约日期】和【预约船只】是否都未修改
        if (fcFishOrder.getDate().compareTo(DateUtil.parse(groundBoatOrderDTO.getDate())) == 0
                && fcFishOrder.getBoatId().equals(groundBoatOrderDTO.getBoatId())) {
            throw new JeecgBootException("预约日期和船只都未修改，请重新选择");
        }
        // 检查预约订单状态，1:已预约（支付完成）状态的订单才可以修改
        if (!"1".equals(fcFishOrder.getStatus())) {
            throw new JeecgBootException("预约订单状态异常，无法修改");
        }
        // 检查预约订单是否未修改过，修改次数 modifyCount 为 0 时，才可以修改
        if (0 != fcFishOrder.getModifyCount()) {
            throw new JeecgBootException("预约订单已修改过，无法再次修改");
        }
        // 检查预约订单日期是否可以修改
        long betweenDay = DateUtil.between(fcFishOrder.getDate(), new Date(), DateUnit.HOUR);
        if (new Date().after(fcFishOrder.getDate()) || betweenDay <= 72) {
            throw new JeecgBootException("预约订单日期只能提前 3 天修改，当前时间无法修改");
        }
    }

    @NotNull
    private WxPayUnifiedOrderV3Request getWxPayUnifiedOrderV3Request(String snowflakeId, BigDecimal fare, String thirdId) {
        // 环境访问地址
        String path = fucciProperties.getPath();
        WxPayUnifiedOrderV3Request orderV3Request = new WxPayUnifiedOrderV3Request();
        // description 商品描述（必填）
        orderV3Request.setDescription("预约船只");
        // out_trade_no 商户订单号（必填）
        orderV3Request.setOutTradeNo(snowflakeId);
        // 设置订单失效时间为当前时间 + 15分钟（北京时间）
        ZonedDateTime expireTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).plusMinutes(15);
        String timeExpireStr = expireTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // time_expire 支付结束时间（选填）
        orderV3Request.setTimeExpire(timeExpireStr);
        // notify_url 商户回调地址（必填）
        orderV3Request.setNotifyUrl(path + FucciConstant.ORDER_NOTIFY_URI);
        // amount 订单金额（必填）
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(BaseWxPayRequest.yuan2Fen(fare));
        orderV3Request.setAmount(amount);
        // payer 支付者信息（必填）
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(thirdId);
        orderV3Request.setPayer(payer);
        return orderV3Request;
    }

}
