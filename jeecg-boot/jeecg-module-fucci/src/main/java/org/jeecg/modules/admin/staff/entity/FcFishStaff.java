package org.jeecg.modules.admin.staff.entity;

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
 * @Description: 员工信息
 * @Author: jeecg-boot
 * @Date: 2025-04-26
 * @Version: V1.0
 */
@Data
@TableName("fc_fish_staff")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "fc_fish_staff对象", description = "员工信息")
public class FcFishStaff implements Serializable {

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
     * 员工姓名
     */
    @Excel(name = "员工姓名", width = 15)
    @ApiModelProperty(value = "员工姓名")
    private String name;

    /**
     * 员工手机号
     */
    @Excel(name = "员工手机号", width = 15)
    @ApiModelProperty(value = "员工手机号")
    private String phone;

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
