package site.liuming.hitef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.constant.AppConsts;
import site.liuming.hitef.constant.FundItemTypeEnum;
import site.liuming.hitef.domain.FundItemDO;
import site.liuming.hitef.service.FundInfoService;
import site.liuming.wechat.common.constant.ApiUrlConsts;

import java.util.List;

/**
 * 捐助项目操作相关控制器
 */
@Controller
public class FundItemController {

    @Autowired
    private AppProperty appConfigProperties;
    @Autowired
    private FundInfoService fundInfoService;

    /**
     * 根据参数type来决定获取校级捐助项目列表还是院系捐助项目列表
     *
     * @param type
     * @param map
     * @return
     */
    @GetMapping(value = "/items")
    public String listFundItemsByType(@RequestParam(value = "type", required = false) Integer type, ModelMap map) {
        if (FundItemTypeEnum.SCHOOL.getType().equals(type)) {//展示 特别推荐（校级基金）
            List<FundItemDO> fundItems = fundInfoService.getSchoolNormalFundItems();
            map.put("fundItems", fundItems);
            return "client/fundList";
        }
        if (FundItemTypeEnum.ACADEMY.getType().equals(type)) {// 展示 院系基金
            List<FundItemDO> fundItems = fundInfoService.getAcademyNormalFundItems();
            map.put("fundItems", fundItems);
            return "client/fundList";
        }
        //展示 所有的正常状态的筹款基金项目页面，包括特别推荐（校级基金）和院系基金
        List<FundItemDO> fundItems = fundInfoService.getNormalFundItems();
        map.put("fundItems", fundItems);
        return "client/fundList";
    }

    /**
     * 根据捐助项目的具体id读取信息
     *
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "/items/{id}")
    public String getFundItem(@PathVariable(value = "id") Integer id, ModelMap map) {
        //先判断该id的基金项目是否存在
        FundItemDO fundItemDO = fundInfoService.getFundItemById(id);
        if (fundItemDO == null) {//不存在
            return AppConsts.DEFAULT_WECHAT_PAGE;
        }
        //基金项目存在时，才执行更新
        //进入某一个筹款基金项目的页面
        map.put("item", fundItemDO);//获取某个筹款项目的信息
        map.put("redirectUrl", appConfigProperties.getPayUrl());//对应着实际支付动作的url页面
        //拉取用户openid的url拼接
        map.put("targetUrl", ApiUrlConsts.WEB_CODE_URL.replace("APPID", appConfigProperties.getAppId())
                .replace("SCOPE", "snsapi_base").replace("STATE", "site/liuming/hitef"));
        return "client/payForm";
    }

}
