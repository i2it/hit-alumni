package net.i2it.hit.hitef.controller;

import net.i2it.hit.hitef.constant.ConfigConsts;
import net.i2it.hit.hitef.util.EncryptionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 处理和微信服务器之间的网络交互操作
 */
@Controller
@RequestMapping("/wechat/hitef/msg")
public class WeChatController {

    /**
     * 用于验证微信服务器发来的微信开发接入请求
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param out
     */
    @GetMapping(params = {"signature", "timestamp", "nonce", "echostr"})
    public void verify(String signature, String timestamp, String nonce, String echostr, PrintWriter out) {
        // 第一步：对参数token、timestamp、nonce排序
        String[] arr = new String[]{ConfigConsts.getToken(), timestamp, nonce};
        Arrays.sort(arr);
        //第二步：将排序后的token、timestamp、nonce参数拼接为一个字符串后，进行sha1加密
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            tmp.append(arr[i]);
        }
        String result = EncryptionUtil.encrypt(tmp.toString(), EncryptionUtil.SHA1);
        // 第三步：加密结果与signature比较，相同时返回参数echostr
        if (result != null && result.equals(signature)) {
            out.print(echostr);
        } else {
            System.out.println("收到非微信服务器发来的验证请求，请小心！");
        }
        out.flush();
        out.close();
    }

    @PostMapping
    public void receiveViaPost(HttpServletRequest request) {
        processMsg(request, "post");
    }

    @GetMapping
    public void receiveViaGet(HttpServletRequest request) {
        processMsg(request, "get");
    }

    private void processMsg(HttpServletRequest request, String reqMethod) {
        try {
            BufferedReader reader = request.getReader();
            StringBuffer sb = new StringBuffer();
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }
            System.out.println(">>> 微信服务器发送的" + reqMethod + "请求信息：" + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // todo  遇到微信服务器发到请求遇到404错误，带排查

}
