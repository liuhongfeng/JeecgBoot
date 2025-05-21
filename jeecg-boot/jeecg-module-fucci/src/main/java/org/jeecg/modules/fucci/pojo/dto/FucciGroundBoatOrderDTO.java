package org.jeecg.modules.fucci.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lhf
 * @date 2025-04-22
 * @describe
 */
@Data
public class FucciGroundBoatOrderDTO {

    /**
     * 预约日期
     */
    @ApiModelProperty(value = "预约日期")
    @NotBlank(message = "预约日期不能为空")
    private String date;

    /**
     * 预约船只 id
     */
    @ApiModelProperty(value = "预约船只 id")
    @NotBlank(message = "预约船只 id 不能为空")
    private String boatId;

    /**
     * 预约票价
     */
    @ApiModelProperty(value = "预约票价")
    @NotNull(message = "预约票价不能为空")
    private BigDecimal fare;

    /**
     * 预约用户姓名
     */
    @ApiModelProperty(value = "预约用户姓名")
    @NotBlank(message = "预约用户姓名不能为空")
    private String name;

    /**
     * 预约用户手机号
     */
    @ApiModelProperty(value = "预约用户手机号")
    @NotBlank(message = "预约用户手机号不能为空")
    private String phone;

}
