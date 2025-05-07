package org.jeecg.modules.fucci.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciGroundServiceImpl implements IFucciGroundService {

    private final ISysUserService sysUserService;
    private final IFcFishGroundService fcFishGroundService;
    private final IFcFishBoatService fcFishBoatService;
    private final IFcFishVipService fcFishVipService;
    private final FcFishOrderMapper fcFishOrderMapper;
    private final FucciProperties fucciProperties;

    @Override
    public JSONObject list() {
        // 环境访问地址
        String path = fucciProperties.getPath();
        JSONObject jsonObject = new JSONObject();
        List<FucciGroundVO> fucciGroundVOS = new ArrayList<>();
        List<FcFishGround> fcFishGrounds = fcFishGroundService.list();
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
        String accessToken = request.getHeader("X-Access-Token");
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
            // 查询用户是否为 VIP 会员用户（且没有超过会员结束时间）
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getEndTime().after(DateUtil.date())) {
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
            // 查询用户是否为 VIP 会员用户（且没有超过会员结束时间）
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getEndTime().after(DateUtil.date())) {
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
    public String confirmOrder(HttpServletRequest request, FucciGroundBoatOrderDTO groundBoatOrderDTO) {
        // 1. 查询用户信息
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 查询用户是否为 VIP 会员用户（且没有超过会员结束时间）
        boolean vip = false;
        if (null != sysUser) {
            LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
            vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
            FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
            if (fcFishVip != null && fcFishVip.getEndTime().after(DateUtil.date())) {
                vip = true;
            }
        }
        // 2. 检查船只数据状态
        FcFishBoat fishBoat = fcFishBoatService.getById(groundBoatOrderDTO.getBoatId());
        if (null == fishBoat) {
            throw new JeecgBootException("船只不存在");
        }
        // 3. 检查钓场数据状态
        FcFishGround fishGround = fcFishGroundService.getById(fishBoat.getGroundId());
        if (null == fishGround) {
            throw new JeecgBootException("钓场不存在");
        }
        // 4. 检查票价是否正确
        if (vip) {
            // 匹配 VIP 价格
            if (!Objects.equals(groundBoatOrderDTO.getFare(), fishGround.getVipPrice())) {
                throw new JeecgBootException("票价有误，请重新进入此页面预约");
            }
        } else {
            // 匹配普通价格
            if (!Objects.equals(groundBoatOrderDTO.getFare(), fishGround.getPrice())) {
                throw new JeecgBootException("票价有误，请重新进入此页面预约");
            }
        }
        // 5. 检查船只是否已被预约
        LambdaQueryWrapper<FcFishOrder> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper.eq(FcFishOrder::getGroundId, fishGround.getId());
        orderLambdaQueryWrapper.eq(FcFishOrder::getDate, DateUtil.parse(groundBoatOrderDTO.getDate()));
        orderLambdaQueryWrapper.eq(FcFishOrder::getBoatId, fishBoat.getId());
        Long count = fcFishOrderMapper.selectCount(orderLambdaQueryWrapper);
        if (count > 0) {
            throw new JeecgBootException("该日期此船只已被预约，请重新选择");
        }
        // 保存钓场船只预约数据
        FcFishOrder fcFishOrder = new FcFishOrder();
        fcFishOrder.setUserId(sysUser.getId());
        fcFishOrder.setRealname(sysUser.getRealname());
        fcFishOrder.setGroundId(fishGround.getId());
        fcFishOrder.setGroundName(fishGround.getName());
        fcFishOrder.setDate(DateUtil.parse(groundBoatOrderDTO.getDate()));
        fcFishOrder.setBoatId(fishBoat.getId());
        fcFishOrder.setBoatNumber(fishBoat.getBoatNumber());
        fcFishOrder.setPhone(groundBoatOrderDTO.getPhone());
        fcFishOrder.setFare(groundBoatOrderDTO.getFare());
        // 预约状态 1:已预约 2:已取消预约
        fcFishOrder.setStatus("1");
        fcFishOrderMapper.insert(fcFishOrder);
        return fcFishOrder.getId();
    }

}
