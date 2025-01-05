package vip.xiaozhao.intern.baseUtil.intf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

public class ConvertUtils {

    //将从Redis中获取的对象转换为指定类型的实体类
    public static <T> T convert2Object(Object object, Class<T> targetType) {
        // 将对象序列化为 JSON 字符串
        String jsonString = JSON.toJSONString(object);
        // 将 JSON 字符串反序列化为指定类型的实体类
        return JSON.parseObject(jsonString, targetType);
    }

    public static <T> List<T> convert2List(Object object, Class<T> clazz) {
        // 将对象序列化为 JSON 字符串
        String jsonString = JSON.toJSONString(object);
        // 使用 TypeReference 反序列化
        return JSON.parseArray(jsonString, clazz);
    }

}
