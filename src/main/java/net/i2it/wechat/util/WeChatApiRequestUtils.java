package net.i2it.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.i2it.wechat.constant.WeChatApiUrlConsts;
import net.i2it.wechat.util.common.HttpUtils;

import java.io.IOException;

/**
 * 微信各服务功能接口的实现，方法的返回类型主要为 com.alibaba.fastjson.JSONObject
 *
 * @author liuming
 * @date 2017/11/15 10:40
 */
public class WeChatApiRequestUtils {

    /*--------微信公众号--------*/

    /**
     * 请求获取应用access_token信息<br>
     * 正常获取时，JSON数据包为：{"access_token":"ACCESS_TOKEN","expires_in":7200}<br>
     * 获取出错时，JSON数据包为：{"errcode":40013,"errmsg":"invalid appid"}<br>
     * 具体返回参数解释和微信公众号开发文档中的内容一致：https://mp.weixin.qq.com/wiki
     *
     * @param appId     公众号的唯一标识
     * @param appSecret 公众号的appsecret
     * @return
     * @throws IOException
     */
    public static JSONObject getAppAccessToken(String appId, String appSecret) throws IOException {
        String url = WeChatApiUrlConsts.APP_ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
        String response = HttpUtils.doGet(url);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject;
    }

    /**
     * 创建微信公众号菜单<br>
     * 正常获取时，JSON数据包为：{"errcode":0,"errmsg":"ok"}<br>
     * 获取出错时，JSON数据包为：{"errcode":40018,"errmsg":"invalid button name size"}
     *
     * @param menuStr json格式的菜单创建请求参数字符串
     * @return
     * @throws IOException
     */
    public static JSONObject createMenu(String appAccessToken, String menuStr) throws IOException {
        String url = WeChatApiUrlConsts.MENU_CREATE_URL.replace("ACCESS_TOKEN", appAccessToken);
        String response = HttpUtils.doPost(url, menuStr);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject;
    }

    /*--------微信网页开发--------*/

    /**
     * 微信网页开发：获取在微信网页中使用的access_token<br>
     * scope为snsapi_base发起的网页授权，是用来获取进入页面的用户的openid以及网页access_token的，access_token可被进一步用来获取用户的基本信息<br>
     * 正常获取时，JSON数据包为：{"access_token":"ACCESS_TOKEN","expires_in":7200,"refresh_token":"REFRESH_TOKEN","openid":"OPENID","scope":"SCOPE"}<br>
     * 获取出错时，JSON数据包为：{"errcode":40029,"errmsg":"invalid code"}
     * 具体返回参数解释和微信公众号开发文档中的内容一致：https://mp.weixin.qq.com/wiki
     *
     * @param appId     公众号的唯一标识
     * @param appSecret 公众号的appsecret
     * @param code      以scope为snsapi_base方式获取access_token过程中第一步获取的code参数
     * @return
     * @throws IOException
     */
    public static JSONObject getWebAccessToken(String appId, String appSecret, String code) throws IOException {
        String url = WeChatApiUrlConsts.WEB_ACCESS_TOKEN_URL.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        String response = HttpUtils.doGet(url);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject;
    }

    /**
     * 微信网页开发：使用js-sdk进行配置的时候，生成需要的签名需要使用jsapi_ticket，jsapi_ticket是公众号用于调用微信JS接口的临时票据<br>
     * 正常获取时，JSON数据包为：{"errcode":0,"errmsg":"ok","ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA","expires_in":7200}<br>
     * 具体返回参数解释和微信公众号开发文档中的内容一致：https://mp.weixin.qq.com/wiki
     *
     * @param appAccessToken 公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用
     * @return
     * @throws IOException
     */
    public static JSONObject getJsApiTicket(String appAccessToken) throws IOException {
        String url = WeChatApiUrlConsts.WEB_JSAPI_TICKET_URL.replace("ACCESS_TOKEN", appAccessToken);
        String response = HttpUtils.doGet(url);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject;
    }

    /*--------微信支付-------*/

    /**
     * 微信支付：获取微信支付统一下单的结果
     *
     * @param unifiedOrderXmlStr 包含统一下单接口所需参数的xml格式的字符串
     * @return xml格式的字符串返回值
     * @throws IOException
     */
    public static String getUnifiedOrderResult(String unifiedOrderXmlStr) throws IOException {
        String response = HttpUtils.doPost(WeChatApiUrlConsts.PAY_UNIFIED_ORDER_URL, unifiedOrderXmlStr);
        return response;
    }

}
