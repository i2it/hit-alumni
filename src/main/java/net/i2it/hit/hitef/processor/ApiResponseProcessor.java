package net.i2it.hit.hitef.processor;

import com.alibaba.fastjson.JSONObject;
import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.hit.hitef.constant.CacheConsts;
import net.i2it.hit.hitef.domain.api.response.AppAccessTokenDTO;
import net.i2it.hit.hitef.domain.api.response.JsApiTicketDTO;
import net.i2it.hit.hitef.domain.api.response.UnifiedOrderResultVO;
import net.i2it.hit.hitef.domain.api.response.WebAccessTokenVO;
import net.i2it.wechat.util.WeChatApiRequestUtils;
import net.i2it.wechat.util.common.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * 处理微信接口调用结果
 *
 * @author liuming
 * @date 2017/11/15 16:48
 */
@Component
public class ApiResponseProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseProcessor.class);

    @Autowired
    private AppConfigProperties appConfigProperties;

    //获取全局access_token信息
    public AppAccessTokenDTO getAppAccessToken() {
        try {
            JSONObject jsonObject = WeChatApiRequestUtils.getAppAccessToken(appConfigProperties.getAppId(), appConfigProperties.getAppSecret());
            if (jsonObject.get("access_token") != null) {
                AppAccessTokenDTO appAccessTokenDTO = JSONObject.toJavaObject(jsonObject, AppAccessTokenDTO.class);
                return appAccessTokenDTO;
            }
            logger.info("获取全局access_token失败");
        } catch (IOException e) {
            logger.error("获取全局access_token异常，信息：{}", e.getStackTrace());
        }
        return null;
    }

    //创建公众号菜单
    public boolean createMenu(String menuStr) {
        try {
            JSONObject jsonObject = WeChatApiRequestUtils.createMenu(CacheConsts.APP_ACCESS_TOKEN, menuStr);
            if ((Integer) jsonObject.get("errcode") == 0) {
                return true;
            }
            logger.info("公众号菜单创建失败");
        } catch (IOException e) {
            logger.error("公众号菜单创建出现异常，信息：{}", e.getStackTrace());
        }
        return false;
    }

    //获取网页开发中需要使用的access_token
    public WebAccessTokenVO getWebAccessToken(String code) {
        try {
            JSONObject jsonObject = WeChatApiRequestUtils.getWebAccessToken(appConfigProperties.getAppId(), appConfigProperties.getAppSecret(), code);
            if (jsonObject.get("access_token") != null) {
                WebAccessTokenVO webAccessTokenVO = JSONObject.toJavaObject(jsonObject, WebAccessTokenVO.class);
                return webAccessTokenVO;
            }
            logger.info("获取网页开发中的access_token失败");
        } catch (IOException e) {
            logger.error("获取网页开发中的access_token出现异常，信息：{}", e.getStackTrace());
        }
        return null;
    }

    //获取网页开发拉取用户信息所需的access_token信息
    public JsApiTicketDTO getJsApiTicket() {
        try {
            JSONObject jsonObject = WeChatApiRequestUtils.getJsApiTicket(CacheConsts.APP_ACCESS_TOKEN);
            if (jsonObject.get("ticket") != null) {
                JsApiTicketDTO jsApiTicketDTO = new JsApiTicketDTO();
                jsApiTicketDTO.setErrcode((Integer) jsonObject.get("errcode"));
                jsApiTicketDTO.setErrmsg((String) jsonObject.get("errmsg"));
                jsApiTicketDTO.setTicket((String) jsonObject.get("ticket"));
                jsApiTicketDTO.setExpires_in((Integer) jsonObject.get("expires_in"));
                return jsApiTicketDTO;
            }
            logger.info("获取网页开发拉取用户信息所需的access_token失败");
        } catch (IOException e) {
            logger.error("获取网页开发来去用户信息所需的access_token异常，信息：", e.getStackTrace());
        }
        return null;
    }

    //获得统一下单结果
    public UnifiedOrderResultVO getUnifiedOrderResult(String unifiedOrderXmlStr) {
        try {
            String xmlStr = WeChatApiRequestUtils.getUnifiedOrderResult(unifiedOrderXmlStr);
            UnifiedOrderResultVO unifiedOrderResultVO = (UnifiedOrderResultVO) XmlUtils.xmlStr2Object(xmlStr, UnifiedOrderResultVO.class);
            return unifiedOrderResultVO;
        } catch (IOException e) {
            logger.info("统一下单请求出错");
        } catch (JAXBException e) {
            logger.info("将统一下单结果转为对对象实例出错");
        }
        return null;
    }

}
