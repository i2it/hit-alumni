package net.i2it.hit.hitef.service.function;

import com.alibaba.fastjson.JSON;
import net.i2it.hit.hitef.constant.ConfigConsts;
import net.i2it.hit.hitef.domain.api.request.MenuVO;

import java.io.UnsupportedEncodingException;

/**
 * 创建微信菜单的具体操作业务
 * Created by liuming on 2017/4/21.
 */
public class MenuOption {

    public boolean create() {
        String menuStr = null;
        try {
            menuStr = this.getMenuStr();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new WeChatApi().createMenu(menuStr);
    }

    private String getMenuStr() throws UnsupportedEncodingException {
        MenuVO menu = new MenuVO();
        //从左数第一个一级菜单
        MenuVO.Button button0 = menu.new Button();
        //从左数第一个一级菜单的二级菜单
        MenuVO.ViewButton button00 = menu.new ViewButton();
        MenuVO.ViewButton button01 = menu.new ViewButton();
        MenuVO.ViewButton button02 = menu.new ViewButton();
        MenuVO.ViewButton button03 = menu.new ViewButton();
        MenuVO.ViewButton button04 = menu.new ViewButton();
        MenuVO.Button button1 = menu.new Button();
        MenuVO.ViewButton button10 = menu.new ViewButton();
        MenuVO.ViewButton button11 = menu.new ViewButton();
        MenuVO.Button button2 = menu.new Button();
        MenuVO.ViewButton button20 = menu.new ViewButton();
        MenuVO.ViewButton button21 = menu.new ViewButton();
        MenuVO.ViewButton button22 = menu.new ViewButton();
        MenuVO.ViewButton button23 = menu.new ViewButton();

        //第一栏菜单
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
        button04.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/donate/contactUs");
        button0.setName("关于我们");
        button0.setSub_button(new MenuVO.Button[]{button00, button01, button02, button03, button04});

        //第二栏菜单
        button10.setName("特别推荐");
        button10.setType("view");
        button10.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/items?q=school");
        button11.setName("院系基金");
        button11.setType("view");
        button11.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/items?q=academy");
        button1.setName("募捐项目");
        button1.setSub_button(new MenuVO.Button[]{button10, button11});

        //第三栏菜单
        button20.setName("微信捐赠");
        button20.setType("view");
        button20.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/items");
        button21.setName("微信捐赠名录");
        button21.setType("view");
        button21.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/donate/list");
        button22.setName("微信捐赠统计");
        button22.setType("view");
        button22.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/donate/stat");
        button23.setName("其他捐赠方式");
        button23.setType("view");
        button23.setUrl(ConfigConsts.getServer_domain_url() + "/wechat/hitef/donate/otherWay");
        button2.setName("我要捐赠");
        button2.setSub_button(new MenuVO.Button[]{button20, button21, button22, button23});

        menu.setButton(new MenuVO.Button[]{button0, button1, button2});

        return JSON.toJSONString(menu);
    }

}
