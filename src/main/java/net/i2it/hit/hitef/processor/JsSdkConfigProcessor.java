package net.i2it.hit.hitef.processor;

import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.hit.hitef.constant.CacheConsts;
import net.i2it.hit.hitef.domain.api.request.JsSdkConfigVO;
import net.i2it.wechat.constant.EncryptionTypeEnum;
import net.i2it.wechat.util.AlgorithmUtils;
import net.i2it.wechat.util.common.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * JS-SDK使用权限签名算法，根据不同的url等情况生成不同的js签名
 *
 * @author liuming
 * @date 2017/11/15 21:10
 */
@Component
public class JsSdkConfigProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JsSdkConfigProcessor.class);

    @Autowired
    private AppConfigProperties appConfigProperties;

    /**
     * 获取微信js-sdk配置信息的数据
     *
     * @param request
     * @return
     */
    public JsSdkConfigVO getJsSdkConfig(HttpServletRequest request) {
        //获取请求的完成url（包括请求参数）
        String fullUrl = request.getQueryString() != null ?
                (request.getRequestURL() + "?" + request.getQueryString()) : (request.getRequestURL() + "");
        return this.getJsSdkConfig(fullUrl);
    }

    /**
     * 获取网页面传输js-sdk配置信息的实例对象
     *
     * @param url
     * @return
     */
    public JsSdkConfigVO getJsSdkConfig(String url) {
        JsSdkConfigVO jsSdkConfigVO = new JsSdkConfigVO();
        jsSdkConfigVO.setNonceStr(AlgorithmUtils.randomStr(16));
        jsSdkConfigVO.setAppId(appConfigProperties.getAppId());
        jsSdkConfigVO.setTimestamp(System.currentTimeMillis());
        jsSdkConfigVO.setSignature(this.getSign(jsSdkConfigVO, url));
        return jsSdkConfigVO;
    }

    // 微信网页js sdk配置中的signatrue的签名算法
    private String getSign(JsSdkConfigVO jsSdkConfigVO, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=" + CacheConsts.JS_API_TICKET);
        sb.append("&noncestr=" + jsSdkConfigVO.getNonceStr());
        sb.append("&timestamp=" + jsSdkConfigVO.getTimestamp());
        sb.append("&url=" + url);
        String result = null;
        try {
            result = EncryptionUtils.encrypt(sb.toString(), EncryptionTypeEnum.SHA1.getValue());
        } catch (Exception e) {
            logger.error("JS-SDK使用权限签名算法加密出现异常，信息：{}", e.getStackTrace());
        }
        return result;
    }

}
