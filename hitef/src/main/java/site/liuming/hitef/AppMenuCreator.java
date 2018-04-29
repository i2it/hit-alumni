package site.liuming.hitef;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.domain.api.request.MenuBO;
import site.liuming.hitef.manager.ApiResponseManager;

/**
 * 创建微信菜单的具体操作业务
 * Created by liuming on 2017/4/21.
 */
@Component
public class AppMenuCreator {

    @Autowired
    private AppProperty appConfigProperties;
    @Autowired
    private ApiResponseManager apiResponseManager;

    public boolean create() {
        String menuStr = this.getMenuStr();
        return apiResponseManager.createMenu(menuStr);
    }


    //设置公众号菜单的内容，生成对应的json格式字符串内容
    private String getMenuStr() {
        String appPrefixUrl = appConfigProperties.getServerDomainUrl() + appConfigProperties.getContextPath();

        //微信公众号菜单
        MenuBO menuBO = new MenuBO();

        //左边数第一栏的菜单
        //一级菜单
        MenuBO.Button button0 = menuBO.new Button();
        //一级菜单的二级菜单
        MenuBO.ViewButton button00 = menuBO.new ViewButton();
        MenuBO.ViewButton button01 = menuBO.new ViewButton();
        MenuBO.ViewButton button02 = menuBO.new ViewButton();
        MenuBO.ViewButton button03 = menuBO.new ViewButton();
        MenuBO.ViewButton button04 = menuBO.new ViewButton();
        //设置按钮
        button00.setName("基金会简介");
        button00.setType("view");
        button00.setUrl("http://d.eqxiu.com/s/AJHjQTCM");
        button01.setName("基金会网站");
        button01.setType("view");
        button01.setUrl("http://hitef.hit.edu.cn");
        button02.setName("基金会章程");
        button02.setType("view");
        button02.setUrl("http://hitef.hit.edu.cn/7721/list.htm");
        button03.setName("免税政策");
        button03.setType("view");
        button03.setUrl("http://hitef.hit.edu.cn/7702/list.htm");
        button04.setName("联系我们");
        button04.setType("view");
        button04.setUrl(appPrefixUrl + "/donate/contactUs");
        button0.setName("关于我们");
        button0.setSub_button(new MenuBO.Button[]{button00, button01, button02, button03, button04});

        //左边数第二栏菜单
        //一级菜单
        MenuBO.Button button1 = menuBO.new Button();
        //二级菜单
        MenuBO.ViewButton button10 = menuBO.new ViewButton();
        MenuBO.ViewButton button11 = menuBO.new ViewButton();
        //设置按钮
        button10.setName("特别推荐");
        button10.setType("view");
        button10.setUrl(appPrefixUrl + "/items?q=school");
        button11.setName("院系基金");
        button11.setType("view");
        button11.setUrl(appPrefixUrl + "/items?q=academy");
        button1.setName("募捐项目");
        button1.setSub_button(new MenuBO.Button[]{button10, button11});

        //左边数第三栏菜单
        //一级菜单
        MenuBO.Button button2 = menuBO.new Button();
        //二级菜单
        MenuBO.ViewButton button20 = menuBO.new ViewButton();
        MenuBO.ViewButton button21 = menuBO.new ViewButton();
        MenuBO.ViewButton button22 = menuBO.new ViewButton();
        MenuBO.ViewButton button23 = menuBO.new ViewButton();
        //设置菜单
        button20.setName("微信捐赠");
        button20.setType("view");
        button20.setUrl(appPrefixUrl + "/items");
        button21.setName("微信捐赠名录");
        button21.setType("view");
        button21.setUrl(appPrefixUrl + "/donate/list");
        button22.setName("微信捐赠统计");
        button22.setType("view");
        button22.setUrl(appPrefixUrl + "/donate/stat");
        button23.setName("其他捐赠方式");
        button23.setType("view");
        button23.setUrl(appPrefixUrl + "/donate/otherWay");
        button2.setName("我要捐赠");
        button2.setSub_button(new MenuBO.Button[]{button20, button21, button22, button23});

        menuBO.setButton(new MenuBO.Button[]{button0, button1, button2});

        return JSON.toJSONString(menuBO);
    }

}
