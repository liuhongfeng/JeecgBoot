package org.jeecg.modules.fucci.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.admin.ground.entity.FcFishGround;
import org.jeecg.modules.admin.ground.service.IFcFishGroundService;
import org.jeecg.modules.admin.order.mapper.FcFishOrderMapper;
import org.jeecg.modules.fucci.common.constant.FucciConstant;
import org.jeecg.modules.fucci.config.FucciProperties;
import org.jeecg.modules.fucci.pojo.vo.*;
import org.jeecg.modules.fucci.service.IFucciGroundService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private final IFcFishGroundService fcFishGroundService;
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
    public FucciGroundDetailsVO details(String id) {
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
        // TODO 是否 VIP，暂时写固定值，后续根据用户查询
        fucciGroundDetailsVO.setVip(true);
        return fucciGroundDetailsVO;
    }

    public FucciGroundOrderVO order(String id, String date) {
        FucciGroundOrderVO fucciGroundOrderVO = new FucciGroundOrderVO();
        FcFishGround fcFishGround = fcFishGroundService.getById(id);
        BeanUtils.copyProperties(fcFishGround, fucciGroundOrderVO);
        // TODO 是否 VIP，暂时写固定值，后续根据用户查询
        fucciGroundOrderVO.setVip(true);
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

}
