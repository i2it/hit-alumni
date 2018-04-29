package site.liuming.hitef.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信公众号App开发的一些配置变量，从配置文件中通过 @Value 注解完成加载
 *
 * @Author liuming
 * @Date 2017/11/15 9:12
 */
@Component
public class AppProperty {
    // 正式公众号的配置信息
    @Value("${wechat.hitef.app-id}")
    private String appId;
    @Value("${wechat.hitef.app-secret}")
    private String appSecret;
    @Value("${wechat.hitef.token}")
    private String token;

    // 微信支付的配置信息
    @Value("${wechat.hitef.mch-id}")
    private String mchId;
    @Value("${wechat.hitef.api-secret}")
    private String apiSecret;

    // 微信网页JS-SDK配置
    @Value("${wechat.hitef.js-sdk-debug}")
    private boolean jsSdkDebug;
    @Value("${wechat.hitef.js-api-list}")
    private String jsApiListStr;

    // 服务器域名
    @Value("${wechat.hitef.server-domain-url}")
    private String serverDomainUrl;

    @Value("${server.context-path}")
    private String contextPath;

    // 支付-统一下单处理对应的url
    @Value("${wechat.hitef.pay-url}")
    private String payUrl;
    // 支付结果通知url
    @Value("${wechat.hitef.notify-url}")
    private String notifyUrl;

    @Value("${wechat.hitef.certification-path}")
    private String certificationPath;

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getToken() {
        return token;
    }

    public String getMchId() {
        return mchId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public boolean isJsSdkDebug() {
        return jsSdkDebug;
    }

    public String getJsApiListStr() {
        return jsApiListStr;
    }

    public String getServerDomainUrl() {
        return serverDomainUrl;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getCertificationPath() {
        return certificationPath;
    }
}
