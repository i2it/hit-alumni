package site.liuming.hitef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.liuming.hitef.domain.api.response.AppAccessTokenDTO;
import site.liuming.hitef.domain.api.response.JsApiTicketDTO;
import site.liuming.hitef.manager.ApiResponseManager;

/**
 * 定时刷新任务器：定时更新access_token和jsapi_ticket<br>
 * spring容器在启动时会自动初始化该类并启动定时任务
 *
 * @author liuming
 * @date 2017/11/15 16:23
 */
@Component
@Lazy(value = false)
public class AppParameterRefreshTimer {

    private Boolean menuCreated = false;

    @Autowired
    private ApiResponseManager apiResponseManager;
    @Autowired
    private AppMenuCreator appMenuCreator;


    //每个90分钟刷新一次
    @Scheduled(fixedRate = 90 * 60 * 100)
    public void refresh() {
        //更新access_token
        AppAccessTokenDTO appAccessTokenDTO;
        while ((appAccessTokenDTO = apiResponseManager.getAppAccessToken()) == null) {
            this.sleep(40);
        }
        AppCache.APP_ACCESS_TOKEN = appAccessTokenDTO.getAccess_token();

        //更新js_api_tiket
        JsApiTicketDTO jsApiTicketDTO;
        while ((jsApiTicketDTO = apiResponseManager.getJsApiTicket()) == null) {
            this.sleep(40);
        }
        AppCache.JS_API_TICKET = jsApiTicketDTO.getTicket();

        // 应用首次启动时创建菜单
        while (!menuCreated) {
            if (appMenuCreator.create()) {
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
