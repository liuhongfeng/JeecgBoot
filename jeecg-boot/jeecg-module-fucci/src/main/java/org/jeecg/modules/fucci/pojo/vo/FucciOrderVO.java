package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lhf
 * @date 2025-04-27
 * @describe
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FucciOrderVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "钓场ID")
    private String groundId;

    @ApiModelProperty(value = "钓场名称")
    private String groundName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date date;

    @ApiModelProperty(value = "船号")
    private String boatNumber;

    @ApiModelProperty(value = "预约填写的姓名")
    private String orderName;

    @ApiModelProperty(value = "预约填写的联系电话")
    private String orderPhone;

    @ApiModelProperty(value = "预约时的票价")
    private String orderFare;

    @ApiModelProperty(value = "预约状态")
    private String status;

    @ApiModelProperty(value = "是否预约修改（true:是，false:否）")
    private Boolean orderModify;

    @ApiModelProperty(value = "预约修改次数")
    private Integer orderModifyCount;

    @ApiModelProperty(value = "钓场联系电话")
    private String phone;

    @ApiModelProperty(value = "钓场地址")
    private String address;

    @ApiModelProperty(value = "钓场位置纬度")
    private String latitude;

    @ApiModelProperty(value = "钓场位置经度")
    private String longitude;

}
