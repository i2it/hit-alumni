package net.i2it.hit.hitef.controller;

import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.wechat.constant.EncryptionTypeEnum;
import net.i2it.wechat.util.common.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 处理和微信服务器之间的网络交互操作
 */
@Controller
@RequestMapping("/msg")
public class WeChatMsgController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatMsgController.class);

    @Autowired
    private AppConfigProperties appConfigProperties;

    /**
     * 用于验证微信服务器发来的微信开发接入请求
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     */
    @ResponseBody
    @GetMapping(params = {"signature", "timestamp", "nonce", "echostr"})
    public String verify(String signature, String timestamp, String nonce, String echostr) {
        // 第一步：对参数token、timestamp、nonce排序
        String[] arr = new String[]{appConfigProperties.getToken(), timestamp, nonce};
        Arrays.sort(arr);
        //第二步：将排序后的token、timestamp、nonce参数拼接为一个字符串后，进行sha1加密
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            tmp.append(arr[i]);
        }
        String result = null;
        try {
            result = EncryptionUtils.encrypt(tmp.toString(), EncryptionTypeEnum.SHA1.getValue());
        } catch (Exception e) {
            logger.info("接入微信公众平台，进行加密出现异常");
        }
        logger.info("微信服务器发来公众号平台接入验证信息：signature={}&timestamp={}&nonce={}&echostr={}，比较{}和{}的值可判断是否处理正确",
                signature, timestamp, nonce, echostr, signature, result);
        // 第三步：加密结果与signature比较，相同时返回参数echostr
        if (result != null && result.equals(signature)) {
            return echostr;
        } else {
            logger.info("微信平台接入认证接口收到了非微信服务器发来的验证请求，请小心！");
        }
        return null;
    }

    @PostMapping
    public void receivePostRequest(HttpServletRequest request) {
        processMsg(request, "post");
    }

    @GetMapping
    public void receiveGetRequest(HttpServletRequest request) {
        processMsg(request, "get");
    }

    private void processMsg(HttpServletRequest request, String reqMethod) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }
            logger.info("微信服务器通过 {} 方式发来的请求信息为：{}", reqMethod, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
