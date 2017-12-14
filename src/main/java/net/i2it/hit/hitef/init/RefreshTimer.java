package net.i2it.hit.hitef.init;

import net.i2it.hit.hitef.constant.CacheConsts;
import net.i2it.hit.hitef.domain.api.response.AppAccessTokenDTO;
import net.i2it.hit.hitef.domain.api.response.JsApiTicketDTO;
import net.i2it.hit.hitef.processor.ApiResponseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时刷新任务器：定时更新access_token和jsapi_ticket<br>
 * spring容器在启动时会自动初始化该类并启动定时任务
 *
 * @author liuming
 * @date 2017/11/15 16:23
 */
@Component
@Lazy(value = false)
public class RefreshTimer {

    private Boolean menuCreated = false;

    @Autowired
    private ApiResponseProcessor apiResponseProcessor;
    @Autowired
    private MenuCreator menuCreator;


    //每个90分钟刷新一次
    @Scheduled(fixedRate = 90 * 60 * 100)
    public void refresh() {
        //更新access_token
        AppAccessTokenDTO appAccessTokenDTO;
        while ((appAccessTokenDTO = apiResponseProcessor.getAppAccessToken()) == null) {
            this.sleep(40);
        }
        CacheConsts.APP_ACCESS_TOKEN = appAccessTokenDTO.getAccess_token();

        //更新js_api_tiket
        JsApiTicketDTO jsApiTicketDTO;
        while ((jsApiTicketDTO = apiResponseProcessor.getJsApiTicket()) == null) {
            this.sleep(40);
        }
        CacheConsts.JS_API_TICKET = jsApiTicketDTO.getTicket();

        //获取到access_token之后，可以创建菜单了
        while (!menuCreated) {
            if (menuCreator.create()) {
                menuCreated = true;
            } else {
                this.sleep(30);
            }
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
