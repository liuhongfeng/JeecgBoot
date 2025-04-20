package org.jeecg.modules.admin.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.admin.order.entity.FcFishOrder;
import org.jeecg.modules.admin.order.service.IFcFishOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 钓场船只预约
 * @Author: jeecg-boot
 * @Date: 2025-04-20
 * @Version: V1.0
 */
@Api(tags = "钓场船只预约")
@RestController
@RequestMapping("/order/fcFishOrder")
@Slf4j
public class FcFishOrderController extends JeecgController<FcFishOrder, IFcFishOrderService> {
    @Autowired
    private IFcFishOrderService fcFishOrderService;

    /**
     * 分页列表查询
     *
     * @param fcFishOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "钓场船只预约-分页列表查询")
    @ApiOperation(value = "钓场船只预约-分页列表查询", notes = "钓场船只预约-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FcFishOrder>> queryPageList(FcFishOrder fcFishOrder,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<FcFishOrder> queryWrapper = QueryGenerator.initQueryWrapper(fcFishOrder, req.getParameterMap());
        Page<FcFishOrder> page = new Page<FcFishOrder>(pageNo, pageSize);
        IPage<FcFishOrder> pageList = fcFishOrderService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param fcFishOrder
     * @return
     */
    @AutoLog(value = "钓场船只预约-添加")
    @ApiOperation(value = "钓场船只预约-添加", notes = "钓场船只预约-添加")
    @RequiresPermissions("order:fc_fish_order:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FcFishOrder fcFishOrder) {
        fcFishOrderService.save(fcFishOrder);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fcFishOrder
     * @return
     */
    @AutoLog(value = "钓场船只预约-编辑")
    @ApiOperation(value = "钓场船只预约-编辑", notes = "钓场船只预约-编辑")
    @RequiresPermissions("order:fc_fish_order:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FcFishOrder fcFishOrder) {
        fcFishOrderService.updateById(fcFishOrder);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "钓场船只预约-通过id删除")
    @ApiOperation(value = "钓场船只预约-通过id删除", notes = "钓场船只预约-通过id删除")
    @RequiresPermissions("order:fc_fish_order:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fcFishOrderService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "钓场船只预约-批量删除")
    @ApiOperation(value = "钓场船只预约-批量删除", notes = "钓场船只预约-批量删除")
    @RequiresPermissions("order:fc_fish_order:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fcFishOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "钓场船只预约-通过id查询")
    @ApiOperation(value = "钓场船只预约-通过id查询", notes = "钓场船只预约-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FcFishOrder> queryById(@RequestParam(name = "id", required = true) String id) {
        FcFishOrder fcFishOrder = fcFishOrderService.getById(id);
        if (fcFishOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fcFishOrder);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fcFishOrder
     */
    @RequiresPermissions("order:fc_fish_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FcFishOrder fcFishOrder) {
        return super.exportXls(request, fcFishOrder, FcFishOrder.class, "钓场船只预约");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("order:fc_fish_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FcFishOrder.class);
    }

}
