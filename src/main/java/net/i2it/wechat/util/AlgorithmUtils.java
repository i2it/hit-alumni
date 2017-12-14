package net.i2it.wechat.util;

import net.i2it.wechat.constant.EncryptionTypeEnum;
import net.i2it.wechat.util.common.EncryptionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号以及微信支付中的一些签名的计算算法实现
 *
 * @author liuming
 * @date 2017/11/15 21:24
 */
public class AlgorithmUtils {

    /**
     * <签名算法>
     * 微信支付中统一下单请求参数【签名】的计算<br>
     * 签名算法 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     *
     * @param object    需要参与签名计算的参数 封装对象
     * @param apiSecret 微信支付中的key
     * @return
     */
    public static String getSign(Object object, String apiSecret) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 用于存放获得的对象属性名以及对应的属性值
        Map<String, Object> params = new HashMap<String, Object>();
        // 获取对象中属性值不为null的属性及属性值
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue != null) {
                params.put(field.getName(), fieldValue);
            }
        }
        // 对属性名进行排序（字典序）
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        // 对属性名和属性值按照字典序拼接，形如：key1=value1&key2=value2
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < keys.length; i++) {
            sb.append(keys[i] + "=" + params.get(keys[i]) + "&");
        }
        sb.append("key=" + apiSecret);
        return EncryptionUtils.encrypt(sb.toString().replace("packageStr", "package"), EncryptionTypeEnum.MD5.getValue()).toUpperCase();
    }

    /**
     * <随机字符串生成>
     * 用于获取由数值组成且指定长度的随机字符串
     *
     * @param length 随机字符串长度
     * @return
     */
    public static String randomStr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
