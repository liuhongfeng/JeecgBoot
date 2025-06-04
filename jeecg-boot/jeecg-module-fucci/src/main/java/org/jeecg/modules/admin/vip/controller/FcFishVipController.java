package org.jeecg.modules.admin.vip.controller;

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
import org.jeecg.modules.admin.vip.entity.FcFishVip;
import org.jeecg.modules.admin.vip.service.IFcFishVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 会员信息
 * @Author: jeecg-boot
 * @Date: 2025-04-26
 * @Version: V1.0
 */
@Api(tags = "会员信息")
@RestController
@RequestMapping("/vip/fcFishVip")
@Slf4j
public class FcFishVipController extends JeecgController<FcFishVip, IFcFishVipService> {
    @Autowired
    private IFcFishVipService fcFishVipService;

    /**
     * 分页列表查询
     *
     * @param fcFishVip
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "会员信息-分页列表查询")
    @ApiOperation(value = "会员信息-分页列表查询", notes = "会员信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FcFishVip>> queryPageList(FcFishVip fcFishVip,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<FcFishVip> queryWrapper = QueryGenerator.initQueryWrapper(fcFishVip, req.getParameterMap());
        Page<FcFishVip> page = new Page<FcFishVip>(pageNo, pageSize);
        IPage<FcFishVip> pageList = fcFishVipService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param fcFishVip
     * @return
     */
    @AutoLog(value = "会员信息-添加")
    @ApiOperation(value = "会员信息-添加", notes = "会员信息-添加")
    @RequiresPermissions("vip:fc_fish_vip:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FcFishVip fcFishVip) {
        fcFishVipService.save(fcFishVip);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fcFishVip
     * @return
     */
    @AutoLog(value = "会员信息-编辑")
    @ApiOperation(value = "会员信息-编辑", notes = "会员信息-编辑")
    @RequiresPermissions("vip:fc_fish_vip:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FcFishVip fcFishVip) {
        fcFishVipService.updateById(fcFishVip);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "会员信息-通过id删除")
    @ApiOperation(value = "会员信息-通过id删除", notes = "会员信息-通过id删除")
    @RequiresPermissions("vip:fc_fish_vip:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fcFishVipService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "会员信息-批量删除")
    @ApiOperation(value = "会员信息-批量删除", notes = "会员信息-批量删除")
    @RequiresPermissions("vip:fc_fish_vip:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fcFishVipService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "会员信息-通过id查询")
    @ApiOperation(value = "会员信息-通过id查询", notes = "会员信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FcFishVip> queryById(@RequestParam(name = "id", required = true) String id) {
        FcFishVip fcFishVip = fcFishVipService.getById(id);
        if (fcFishVip == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fcFishVip);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fcFishVip
     */
    @RequiresPermissions("vip:fc_fish_vip:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FcFishVip fcFishVip) {
        return super.exportXls(request, fcFishVip, FcFishVip.class, "会员信息");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("vip:fc_fish_vip:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FcFishVip.class);
    }

}
