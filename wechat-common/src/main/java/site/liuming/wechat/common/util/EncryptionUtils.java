package site.liuming.wechat.common.util;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密算法的实现工具类
 *
 * @author liuming
 * @date 2017/11/15 15:26
 */
public class EncryptionUtils {

    /**
     * 通用的加密过程
     *
     * @param msg            需要加密的消息
     * @param encryptionType {@link EncryptionTypeEnum} 支持的加密算法
     * @return 加密后的信息
     */
    public static String encrypt(String msg, String encryptionType) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance(encryptionType);
        digest.update(msg.getBytes("UTF-8"));
        return Hex.encodeHexString(digest.digest());
    }


    /**
     * 微信公众号开发中涉及到的加密算法枚举类
     */
    public enum EncryptionTypeEnum {

        MD5("MD5"),
        SHA1("SHA"),;

        private String value;

        EncryptionTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

}
