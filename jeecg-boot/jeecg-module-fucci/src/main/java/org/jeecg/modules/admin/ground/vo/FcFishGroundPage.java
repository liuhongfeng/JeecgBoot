package org.jeecg.modules.admin.ground.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.admin.ground.entity.FcFishBoat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 钓场信息
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
@Data
@ApiModel(value = "fc_fish_groundPage对象", description = "钓场信息")
public class FcFishGroundPage {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 钓场名称
     */
    @Excel(name = "钓场名称", width = 15)
    @ApiModelProperty(value = "钓场名称")
    private String name;
    /**
     * 钓场首页图
     */
    @Excel(name = "钓场首页图", width = 15)
    @ApiModelProperty(value = "钓场首页图")
    private String homeImage;
    /**
     * 钓场详情图
     */
    @Excel(name = "钓场详情图", width = 15)
    @ApiModelProperty(value = "钓场详情图")
    private String detailsImage;
    /**
     * 钓场介绍
     */
    @Excel(name = "钓场介绍", width = 15)
    @ApiModelProperty(value = "钓场介绍")
    private String introduce;
    /**
     * 钓场地址
     */
    @Excel(name = "钓场地址", width = 15)
    @ApiModelProperty(value = "钓场地址")
    private String address;
    /**
     * 钓场位置纬度
     */
    @Excel(name = "钓场位置纬度", width = 15)
    @ApiModelProperty(value = "钓场位置纬度")
    private String latitude;
    /**
     * 钓场位置经度
     */
    @Excel(name = "钓场位置经度", width = 15)
    @ApiModelProperty(value = "钓场位置经度")
    private String longitude;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private String phone;
    /**
     * 钓位数量
     */
    @Excel(name = "钓位数量", width = 15)
    @ApiModelProperty(value = "钓位数量")
    private Integer positionQuantity;
    /**
     * 营业开始时间
     */
    @Excel(name = "营业开始时间", width = 15)
    @ApiModelProperty(value = "营业开始时间")
    private String startTime;
    /**
     * 营业结束时间
     */
    @Excel(name = "营业结束时间", width = 15)
    @ApiModelProperty(value = "营业结束时间")
    private String endTime;
    /**
     * 价格
     */
    @Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * VIP 价格
     */
    @Excel(name = "VIP 价格", width = 15)
    @ApiModelProperty(value = "VIP 价格")
    private BigDecimal vipPrice;
    /**
     * 价格说明
     */
    @Excel(name = "价格说明", width = 15)
    @ApiModelProperty(value = "价格说明")
    private String priceDesc;
    /**
     * 场地设施
     */
    @Excel(name = "场地设施", width = 15, dicCode = "facilities")
    @Dict(dicCode = "facilities")
    @ApiModelProperty(value = "场地设施")
    private String facilities;
    /**
     * 船只总数
     */
    @Excel(name = "船只总数", width = 15)
    @ApiModelProperty(value = "船只总数")
    private Integer boatQuantity;
    /**
     * 钓场状态
     */
    @Excel(name = "钓场状态", width = 15, dicCode = "fish_ground_status")
    @Dict(dicCode = "fish_ground_status")
    @ApiModelProperty(value = "钓场状态")
    private String status;
    /**
     * 删除状态（0:正常，1:删除）
     */
    @Excel(name = "删除状态（0:正常，1:删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0:正常，1:删除）")
    private Integer delFlag;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ExcelCollection(name = "钓场船只")
    @ApiModelProperty(value = "钓场船只")
    private List<FcFishBoat> fcFishBoatList;

}
