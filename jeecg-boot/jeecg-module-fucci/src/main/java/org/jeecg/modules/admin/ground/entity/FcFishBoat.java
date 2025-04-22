package org.jeecg.modules.admin.ground.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 钓场船只
 * @Author: jeecg-boot
 * @Date: 2025-04-14
 * @Version: V1.0
 */
@ApiModel(value = "fc_fish_boat对象", description = "钓场船只")
@Data
@TableName("fc_fish_boat")
public class FcFishBoat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 船号
     */
    @Excel(name = "船号", width = 15)
    @ApiModelProperty(value = "船号")
    private String boatNumber;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "fish_boat_status")
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 载客量
     */
    @Excel(name = "载客量", width = 15)
    @ApiModelProperty(value = "载客量")
    private Integer capacity;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 钓场ID
     */
    @ApiModelProperty(value = "钓场ID")
    private String groundId;
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
}
