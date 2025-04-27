package org.jeecg.modules.fucci.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.admin.order.mapper.FcFishOrderMapper;
import org.jeecg.modules.fucci.pojo.vo.FucciOrderVO;
import org.jeecg.modules.fucci.service.IFucciOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

}
