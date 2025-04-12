package org.jeecg.modules.fucci.common.constant;

/**
 * 福羲项目-常量
 */
public interface FucciConstant {

    interface User {
        /**
         * 真实姓名（用户昵称）
         */
        String REALNAME = "福羲用户";

        /**
         * 用户默认密码（明文密码）
         */
        String PASSWORD = "123456";

        /**
         * 用户头像地址
         */
        String AVATAR_URL = "https://admin.lureexpert.com/jeecgboot/sys/common/static/temp/WechatIMG1881_1743908875667.jpg";

        /**
         * 第三方类型（微信小程序）
         */
        String THIRD_TYPE = "mini";

        /**
         * 默认创建人
         */
        String CREATE_BY = "admin";
    }

}
