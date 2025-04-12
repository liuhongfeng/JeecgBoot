package org.jeecg.modules.fucci.pojo.dto;

import lombok.Data;

/**
 * 福羲用户信息 DTO
 */
@Data
public class FucciUserDTO {

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

}
