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
public class FucciOrderPayVO {

    @ApiModelProperty(value = "预约订单id")
    private String orderId;

    @ApiModelProperty(value = "小程序AppID")
    @JsonIgnore
    private String appId;

    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;

    @ApiModelProperty(value = "预支付交易会话标识")
    private String packageValue;

    @ApiModelProperty(value = "签名值")
    private String paySign;

    @ApiModelProperty(value = "签名类型")
    private String signType;

    @ApiModelProperty(value = "时间戳")
    private String timeStamp;

}
