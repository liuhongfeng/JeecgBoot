package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lhf
 * @date 2025-05-05
 * @describe
 */
@Data
public class FucciGroundStaffOrderDateVO {

    @ApiModelProperty(value = "预约id")
    private String id;

    @ApiModelProperty(value = "预约用户ID")
    @JsonIgnore
    private String userId;

    @ApiModelProperty(value = "预约用户昵称")
    private String realname;

    @ApiModelProperty(value = "钓场ID")
    @JsonIgnore
    private String groundId;

    @ApiModelProperty(value = "钓场名称")
    private String groundName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date date;

    @ApiModelProperty(value = "用户预约的船只ID")
    @JsonIgnore
    private String boatId;

    @ApiModelProperty(value = "用户预约的船号")
    private String boatNumber;

    @ApiModelProperty(value = "用户预约时填写的联系电话")
    private String phone;

}
