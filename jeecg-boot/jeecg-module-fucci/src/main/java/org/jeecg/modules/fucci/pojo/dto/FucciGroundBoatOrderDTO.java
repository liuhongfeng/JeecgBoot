package org.jeecg.modules.fucci.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = "手机号格式不正确")
    private String phone;

}
