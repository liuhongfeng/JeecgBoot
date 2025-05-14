package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhf
 * @date 2025-05-14
 * @describe
 */
@Data
public class FucciOrderPayResultVO {

    @ApiModelProperty(value = "小程序AppID")
    @JsonIgnore
    private String appId;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "交易状态")
    private String tradeState;

    @ApiModelProperty(value = "交易状态描述")
    private String tradeStateDesc;

    @ApiModelProperty(value = "支付完成时间")
    private String successTime;

}
