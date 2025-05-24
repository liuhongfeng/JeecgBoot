package org.jeecg.modules.fucci.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.admin.staff.entity.FcFishStaff;
import org.jeecg.modules.admin.staff.service.IFcFishStaffService;
import org.jeecg.modules.admin.vip.entity.FcFishVip;
import org.jeecg.modules.admin.vip.service.IFcFishVipService;
import org.jeecg.modules.fucci.common.constant.FucciConstant;
import org.jeecg.modules.fucci.common.util.UserIdGenerator;
import org.jeecg.modules.fucci.pojo.dto.FucciUserDTO;
import org.jeecg.modules.fucci.pojo.dto.FucciUserLoginDTO;
import org.jeecg.modules.fucci.pojo.vo.FucciUserVO;
import org.jeecg.modules.fucci.service.IFucciUserService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciUserServiceImpl implements IFucciUserService {

    private final RedisUtil redisUtil;
    private final WxMaService wxMaService;
    private final ISysUserService sysUserService;
    private final IFcFishVipService fcFishVipService;
    private final IFcFishStaffService fcFishStaffService;

    @Override
    public JSONObject login(FucciUserLoginDTO fucciUserLoginDTO) {
        JSONObject jsonObject = new JSONObject();
        String wxCode = fucciUserLoginDTO.getWxCode();
        // 通过微信 code 获取微信用户唯一标识 openid
        String openid;
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxCode);
            openid = session.getOpenid();
        } catch (WxErrorException e) {
            throw new JeecgBootException(e.getMessage());
        } finally {
            // 清理 ThreadLocal
            WxMaConfigHolder.remove();
        }
        // 根据 openid 查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getThirdId, openid);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (sysUser == null) {
            // 用户注册信息
            SysUser newUser = new SysUser();
            // 生成「登录账号」（用户ID）
            String username = UserIdGenerator.generateUserId();
            // md5 密码盐
            String salt = oConvertUtils.randomGen(8);
            String passwordEncode = PasswordUtil.encrypt(username, FucciConstant.User.PASSWORD, salt);
            newUser.setSalt(salt);
            newUser.setUsername(username);
            newUser.setRealname(FucciConstant.User.REALNAME);
            newUser.setPassword(passwordEncode);
            newUser.setUserIdentity(1);
            newUser.setAvatar(FucciConstant.User.AVATAR_URL);
            // 第三方登录的唯一标识（微信用户唯一标识 openid）
            newUser.setThirdId(openid);
            newUser.setThirdType(FucciConstant.User.THIRD_TYPE);
            newUser.setCreateBy(FucciConstant.User.CREATE_BY);
            newUser.setCreateTime(new Date());
            newUser.setStatus(CommonConstant.USER_UNFREEZE);
            newUser.setDelFlag(CommonConstant.DEL_FLAG_0);
            newUser.setActivitiSync(CommonConstant.ACT_SYNC_1);
            sysUserService.save(newUser);
            sysUser = newUser;
        }
        FucciUserVO fucciUserVO = getFucciUserVO(sysUser);
        jsonObject.put("userInfo", fucciUserVO);
        // 生成 token
        String sysUserUsername = sysUser.getUsername();
        String sysUserPassword = sysUser.getPassword();
        String token = JwtUtil.sign(sysUserUsername, sysUserPassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        jsonObject.put("token", token);
        return jsonObject;
    }

    @Override
    public JSONObject getUserInfo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = JwtUtil.getUserNameByToken(request);
        if (StringUtils.isNotEmpty(username)) {
            // 根据登录账号（用户ID）查询用户信息
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getUsername, username);
            SysUser sysUser = sysUserService.getOne(queryWrapper);
            // 设置福羲用户信息
            FucciUserVO fucciUserVO = getFucciUserVO(sysUser);
            jsonObject.put("userInfo", fucciUserVO);
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateUserInfo(HttpServletRequest request, FucciUserDTO fucciUserDTO) {
        JSONObject jsonObject = new JSONObject();
        String username = JwtUtil.getUserNameByToken(request);
        if (StringUtils.isNotEmpty(username)) {
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getUsername, username);
            SysUser sysUser = sysUserService.getOne(queryWrapper);
            // 用户信息更新对象
            SysUser updateSysUser = new SysUser();
            updateSysUser.setId(sysUser.getId());
            // 更新【昵称】
            if (StringUtils.isNotEmpty(fucciUserDTO.getNickname())) {
                updateSysUser.setRealname(fucciUserDTO.getNickname());
                sysUser.setRealname(fucciUserDTO.getNickname());
            }
            // 更新【头像】
            if (StringUtils.isNotEmpty(fucciUserDTO.getAvatarUrl())) {
                updateSysUser.setAvatar(fucciUserDTO.getAvatarUrl());
                sysUser.setAvatar(fucciUserDTO.getAvatarUrl());
            }
            sysUserService.updateById(updateSysUser);
            // 设置福羲用户信息
            FucciUserVO fucciUserVO = getFucciUserVO(sysUser);
            jsonObject.put("userInfo", fucciUserVO);
        }
        return jsonObject;
    }

    /**
     * 设置福羲路亚用户信息
     *
     * @param sysUser 系统用户信息
     * @return 福羲路亚用户信息
     */
    private FucciUserVO getFucciUserVO(SysUser sysUser) {
        FucciUserVO fucciUserVO = new FucciUserVO();
        fucciUserVO.setUserId(sysUser.getUsername());
        fucciUserVO.setNickname(sysUser.getRealname());
        fucciUserVO.setAvatarUrl(sysUser.getAvatar());
        fucciUserVO.setVip(false);
        fucciUserVO.setStaff(false);
        // 查询用户是否为 VIP 会员用户（且会员使用次数大于0）
        LambdaQueryWrapper<FcFishVip> vipQueryWrapper = new LambdaQueryWrapper<>();
        vipQueryWrapper.eq(FcFishVip::getUserId, sysUser.getId());
        FcFishVip fcFishVip = fcFishVipService.getOne(vipQueryWrapper);
        if (fcFishVip != null && fcFishVip.getCount() > 0) {
            fucciUserVO.setVip(true);
            fucciUserVO.setVipStartTime(fcFishVip.getStartTime());
            fucciUserVO.setVipEndTime(fcFishVip.getEndTime());
            fucciUserVO.setVipCount(fcFishVip.getCount());
        }
        // 查询用户是否为工作人员（工作人员只能服务一个钓场）
        LambdaQueryWrapper<FcFishStaff> staffQueryWrapper = new LambdaQueryWrapper<>();
        staffQueryWrapper.eq(FcFishStaff::getUserId, sysUser.getId());
        FcFishStaff fcFishStaff = fcFishStaffService.getOne(staffQueryWrapper);
        if (fcFishStaff != null) {
            fucciUserVO.setStaff(true);
        }
        return fucciUserVO;
    }

    @Override
    public String logout(HttpServletRequest request) {
        // 用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            throw new JeecgBootException("Token 为空，登出失败！");
        }
        String username = JwtUtil.getUsername(token);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (sysUser == null) {
            throw new JeecgBootException("Token 无效，登出失败！");
        }
        // 清除用户登录 Token 缓存
        redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
        // 清空用户的缓存信息
        redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
        log.info("用户名：" + sysUser.getRealname() + "，登出成功！");
        return "登出成功！";
    }

}
