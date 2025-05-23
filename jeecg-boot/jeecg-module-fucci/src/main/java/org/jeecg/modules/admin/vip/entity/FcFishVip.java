package org.jeecg.modules.admin.vip.entity;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 会员信息
 * @Author: jeecg-boot
 * @Date: 2025-04-26
 * @Version: V1.0
 */
@Data
@TableName("fc_fish_vip")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "fc_fish_vip对象", description = "会员信息")
public class FcFishVip implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID", width = 15)
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 小程序用户ID
     */
    @Excel(name = "小程序用户ID", width = 15)
    @ApiModelProperty(value = "小程序用户ID")
    private String username;

    /**
     * 小程序用户昵称
     */
    @Excel(name = "小程序用户昵称", width = 15)
    @ApiModelProperty(value = "小程序用户昵称")
    private String realname;

    /**
     * 会员姓名
     */
    @Excel(name = "会员姓名", width = 15)
    @ApiModelProperty(value = "会员姓名")
    private String name;

    /**
     * 会员手机号
     */
    @Excel(name = "会员手机号", width = 15)
    @ApiModelProperty(value = "会员手机号")
    private String phone;

    /**
     * 会员开始时间
     */
    @Excel(name = "会员开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "会员开始时间")
    private Date startTime;

    /**
     * 会员结束时间
     */
    @Excel(name = "会员结束时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "会员结束时间")
    private Date endTime;

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
