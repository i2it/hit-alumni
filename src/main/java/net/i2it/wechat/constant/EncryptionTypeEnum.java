package net.i2it.wechat.constant;

/**
 * 目前支持实现的加密算法枚举类
 *
 * @author liuming
 * @date 2017/11/15 15:21
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
