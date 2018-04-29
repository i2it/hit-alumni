package site.liuming.hitef.listener;

import site.liuming.hitef.constant.AppProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

/**
 * 网站每次启动时需要对web项目的配置进行初始化的设置监听器
 * Created by liuming on 2017/5/2.
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    @Autowired
    private AppProperty appConfigProperties;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // todo 考虑将这个图片放到图片cdn服务，存储图片的网络url
        //将这个信息放到应用的上下文中
        servletContextEvent.getServletContext().setAttribute("globalUrlPrefix", appConfigProperties.getServerDomainUrl() + appConfigProperties.getContextPath());
        //创建指定文件目录
        File file = new File(appConfigProperties.getCertificationPath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
