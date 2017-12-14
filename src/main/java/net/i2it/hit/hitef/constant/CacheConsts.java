package net.i2it.hit.hitef.constant;

/**
 * 从微信服务器获得的具有一定时效的变量，在给定时间内不变<br>
 * 所以严格来说，这里的变量不是常量（只是特定时间内不变）
 */
public class CacheConsts {

    /**
     * 用于管理公众号菜单、用户等需要的access_token
     */
    public static String APP_ACCESS_TOKEN;

    /**
     * jsapi_ticket是公众号调用微信js接口的票据，用于生成微信js sdk的网页配置中的签名
     */
    public static String JS_API_TICKET;

}
