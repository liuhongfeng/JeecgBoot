package org.jeecg.modules.fucci.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.admin.order.mapper.FcFishOrderMapper;
import org.jeecg.modules.admin.staff.entity.FcFishStaff;
import org.jeecg.modules.admin.staff.service.IFcFishStaffService;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundStaffOrderDateVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundStaffOrderVO;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;
import org.jeecg.modules.fucci.service.IFucciOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciOrderServiceImpl implements IFucciOrderService {

    private final ISysUserService sysUserService;
    private final IFcFishStaffService fcFishStaffService;
    private final FcFishOrderMapper fcFishOrderMapper;

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
                groundStaffOrderVO.setOrderDateList(entry.getValue());
                return groundStaffOrderVO;
            }).sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())).collect(Collectors.toList());
        }
        jsonObject.put("records", groundStaffOrderVOList);
        return jsonObject;
    }

}
