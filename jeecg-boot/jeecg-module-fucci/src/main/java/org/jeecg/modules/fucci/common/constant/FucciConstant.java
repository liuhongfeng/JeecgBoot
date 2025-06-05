package org.jeecg.modules.fucci.common.constant;

/**
 * 福羲项目-常量
 */
public interface FucciConstant {

    /**
     * 系统静态资源路径
     */
    String STATIC_PATH = "/sys/common/static/";

    /**
     * 支付回调通知 URI
     */
    String ORDER_NOTIFY_URI = "/fucci/order/pay/notify/success";

    /**
     * 退款回调通知 URI
     */
    String REFUND_NOTIFY_URI = "/fucci/order/pay/notify/refund";

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
         * 用户头像地址 URI
         */
        String AVATAR_URI = "/sys/common/static/temp/WechatIMG1881_1743908875667.jpg";

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
