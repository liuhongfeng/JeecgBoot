package org.jeecg.modules.fucci.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lhf
 * @date 2025-04-15
 * @describe
 */
@Data
public class FucciGroundDetailsVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 钓场名称
     */
    @ApiModelProperty(value = "钓场名称")
    private String name;

    /**
     * 钓场详情图
     */
    @ApiModelProperty(value = "钓场详情图")
    private String detailsImage;

    /**
     * 钓场地址
     */
    @ApiModelProperty(value = "钓场地址")
    private String address;

    /**
     * 营业开始时间
     */
    @ApiModelProperty(value = "营业开始时间")
    private String startTime;

    /**
     * 营业结束时间
     */
    @ApiModelProperty(value = "营业结束时间")
    private String endTime;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 场地设施
     */
    @ApiModelProperty(value = "场地设施")
    private String facilities;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     * VIP 价格
     */
    @ApiModelProperty(value = "VIP 价格")
    private BigDecimal vipPrice;

    /**
     * 是否 VIP（true:是，false:否）
     */
    @ApiModelProperty(value = "是否 VIP（true:是，false:否）")
    private boolean vip;

}
