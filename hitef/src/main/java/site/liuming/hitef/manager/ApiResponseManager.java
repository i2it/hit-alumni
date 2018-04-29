package site.liuming.hitef.manager;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.AppCache;
import site.liuming.hitef.domain.api.response.AppAccessTokenDTO;
import site.liuming.hitef.domain.api.response.JsApiTicketDTO;
import site.liuming.hitef.domain.api.response.UnifiedOrderResultDTO;
import site.liuming.hitef.domain.api.response.WebAccessTokenDTO;
import site.liuming.wechat.common.ApiUrlRequest;
import site.liuming.wechat.common.util.XmlUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * 处理微信接口调用结果，并转为相应的封装类对象
 *
 * @author liuming
 * @date 2017/11/15 16:48
 */
@Component
public class ApiResponseManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseManager.class);

    @Autowired
    private AppProperty appConfigProperties;

    public AppAccessTokenDTO getAppAccessToken() {
        try {
            JSONObject jsonObject = ApiUrlRequest.
                    getAppAccessToken(appConfigProperties.getAppId(), appConfigProperties.getAppSecret());
            if (jsonObject.get("access_token") != null) {
                return JSONObject.toJavaObject(jsonObject, AppAccessTokenDTO.class);
            }
            logger.info("获取全局access_token失败");
        } catch (IOException e) {
            logger.error("获取全局access_token异常，信息：{}", e.getMessage());
        }
        return null;
    }

    public boolean createMenu(String menuStr) {
        try {
            JSONObject jsonObject = ApiUrlRequest.createMenu(AppCache.APP_ACCESS_TOKEN, menuStr);
            if ((Integer) jsonObject.get("errcode") == 0) {
                return true;
            }
            logger.info("公众号菜单创建失败");
        } catch (IOException e) {
            logger.error("公众号菜单创建出现异常，信息：{}", e.getMessage());
        }
        return false;
    }

    public WebAccessTokenDTO getWebAccessToken(String code) {
        try {
            JSONObject jsonObject = ApiUrlRequest.getWebAccessToken(appConfigProperties.getAppId(), appConfigProperties.getAppSecret(), code);
            if (jsonObject.get("access_token") != null) {
                return JSONObject.toJavaObject(jsonObject, WebAccessTokenDTO.class);
            }
            logger.info("获取网页开发中的access_token失败");
        } catch (IOException e) {
            logger.error("获取网页开发中的access_token出现异常，信息：{}", e.getMessage());
        }
        return null;
    }

    public JsApiTicketDTO getJsApiTicket() {
        try {
            JSONObject jsonObject = ApiUrlRequest.getJsApiTicket(AppCache.APP_ACCESS_TOKEN);
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
            logger.error("获取网页开发来去用户信息所需的access_token异常，信息：", e.getMessage());
        }
        return null;
    }

    public UnifiedOrderResultDTO getUnifiedOrderResult(String unifiedOrderXmlStr) {
        try {
            String xmlStr = ApiUrlRequest.getUnifiedOrderResult(unifiedOrderXmlStr);
            return (UnifiedOrderResultDTO) XmlUtils.xmlStr2Object(xmlStr, UnifiedOrderResultDTO.class);
        } catch (IOException e) {
            logger.info("统一下单请求出错");
        } catch (JAXBException e) {
            logger.info("将统一下单结果转为对对象实例出错");
        }
        return null;
    }

}
