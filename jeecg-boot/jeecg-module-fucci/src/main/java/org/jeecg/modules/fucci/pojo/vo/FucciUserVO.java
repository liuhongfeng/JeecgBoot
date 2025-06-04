package org.jeecg.modules.fucci.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 福羲用户信息
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FucciUserVO {

    /**
     * 用户ID
     * 对应 sys_user.username（登录账号）
     */
    private String userId;

    /**
     * 昵称
     * 对应 sys_user.realname（真实姓名）
     */
    private String nickname;

    /**
     * 头像
     * 对应 sys_user.avatar（头像）
     */
    private String avatarUrl;

    /**
     * 是否 VIP（true:是，false:否）
     */
    private boolean vip;

    /**
     * VIP 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date vipStartTime;

    /**
     * VIP 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date vipEndTime;

    /**
     * 会员次数
     */
    private Integer vipCount;

    /**
     * 是否工作人员（true:是，false:否）
     */
    private boolean staff;

}
