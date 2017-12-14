package net.i2it.wechat.constant;

/**
 * 微信公众号开发API接口URL
 *
 * @Author liuming
 * @Date 2017/11/15 10:04
 */
public class WeChatApiUrlConsts {

    /**
     * 全局access_token请求接口
     */
    public final static String APP_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 微信菜单：创建微信菜单的接口
     */
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";


    /**
     * 微信网页开发：在微信网页中获取用于置换access_token（在网页中使用）的code
     */
    public final static String WEB_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 微信网页开发：获取在微信网页中使用的access_token
     */
    public final static String WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 微信网页开发：使用js-sdk进行配置的时候，需要的签名生成需要使用jsapi_ticket，这为jsapi_ticket的请求接口
     */
    public final static String WEB_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";


    /**
     * 微信支付：统一下单接口，返回结果中最为重要的是prepay_id
     */
    public final static String PAY_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

}
