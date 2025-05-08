package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lhf
 * @date 2025-05-05
 * @describe
 */
@Data
public class FucciGroundStaffOrderVO {

    /**
     * 预约日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date orderDate;

    /**
     * 该日期下的预约数量
     */
    private Integer orderCount;

    /**
     * 该日期下的预约列表信息
     */
    @ApiModelProperty(value = "该日期下的预约列表信息")
    private List<FucciGroundStaffOrderDateVO> orderDateList;

}
