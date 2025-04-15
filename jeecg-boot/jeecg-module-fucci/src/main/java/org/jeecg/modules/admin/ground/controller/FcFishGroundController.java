package org.jeecg.modules.admin.ground.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecg.modules.admin.ground.entity.FcFishGround;
import org.jeecg.modules.admin.ground.service.IFcFishBoatService;
import org.jeecg.modules.admin.ground.service.IFcFishGroundService;
import org.jeecg.modules.admin.ground.vo.FcFishGroundPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 钓场信息
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
@Api(tags = "钓场信息")
@RestController
@RequestMapping("/ground/fcFishGround")
@Slf4j
public class FcFishGroundController {
    @Autowired
    private IFcFishGroundService fcFishGroundService;
    @Autowired
    private IFcFishBoatService fcFishBoatService;

    /**
     * 分页列表查询
     *
     * @param fcFishGround
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "钓场信息-分页列表查询")
    @ApiOperation(value = "钓场信息-分页列表查询", notes = "钓场信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FcFishGround>> queryPageList(FcFishGround fcFishGround,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<FcFishGround> queryWrapper = QueryGenerator.initQueryWrapper(fcFishGround, req.getParameterMap());
        Page<FcFishGround> page = new Page<FcFishGround>(pageNo, pageSize);
        IPage<FcFishGround> pageList = fcFishGroundService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param fcFishGroundPage
     * @return
     */
    @AutoLog(value = "钓场信息-添加")
    @ApiOperation(value = "钓场信息-添加", notes = "钓场信息-添加")
    @RequiresPermissions("ground:fc_fish_ground:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FcFishGroundPage fcFishGroundPage) {
        FcFishGround fcFishGround = new FcFishGround();
        BeanUtils.copyProperties(fcFishGroundPage, fcFishGround);
        fcFishGroundService.saveMain(fcFishGround, fcFishGroundPage.getFcFishBoatList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fcFishGroundPage
     * @return
     */
    @AutoLog(value = "钓场信息-编辑")
    @ApiOperation(value = "钓场信息-编辑", notes = "钓场信息-编辑")
    @RequiresPermissions("ground:fc_fish_ground:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FcFishGroundPage fcFishGroundPage) {
        FcFishGround fcFishGround = new FcFishGround();
        BeanUtils.copyProperties(fcFishGroundPage, fcFishGround);
        FcFishGround fcFishGroundEntity = fcFishGroundService.getById(fcFishGround.getId());
        if (fcFishGroundEntity == null) {
            return Result.error("未找到对应数据");
        }
        fcFishGroundService.updateMain(fcFishGround, fcFishGroundPage.getFcFishBoatList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "钓场信息-通过id删除")
    @ApiOperation(value = "钓场信息-通过id删除", notes = "钓场信息-通过id删除")
    @RequiresPermissions("ground:fc_fish_ground:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fcFishGroundService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "钓场信息-批量删除")
    @ApiOperation(value = "钓场信息-批量删除", notes = "钓场信息-批量删除")
    @RequiresPermissions("ground:fc_fish_ground:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fcFishGroundService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "钓场信息-通过id查询")
    @ApiOperation(value = "钓场信息-通过id查询", notes = "钓场信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FcFishGround> queryById(@RequestParam(name = "id", required = true) String id) {
        FcFishGround fcFishGround = fcFishGroundService.getById(id);
        if (fcFishGround == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fcFishGround);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "钓场船只通过主表ID查询")
    @ApiOperation(value = "钓场船只主表ID查询", notes = "钓场船只-通主表ID查询")
    @GetMapping(value = "/queryFcFishBoatByMainId")
    public Result<List<FcFishBoat>> queryFcFishBoatListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<FcFishBoat> fcFishBoatList = fcFishBoatService.selectByMainId(id);
        return Result.OK(fcFishBoatList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fcFishGround
     */
    @RequiresPermissions("ground:fc_fish_ground:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FcFishGround fcFishGround) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<FcFishGround> queryWrapper = QueryGenerator.initQueryWrapper(fcFishGround, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        // Step.2 获取导出数据
        List<FcFishGround> fcFishGroundList = fcFishGroundService.list(queryWrapper);

        // Step.3 组装pageList
        List<FcFishGroundPage> pageList = new ArrayList<FcFishGroundPage>();
        for (FcFishGround main : fcFishGroundList) {
            FcFishGroundPage vo = new FcFishGroundPage();
            BeanUtils.copyProperties(main, vo);
            List<FcFishBoat> fcFishBoatList = fcFishBoatService.selectByMainId(main.getId());
            vo.setFcFishBoatList(fcFishBoatList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "钓场信息列表");
        mv.addObject(NormalExcelConstants.CLASS, FcFishGroundPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("钓场信息数据", "导出人:" + sysUser.getRealname(), "钓场信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("ground:fc_fish_ground:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<FcFishGroundPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FcFishGroundPage.class, params);
                for (FcFishGroundPage page : list) {
                    FcFishGround po = new FcFishGround();
                    BeanUtils.copyProperties(page, po);
                    fcFishGroundService.saveMain(po, page.getFcFishBoatList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
