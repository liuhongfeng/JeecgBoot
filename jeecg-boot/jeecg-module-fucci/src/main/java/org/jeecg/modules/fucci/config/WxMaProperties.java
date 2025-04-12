package org.jeecg.modules.fucci.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private List<Config> configs;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的 appid
         */
        private String appid;

        /**
         * 设置微信小程序的 Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的 token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的 EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML 或者 JSON
         */
        private String msgDataFormat;

    }

}