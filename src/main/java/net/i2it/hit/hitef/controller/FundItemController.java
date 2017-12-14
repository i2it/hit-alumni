package net.i2it.hit.hitef.controller;

import net.i2it.hit.hitef.constant.ConfigConsts;
import net.i2it.hit.hitef.domain.FundItemDO;
import net.i2it.hit.hitef.service.FundInfoService;
import net.i2it.hit.hitef.service.function.CommonService;
import net.i2it.hit.hitef.service.function.WeChatApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/wechat/hitef")
public class FundItemController {

    private static final String DEFAULT_FRONT_PAGE = "redirect:/wechat/hitef/items";

    @Autowired
    private CommonService commonService;
    @Autowired
    private FundInfoService fundInfoService;

    /*----------------微信端动作----------------*/

    //微信端捐款项目展示列表页面
    @GetMapping(value = "/items")
    public String showFundItemsByQueryType(@RequestParam(value = "q", required = false) String q,
                                           HttpServletRequest request, ModelMap map) {
        map.put("jsSdkConfig", commonService.getJsSdkConfig(request));//调用微信页面js sdk功能需要的配置信息
        String name = "校友年度捐赠";
        String name_ = "爱心传递基金";
        if ("school".equals(q)) {//展示 特别推荐（校级基金）
            List<FundItemDO> fundItems = fundInfoService.getSchoolNormalFundItems();
            rankFundItem(fundItems, name, name_);
            map.put("fundItems", fundItems);
            return "client/fundList";
        }
        if ("academy".equals(q)) {// 展示 院系基金
            List<FundItemDO> fundItems = fundInfoService.getAcademyNormalFundItems();
            map.put("fundItems", fundItems);
            return "client/fundList";
        }
        //展示 所有的正常状态的筹款基金项目页面，包括特别推荐（校级基金）和院系基金
        List<FundItemDO> fundItems = fundInfoService.getNormalFundItems();
        rankFundItem(fundItems, name, name_);
        map.put("fundItems", fundItems);
        return "client/fundList";
    }

    //微信端：展示某个捐款信息的页面
    @GetMapping(value = "/items/{id}")
    public String showFundItem(@PathVariable(value = "id") Integer id,
                               HttpServletRequest request, ModelMap map) {
        map.put("jsSdkConfig", commonService.getJsSdkConfig(request));//调用微信页面js sdk功能需要的配置信息
        //先判断该id的基金项目是否存在
        FundItemDO fundItemDO = fundInfoService.getFundItemById(id);
        if (fundItemDO == null) {//不存在
            return DEFAULT_FRONT_PAGE;
        }
        //基金项目存在时，才执行更新
        //进入某一个筹款基金项目的页面
        map.put("item", fundItemDO);//获取某个筹款项目的信息
        map.put("redirectUrl", ConfigConsts.getPay_url());//对应着实际支付动作的url页面
        //拉取用户openid的url拼接
        map.put("targetUrl", WeChatApi.API_WEB_CODE.replace("APPID", ConfigConsts.getApp_id())
                .replace("SCOPE", "snsapi_base").replace("STATE", "hitef"));
        return "client/payForm";
    }

    // 将*校友年度捐赠*放到*爱心传递基金*前面
    private void rankFundItem(List<FundItemDO> fundItems, String name, String name_) {
        FundItemDO fundItem = filterAlumniDonateItem(fundItems, name);
        if (fundItem != null) {
            int targetIndex = 0;
            for (int i = 0; i < fundItems.size(); i++) {
                if (name_.equals(fundItems.get(i).getName())) {
                    targetIndex = i;
                    break;
                }
            }
            fundItems.add(targetIndex, fundItem);
        }
    }

    // 如果*校友年度捐赠*存在，从 基金项目列表 将*校友年度捐赠*去除（删除）
    private FundItemDO filterAlumniDonateItem(List<FundItemDO> fundItems, String name) {
        Iterator<FundItemDO> iterator = fundItems.iterator();
        while (iterator.hasNext()) {
            FundItemDO fundItem = iterator.next();
            if (name.equals(fundItem.getName())) {
                iterator.remove();
                return fundItem;
            }
        }
        return null;
    }

}
