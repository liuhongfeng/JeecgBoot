package org.jeecg.modules.fucci.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 福羲用户登录信息 DTO
 */
@Data
public class FucciUserLoginDTO {

    /**
     * 微信小程序登录 code
     */
    @NotBlank(message = "wxCode 不能为空")
    private String wxCode;

}
