package site.liuming.hitef;

import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.domain.api.request.JsSdkConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.liuming.wechat.common.Generator;
import site.liuming.wechat.common.util.EncryptionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * JS-SDK使用权限签名算法，根据不同的url等情况生成不同的js签名
 *
 * @author liuming
 * @date 2017/11/15 21:10
 */
@Component
public class AppWebPageJsSdkConfigGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AppWebPageJsSdkConfigGenerator.class);

    @Autowired
    private AppProperty appConfigProperties;

    /**
     * 获取微信js-sdk配置信息的数据
     *
     * @param request
     * @return
     */
    public JsSdkConfigVO generateJsSdkConfig(HttpServletRequest request) {
        //获取请求的完成url（包括请求参数）
        String fullUrl = request.getQueryString() != null ?
                (request.getRequestURL() + "?" + request.getQueryString()) : (request.getRequestURL() + "");
        return this.generateJsSdkConfig(fullUrl);
    }

    /**
     * 获取网页面传输js-sdk配置信息的实例对象
     *
     * @param url
     * @return
     */
    public JsSdkConfigVO generateJsSdkConfig(String url) {
        JsSdkConfigVO JsSdkConfigVO = new JsSdkConfigVO();
        JsSdkConfigVO.setNonceStr(Generator.generateRandomStr(16));
        JsSdkConfigVO.setAppId(appConfigProperties.getAppId());
        JsSdkConfigVO.setTimestamp(System.currentTimeMillis());
        JsSdkConfigVO.setSignature(this.generateSign(JsSdkConfigVO, url));
        return JsSdkConfigVO;
    }

    // 微信网页js sdk配置中的signatrue的签名生成算法
    private String generateSign(JsSdkConfigVO JsSdkConfigVO, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=").append(AppCache.JS_API_TICKET)
                .append("&noncestr=").append(JsSdkConfigVO.getNonceStr())
                .append("&timestamp=").append(JsSdkConfigVO.getTimestamp())
                .append("&url=").append(url);
        String result = null;
        try {
            result = EncryptionUtils.encrypt(sb.toString(), EncryptionUtils.EncryptionTypeEnum.SHA1.getValue());
        } catch (Exception e) {
            logger.error("JS-SDK使用权限签名算法加密出现异常，信息：{}", e.getMessage());
        }
        return result;
    }

}
