package org.jeecg.modules.fucci.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lhf
 * @date 2025-04-17
 * @describe
 */
@Data
@Component
@ConfigurationProperties(prefix = "jsgh.fucci")
public class FucciProperties {

    String path;

}
