package vip.xiaozhao.intern.baseUtil.intf.utils;

/**
 * @author hlxllo
 * @description 发送消息工具类
 * @date 2025/2/7
 */
public class MessageUtil {
    public static int getLevel(long timeGapMinutes) {
        // 根据分钟差来判断等级
        int level;
        if (timeGapMinutes < 60) {
            level = 1;
        } else if (timeGapMinutes < 1440) {
            level = 2;
        } else if (timeGapMinutes <= 4320) {
            level = 3;
        } else if (timeGapMinutes <= 10080) {
            level = 4;
        } else {
            level = 5;
        }
        return level;
    }
}
