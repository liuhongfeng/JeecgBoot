package org.jeecg.modules.admin.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 钓场船只预约
 * @Author: jeecg-boot
 * @Date: 2025-04-20
 * @Version: V1.0
 */
@Data
@TableName("fc_fish_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "fc_fish_order对象", description = "钓场船只预约")
public class FcFishOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 预约用户ID
     */
    @Excel(name = "预约用户ID", width = 15)
    @ApiModelProperty(value = "预约用户ID")
    private String userId;

    /**
     * 预约用户昵称
     */
    @Excel(name = "预约用户昵称", width = 15)
    @ApiModelProperty(value = "预约用户昵称")
    private String realname;

    /**
     * 钓场ID
     */
    @Excel(name = "钓场ID", width = 15)
    @ApiModelProperty(value = "钓场ID")
    private String groundId;

    /**
     * 钓场名称
     */
    @Excel(name = "钓场名称", width = 15)
    @ApiModelProperty(value = "钓场名称")
    private String groundName;

    /**
     * 预约日期
     */
    @Excel(name = "预约日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date date;

    /**
     * 钓场船只ID
     */
    @Excel(name = "钓场船只ID", width = 15)
    @ApiModelProperty(value = "钓场船只ID")
    private String boatId;

    /**
     * 船号
     */
    @Excel(name = "船号", width = 15)
    @ApiModelProperty(value = "船号")
    private String boatNumber;

    /**
     * 预约用户姓名
     */
    @Excel(name = "预约用户姓名", width = 15)
    @ApiModelProperty(value = "预约用户姓名")
    private String name;

    /**
     * 预约用户手机号
     */
    @Excel(name = "预约用户手机号", width = 15)
    @ApiModelProperty(value = "预约用户手机号")
    private String phone;

    /**
     * 预约票价
     */
    @Excel(name = "预约票价", width = 15)
    @ApiModelProperty(value = "预约票价")
    private BigDecimal fare;

    /**
     * 预约状态
     */
    @Excel(name = "预约状态", width = 15, dicCode = "fish_order_status")
    @Dict(dicCode = "fish_order_status")
    @ApiModelProperty(value = "预约状态")
    private String status;

    /**
     * 删除状态（0:正常，1:删除）
     */
    @Excel(name = "删除状态（0:正常，1:删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0:正常，1:删除）")
    @TableLogic
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

}
