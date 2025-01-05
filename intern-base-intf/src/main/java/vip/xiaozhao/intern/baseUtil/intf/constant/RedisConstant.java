package vip.xiaozhao.intern.baseUtil.intf.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisConstant {

    public static String hostName;
    public static int port;
    public static String auth;
    public static String envName;

    public static final String PRE_USER_ID = "userId:";
    public static final String PRE_NOVEL_ID = "novelId:";
    public static final String HOT_NOVEL_LIST = "hotNovelList:";

    @Value("${redis.ip}")
    public  void setHostName(String hostName) {
        RedisConstant.hostName = hostName;
    }
    @Value("${redis.port}")
    public  void setPort(int port) {
        RedisConstant.port = port;
    }
    @Value("${redis.auth}")
    public  void setAuth(String auth) {
        RedisConstant.auth = auth;
    }
    @Value("${envName}")
    public  void setEnvName(String envName) {
        RedisConstant.envName = envName;
    }
}
