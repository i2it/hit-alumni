package net.i2it.hit.hitef.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信公众号App开发的一些配置变量，从配置文件中通过 @Value 注解完成加载
 *
 * @Author liuming
 * @Date 2017/11/15 9:12
 */
@Component
public class AppConfigProperties {

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
    private boolean jsSdkDebuged;
    @Value("${wechat.hitef.js-api-list}")
    private String jsApiList;

    // 服务器域名
    @Value("${wechat.hitef.server-domain-url}")
    private String serverDomainUrl;

    @Value("${server.context-path}")
    private String contextPath;

    // 支付-统一下单处理对应的url
    @Value("${wechat.hitef.pay-url}")
    private String payUrl;

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

    public boolean isJsSdkDebuged() {
        return jsSdkDebuged;
    }

    public String getJsApiList() {
        return jsApiList;
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

    public String getCertificationPath() {
        return certificationPath;
    }

}
