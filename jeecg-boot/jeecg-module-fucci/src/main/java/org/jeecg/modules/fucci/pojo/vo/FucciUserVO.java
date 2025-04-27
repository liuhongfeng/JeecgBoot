package org.jeecg.modules.fucci.pojo.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 福羲用户信息
 */
@Data
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
    private LocalDate vipStartTime;

    /**
     * VIP 结束时间
     */
    private LocalDate vipEndTime;

    /**
     * 是否工作人员（true:是，false:否）
     */
    private boolean staff;

}
