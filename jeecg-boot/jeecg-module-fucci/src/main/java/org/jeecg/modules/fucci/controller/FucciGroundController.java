package org.jeecg.modules.fucci.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.fucci.pojo.dto.FucciGroundBoatOrderDTO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundDetailsVO;
import org.jeecg.modules.fucci.pojo.vo.FucciGroundOrderVO;
import org.jeecg.modules.fucci.service.IFucciGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhf
 * @date 2025-04-14
 * @describe
 */
@Api(tags = "福羲项目-钓场相关接口")
@RestController
@RequestMapping("/fucci/ground")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FucciGroundController {

    private final IFucciGroundService fucciGroundService;

    @ApiOperation(value = "钓场信息列表查询接口")
    @GetMapping
    public Result<?> list() {
        return Result.ok(fucciGroundService.list());
    }

    @ApiOperation(value = "钓场信息详情接口")
    @GetMapping("/details")
    public Result<FucciGroundDetailsVO> details(@RequestParam(name = "id") String id) {
        return Result.ok(fucciGroundService.details(id));
    }

    @ApiOperation(value = "钓场船只预约信息查询接口")
    @GetMapping("/order")
    public Result<FucciGroundOrderVO> order(@RequestParam(name = "id") String id,
                                            @RequestParam(name = "date", required = false) String date) {
        return Result.ok(fucciGroundService.order(id, date));
    }

    @ApiOperation(value = "钓场船只确认预约接口")
    @PostMapping("/order")
    public Result<String> confirmOrder(HttpServletRequest request,
                                       @RequestBody FucciGroundBoatOrderDTO groundBoatOrderDTO) {
        return Result.ok(fucciGroundService.confirmOrder(request, groundBoatOrderDTO));
    }

}
