package org.jeecg.modules.fucci.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.fucci.pojo.dto.FucciUserDTO;
import org.jeecg.modules.fucci.pojo.dto.FucciUserLoginDTO;
import org.jeecg.modules.fucci.service.IFucciUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-12
 * @describe 福羲项目-用户相关接口
 */
@Api(tags = "福羲项目-用户相关接口")
@RestController
@RequestMapping("/fucci/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciUserController {

    private final IFucciUserService fucciUserService;

    @ApiOperation(value = "微信小程序注册登录接口")
    @PostMapping("/login")
    public Result<?> login(@RequestBody FucciUserLoginDTO fucciUserLoginDTO) {
        return Result.ok(fucciUserService.login(fucciUserLoginDTO));
    }

    @ApiOperation(value = "用户信息查询接口")
    @GetMapping
    public Result<?> getUserInfo(HttpServletRequest request) {
        return Result.ok(fucciUserService.getUserInfo(request));
    }

    @ApiOperation(value = "用户信息更新接口")
    @PostMapping
    public Result<?> updateUserInfo(HttpServletRequest request, @RequestBody FucciUserDTO fucciUserDTO) {
        return Result.ok(fucciUserService.updateUserInfo(request, fucciUserDTO));
    }

}
