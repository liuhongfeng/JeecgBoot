package org.jeecg.modules.fucci.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lhf
 * @date 2025-04-15
 * @describe
 */
@Data
public class FucciGroundVO {

    /**
     * id
     */
    private String id;

    /**
     * 钓场名称
     */
    private String name;

    /**
     * 钓场首页图
     */
    private String homeImage;

    /**
     * 钓场地址
     */
    private String address;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 月预约数量
     */
    private Integer monthlyReservation;

}
