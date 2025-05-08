package org.jeecg.modules.fucci.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.fucci.pojo.dto.FucciUserDTO;
import org.jeecg.modules.fucci.pojo.dto.FucciUserLoginDTO;

import javax.servlet.http.HttpServletRequest;

public interface IFucciUserService {

    /**
     * 微信小程序注册登录接口
     *
     * @param fucciUserLoginDTO 福羲用户登录信息
     * @return 福羲用户信息
     */
    JSONObject login(FucciUserLoginDTO fucciUserLoginDTO);

    /**
     * 用户信息查询接口
     *
     * @param request 请求信息
     * @return 福羲用户信息
     */
    JSONObject getUserInfo(HttpServletRequest request);

    /**
     * 用户信息更新接口
     *
     * @param request      请求信息
     * @param fucciUserDTO 福羲用户信息
     * @return 福羲用户信息
     */
    JSONObject updateUserInfo(HttpServletRequest request, FucciUserDTO fucciUserDTO);

    /**
     * 微信小程序用户登出接口
     *
     * @param request 请求信息
     * @return 微信小程序用户登出结果
     */
    String logout(HttpServletRequest request);

}
