package org.jeecg.modules.admin.staff.controller;

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
import org.jeecg.modules.admin.staff.entity.FcFishStaff;
import org.jeecg.modules.admin.staff.service.IFcFishStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 员工信息
 * @Author: jeecg-boot
 * @Date: 2025-04-26
 * @Version: V1.0
 */
@Api(tags = "员工信息")
@RestController
@RequestMapping("/staff/fcFishStaff")
@Slf4j
public class FcFishStaffController extends JeecgController<FcFishStaff, IFcFishStaffService> {
    @Autowired
    private IFcFishStaffService fcFishStaffService;

    /**
     * 分页列表查询
     *
     * @param fcFishStaff
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "员工信息-分页列表查询")
    @ApiOperation(value = "员工信息-分页列表查询", notes = "员工信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FcFishStaff>> queryPageList(FcFishStaff fcFishStaff,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<FcFishStaff> queryWrapper = QueryGenerator.initQueryWrapper(fcFishStaff, req.getParameterMap());
        Page<FcFishStaff> page = new Page<FcFishStaff>(pageNo, pageSize);
        IPage<FcFishStaff> pageList = fcFishStaffService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param fcFishStaff
     * @return
     */
    @AutoLog(value = "员工信息-添加")
    @ApiOperation(value = "员工信息-添加", notes = "员工信息-添加")
    @RequiresPermissions("staff:fc_fish_staff:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FcFishStaff fcFishStaff) {
        fcFishStaffService.save(fcFishStaff);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fcFishStaff
     * @return
     */
    @AutoLog(value = "员工信息-编辑")
    @ApiOperation(value = "员工信息-编辑", notes = "员工信息-编辑")
    @RequiresPermissions("staff:fc_fish_staff:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FcFishStaff fcFishStaff) {
        fcFishStaffService.updateById(fcFishStaff);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "员工信息-通过id删除")
    @ApiOperation(value = "员工信息-通过id删除", notes = "员工信息-通过id删除")
    @RequiresPermissions("staff:fc_fish_staff:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fcFishStaffService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "员工信息-批量删除")
    @ApiOperation(value = "员工信息-批量删除", notes = "员工信息-批量删除")
    @RequiresPermissions("staff:fc_fish_staff:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fcFishStaffService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "员工信息-通过id查询")
    @ApiOperation(value = "员工信息-通过id查询", notes = "员工信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FcFishStaff> queryById(@RequestParam(name = "id", required = true) String id) {
        FcFishStaff fcFishStaff = fcFishStaffService.getById(id);
        if (fcFishStaff == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fcFishStaff);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fcFishStaff
     */
    @RequiresPermissions("staff:fc_fish_staff:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FcFishStaff fcFishStaff) {
        return super.exportXls(request, fcFishStaff, FcFishStaff.class, "员工信息");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("staff:fc_fish_staff:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FcFishStaff.class);
    }

}
