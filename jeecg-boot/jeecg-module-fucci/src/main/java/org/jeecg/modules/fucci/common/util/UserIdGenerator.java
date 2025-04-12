package org.jeecg.modules.fucci.common.util;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * 用户ID 生成工具
 */
public class UserIdGenerator {

    private static final String BASE36 = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String FXID = "fxid_";
    private static final SecureRandom random = new SecureRandom();

    /**
     * 随机生成指定长度的 Base36 字符串（小写字母 + 数字）
     */
    private static String randomBase36(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE36.charAt(random.nextInt(BASE36.length())));
        }
        return sb.toString();
    }

    /**
     * 生成 fxid 开头的 14 位编码
     */
    public static String generateUserId() {
        // 获取当前时间戳（毫秒）转为 Base36（避免重复）
        long millis = Instant.now().toEpochMilli();
        // base36 编码
        String timePart = Long.toString(millis, 36);
        // 补齐剩余位数为随机串
        int remainingLength = 14 - timePart.length();
        if (remainingLength < 0) {
            // 极少数情况超长，截断
            timePart = timePart.substring(0, 14);
            remainingLength = 0;
        }
        String randomPart = randomBase36(remainingLength);
        return FXID + (timePart + randomPart);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(generateUserId());
        }
    }

}
