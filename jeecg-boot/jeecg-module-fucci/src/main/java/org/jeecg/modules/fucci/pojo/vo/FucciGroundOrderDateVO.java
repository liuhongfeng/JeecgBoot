package org.jeecg.modules.fucci.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhf
 * @date 2025-04-20
 * @describe
 */
@Data
public class FucciGroundOrderDateVO {

    /**
     * 预约日期
     */
    @ApiModelProperty(value = "预约日期")
    private String orderDate;

    /**
     * 该日期下预约的船只数
     */
    @ApiModelProperty(value = "该日期下预约的船只数")
    private Integer orderQuantity;

}
