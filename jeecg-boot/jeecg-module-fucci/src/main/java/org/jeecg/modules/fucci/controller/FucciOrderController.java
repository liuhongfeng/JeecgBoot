package org.jeecg.modules.fucci.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.fucci.service.IFucciOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-27
 * @describe
 */
@Api(tags = "福羲项目-预约相关接口")
@RestController
@RequestMapping("/fucci/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciOrderController {

    private final IFucciOrderService fucciOrderService;

    @ApiOperation(value = "我的预约信息列表查询接口")
    @GetMapping
    public Result<?> list(HttpServletRequest request,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.ok(fucciOrderService.list(request, pageNo, pageSize));
    }

    @ApiOperation(value = "预约完成详情信息查询接口")
    @GetMapping("/details")
    public Result<?> details(@RequestParam(name = "id") String id) {
        return Result.ok(fucciOrderService.details(id));
    }

    @ApiOperation(value = "工作人员-预约日期列表查询接口")
    @GetMapping("/staff")
    public Result<?> staffOrderDatelist(HttpServletRequest request) {
        return Result.ok(fucciOrderService.staffOrderDateList(request));
    }

}
