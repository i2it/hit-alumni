package net.i2it.hit.hitef.controller;

import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.hit.hitef.domain.FundItemDO;
import net.i2it.hit.hitef.processor.JsSdkConfigProcessor;
import net.i2it.hit.hitef.service.FundInfoService;
import net.i2it.wechat.constant.WeChatApiUrlConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@Controller
public class FundItemController {

    @Autowired
    private AppConfigProperties appConfigProperties;

    private static final String DEFAULT_FRONT_PAGE = "redirect:/wechat/hitef/items";
    private static final String DEFAULT_BACKEND_PAGE = "redirect:/wechat/hitef/items?type=1";

    @Autowired
    private JsSdkConfigProcessor jsSdkConfigProcessor;
    @Autowired
    private FundInfoService fundInfoService;

    /*----------------微信端动作----------------*/

    //微信端捐款项目展示列表页面
    @GetMapping(value = "/items")
    public String showFundItemsByQueryType(@RequestParam(value = "q", required = false) String q,
                                           HttpServletRequest request, ModelMap map) {
        map.put("jsSdkConfig", jsSdkConfigProcessor.getJsSdkConfig(request));//调用微信页面js sdk功能需要的配置信息
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
        map.put("jsSdkConfig", jsSdkConfigProcessor.getJsSdkConfig(request));//调用微信页面js sdk功能需要的配置信息
        //先判断该id的基金项目是否存在
        FundItemDO fundItemDO = fundInfoService.getFundItemById(id);
        if (fundItemDO == null) {//不存在
            return DEFAULT_FRONT_PAGE;
        }
        //基金项目存在时，才执行更新
        //进入某一个筹款基金项目的页面
        map.put("item", fundItemDO);//获取某个筹款项目的信息
        map.put("redirectUrl", appConfigProperties.getPayUrl());//对应着实际支付动作的url页面
        //拉取用户openid的url拼接
        map.put("targetUrl", WeChatApiUrlConsts.WEB_CODE_URL.replace("APPID", appConfigProperties.getAppId())
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

    /*---------------管理后台动作----------------*/
    /*
    //管理后台：根据 捐款项目是否已经被终止 去展示
    @GetMapping(value = "/items", params = {"type"})
    public String showFundItemsByType(Integer type, ModelMap map) {
        if (type == 0) {//终止的基金项目
            map.put("fundItems", fundInfoService.getStopedFundItems());
        } else {//正常进行的基金项目
            map.put("fundItems", fundInfoService.getNormalFundItems());
        }
        return "admin/fundItems";
    }

    //管理后台：显示增加捐款项目的表单页面
    @GetMapping(value = "/items", params = {"opt"})
    public String addFundItem(String opt, ModelMap map) {
        if ("add".equals(opt)) {//新增基金信息动作
            map.put("opt", "add");
            return "admin/fundItemForm";
        }
        return DEFAULT_BACKEND_PAGE;
    }

    //管理后台：增加捐款项目的动作
    @PostMapping(value = "/items")
    public String addFundItem(@RequestParam(value = "picture", required = false) MultipartFile file, FundItemDO fundItem,
                              HttpServletRequest request) {
        long tmp = System.currentTimeMillis();
        if (file != null) {
            String targetFileName = upload(file, tmp, request);
            //上传成功后
            fundItem.setPictureName(targetFileName);
        }
        fundItem.setStatus(1);
        fundItem.setCtime((int) (tmp / 1000));
        fundItem.setUtime((int) (tmp / 1000));
        fundInfoService.isSaved(fundItem);
        return DEFAULT_BACKEND_PAGE;
    }

    //管理后台：显示更新捐款项目的表单页面
    @GetMapping(value = "/items/{id}", params = {"opt"})
    public String updateFundItem(@PathVariable(value = "id") Integer id, String opt, ModelMap map) {
        //先判断该id的基金项目是否存在
        FundItemDO fundItemDO = fundInfoService.getFundItemById(id);
        if (fundItemDO == null) {//不存在
            return DEFAULT_BACKEND_PAGE;
        }
        //基金项目存在时，才执行更新
        if ("update".equals(opt)) {//获取捐款项目信息 用于显示在修改信息页面
            map.put("opt", "update");
            map.put("fundItem", fundItemDO);
            return "admin/fundItemForm";
        }
        if ("stop".equals(opt) && fundInfoService.isStoped(id)) {//终止捐款项目
            return DEFAULT_BACKEND_PAGE;
        }
        return DEFAULT_BACKEND_PAGE;
    }

    //管理后台：更新捐款项目信息动作
    @PostMapping("/items/{id}")
    public String updateFundItem(@RequestParam(value = "picture", required = false) MultipartFile file,
                                 @PathVariable("id") Integer id, FundItemDO fundItem,
                                 HttpServletRequest request) {
        //先判断该id的基金项目是否存在
        FundItemDO fundItemDO = fundInfoService.getFundItemById(id);
        if (fundItemDO == null) {//不存在
            return DEFAULT_BACKEND_PAGE;
        }
        //上传图片
        long tmp = System.currentTimeMillis();
        if (file != null) {
            String targetFileName = upload(file, tmp, request);
            if (targetFileName == null) {//没有上传图片，还是用之前上传的图片
                fundItem.setPictureName(fundItemDO.getPictureName());
            } else {
                fundItem.setPictureName(targetFileName);
            }
        }
        //基金项目存在
        fundItem.setId(id);
        fundItem.setUtime((int) (tmp / 1000));//获取1970年1月1日0时起的秒数
        if (fundInfoService.isUpdated(fundItem)) {//更新成功
            return DEFAULT_BACKEND_PAGE;
        }
        return DEFAULT_BACKEND_PAGE;
    }

    // 上传图片
    private String upload(MultipartFile file, long timestamp, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        if (!"".equals(fileName)) {
            //上传路径
            String targetPath = request.getServletContext().getRealPath("WEB-INF/pictures");
            //构建上传后文件名
            StringBuilder targetFileName = new StringBuilder();
            targetFileName.append(timestamp);
            targetFileName.append(fileName.substring(fileName.lastIndexOf(".")));
            System.out.println(targetFileName.toString());
            //构建上传目标文件
            File targetFile = new File(targetPath, targetFileName.toString());
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            //上传图片
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return targetFileName.toString();
        }
        return null;
    }
    */

}
