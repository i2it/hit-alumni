package site.liuming.wechat.common;

import site.liuming.wechat.common.util.EncryptionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号和微信支付开发中一些参数生成器
 *
 * @author liuming
 * @date 2017/11/15 21:24
 */
public class Generator {

    /**
     * 签名算法 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     *
     * @param object    自定义的封装sign生成参数的对象，变量名需要与开发文档中的保持一致，且均需要定义为<code>String</code>
     * @param apiSecret 微信支付中的key
     * @return
     */
    public static String generateSign(Object object, String apiSecret) throws NoSuchAlgorithmException, UnsupportedEncodingException, IllegalAccessException {
        // 用于存放获得的对象属性名以及对应的属性值
        Map<String, String> params = new HashMap<String, String>();
        // 获取对象中属性值不为null的属性及属性值
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue instanceof String) {
                params.put(field.getName(), (String) fieldValue);
            }
        }
        // 对属性名进行排序（字典序）
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 对属性名和属性值按照字典序拼接，形如：key1=value1&key2=value2
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.append("key=").append(apiSecret);
        return EncryptionUtils.encrypt(sb.toString().replace("packageStr", "package"),
                EncryptionUtils.EncryptionTypeEnum.MD5.getValue()).toUpperCase();
    }

    /**
     * 生成由纯数字组成的、指定长度的随机字符串
     *
     * @param length 指定要生成的字符串长度
     * @return 由纯数字组成的、指定长度的随机字符串
     */
    public static String generateRandomStr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

}