package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhf
 * @date 2025-04-20
 * @describe
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FucciGroundBoatOrderVO {

    /**
     * 船只 id
     */
    @ApiModelProperty(value = "boatId")
    private String boatId;

    /**
     * 船号
     */
    @ApiModelProperty(value = "船号")
    private String boatNumber;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @JsonIgnore
    private String status;

    /**
     * 载客量
     */
    @ApiModelProperty(value = "载客量")
    private Integer capacity;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @JsonIgnore
    private String remark;

    /**
     * 预约订单 id
     */
    @ApiModelProperty(value = "预约订单 id")
    @JsonIgnore
    private String orderId;

    /**
     * 预约日期
     */
    @ApiModelProperty(value = "预约日期")
    @JsonIgnore
    private String orderDate;

    /**
     * 是否预约
     */
    @ApiModelProperty(value = "是否预约")
    private boolean reserve;

}
