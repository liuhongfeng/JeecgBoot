package org.jeecg.modules.fucci.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhf
 * @date 2025-04-20
 * @describe
 */
@Data
public class FucciGroundOrderVO {

    /**
     * 预约 id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 钓场名称
     */
    @ApiModelProperty(value = "钓场名称")
    private String name;

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

    /**
     * 船只总数
     */
    @ApiModelProperty(value = "船只总数")
    private Integer boatQuantity;

    /**
     * 查询钓场近 30 天已预约的船只数据
     */
    @ApiModelProperty(value = "钓场近 30 天已预约的船只数据")
    private List<FucciGroundOrderDateVO> groundOrderDateList;

    /**
     * 根据日期查询钓场所有船只的预约情况
     */
    @ApiModelProperty(value = "某日期下钓场所有船只的预约数据")
    private List<FucciGroundBoatOrderVO> groundBoatOrderList;

}
