package org.jeecg.modules.fucci.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.admin.ground.entity.FcFishGround;
import org.jeecg.modules.admin.ground.service.IFcFishGroundService;
import org.jeecg.modules.fucci.common.constant.FucciConstant;
import org.jeecg.modules.fucci.config.FucciProperties;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundDetailsVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundVO;
import org.jeecg.modules.fucci.service.IFucciGroundService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final FucciProperties fucciProperties;

    @Override
    public JSONObject list() {
        JSONObject jsonObject = new JSONObject();
        List<FucciGroundVO> fucciGroundVOS = new ArrayList<>();
        List<FcFishGround> fcFishGrounds = fcFishGroundService.list();
        fcFishGrounds.forEach(fcFishGround -> {
            FucciGroundVO fucciGroundVO = new FucciGroundVO();
            BeanUtils.copyProperties(fcFishGround, fucciGroundVO);
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

}
