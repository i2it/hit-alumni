package site.liuming.hitef.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.liuming.hitef.constant.AppConsts;
import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.domain.ContributorVO;
import site.liuming.hitef.domain.DonateRecordDO;
import site.liuming.hitef.domain.PrepayInfoDTO;
import site.liuming.hitef.domain.PrepayInfoVO;
import site.liuming.hitef.service.DonateService;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 关于捐助的前端控制器
 *
 * @author liuming
 * @date 2017/11/18 20:45
 */
@Controller
@RequestMapping(value = {"/donate", "/test"})
public class DonateController {

    private static final Logger logger = LoggerFactory.getLogger(DonateController.class);

    @Autowired
    private AppProperty appProperty;
    @Autowired
    private DonateService donateService;

    /**
     * 统一下单，获取下一步实际支付所需的js提交参数
     *
     * @param payInfo
     * @param code
     * @param modelMap
     * @return
     */
    @GetMapping(params = {"code"})
    public String prepay(String payInfo, String code, ModelMap modelMap) {
        //【1】、解析用户提交的支付订单信息，并转为相应的封装类实例对象
        PrepayInfoVO prepayInfoVO = this.getPrepayVO(payInfo);
        //捐款上限，避免超出数据库中数值范围
        if (prepayInfoVO == null) {
            return AppConsts.DEFAULT_WECHAT_PAGE;
        }
        if (prepayInfoVO.getMoney() > 90000000.0) {
            return "redirect:" + appProperty.getContextPath() + "/items/" + prepayInfoVO.getId();
        }
        PrepayInfoDTO prepayInfoDTO = donateService.getPayRequestInfo(code, prepayInfoVO);
        modelMap.put("fundItemId", prepayInfoVO.getId());
        //统一下单后，商户订单号
        modelMap.put("out_trade_no", prepayInfoDTO.getOutTradeNO());
        //页面发起js_api字符需要的配置信息
        modelMap.put("payInfo", prepayInfoDTO.getPayRequestVO());
        return "client/payAction";
    }

    /**
     * 处理客户端传来的支付信息，得到支付项目id、支付项目名称和金额
     *
     * @param payInfoStr 以 <支付项目id__支付项目名称__支付金额> 组织的支付信息
     * @return
     */
    private PrepayInfoVO getPrepayVO(String payInfoStr) {
        String[] arr = payInfoStr.split("__");
        if (arr.length == 3) {
            try {
                return new PrepayInfoVO(Integer.parseInt(arr[0]), arr[1], Double.parseDouble(arr[2]));
            } catch (Exception e) {
                logger.error("用户支付提交的数据异常，支付信息：{}，错误信息：{}", payInfoStr, e.getMessage());
            }
        }
        return null;
    }

    //支付成功后微信服务器发起通知的地址，需要返回特定信息，不然微信服务器会一直发信息请求确认
    @RequestMapping("/resultNofity")
    @ResponseBody
    public String notifyResult(HttpServletRequest request, HttpServletResponse response) {
        //获取支付成功后微信服务器返回的支付成功通知
        response.setContentType("text/xml");
        ServletInputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            in = request.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > 0) {
                sb.append(new String(bytes, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对通知中的信息可以进行二次校验
        if (donateService.updatePayState(sb.toString())) {
            return "SUCCESS";
        }
        return "";
    }

    /**
     * 用户完成支付后进入到捐赠者信息填写页面
     *
     * @param outTradeNo
     * @param map
     * @return
     */
    @GetMapping(params = {"action=contributorInfo"})
    public String updateContributorInfo(String outTradeNo, ModelMap map) {
        map.put("out_trade_no", outTradeNo);//支付单的对应的唯一id
        map.put("donateInfo", donateService.getDonateInfo(outTradeNo));
        return "client/contributorForm";
    }

    //捐赠者信息提交之后，转入支付过程结束提示页面
    @PostMapping(params = {"action=contributorInfo"})
    public String updateContributorInfo(String outTradeNo, String comment, ContributorVO contributorVO, ModelMap map) {
        //但内容为空字符串时，赋值为null
        comment = "".equals(comment == null ? null : comment.trim()) ? null : comment;
        fillBankStringFieldWithNull(contributorVO);
        donateService.updateContributorInfo(outTradeNo, comment, contributorVO);
        if (null == contributorVO.getTrueName() || "".equals(contributorVO.getTrueName().trim())) {
            contributorVO.setTrueName("匿名");
        }
        map.put("contributorName", contributorVO.getTrueName().equals("匿名") ? "校友" : contributorVO.getTrueName());
        map.put("out_trade_no", donateService.createCertificate(outTradeNo));
        return "client/payResult";
    }

    /**
     * 通过反射将传入的ContributorVO实例对象中值为空字符串的字符串变量赋值为null
     *
     * @param contributorVO {@link ContributorVO}
     */
    private void fillBankStringFieldWithNull(ContributorVO contributorVO) {
        Field[] fields = ContributorVO.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().getName().equals("class java.lang.String")) {
                //将私有属性设为可访问
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(contributorVO);
                    //变量值是否为null
                    if (fieldValue == null) {
                        continue;
                    }
                    //字符串变量值为空字符串，则赋值为null
                    if ("".equals(((String) fieldValue).trim())) {
                        field.set(contributorVO, null);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 捐赠证书图片
     *
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/certification/{id}")
    public String certification(ModelMap map, @PathVariable("id") String id) {
        map.put("out_trade_no", id);
        return "client/donateCertification";
    }

    /**
     * 微信捐赠名录，实际已经完成支付的捐助记录列表，分页
     *
     * @param pageIndex
     * @param map
     * @return
     */
    @GetMapping("/list")
    public String donateList(@RequestParam(value = "page", required = false) Integer pageIndex, ModelMap map) {
        if (pageIndex == null) {
            pageIndex = 1;
        }
        List<DonateRecordDO> donateRecordDOList = donateService.getDonateRecordByPage(pageIndex);
        map.put("donateList", donateRecordDOList);
        map.put("currentPageIndex", pageIndex);
        return "client/donateList";
    }

    /**
     * 微信捐赠统计
     *
     * @param map
     * @return
     */
    @GetMapping("/stat")
    public String donateStat(ModelMap map) {
        map.put("stat", donateService.getStatistics());
        return "client/donateStat";
    }

    /**
     * 其他捐赠方式 页面
     *
     * @return
     */
    @GetMapping("/otherWay")
    public String otherWay() {
        return "client/otherDonateWays";
    }

    /**
     * 联系我们 页面
     *
     * @return
     */
    @GetMapping("/contactUs")
    public String contactUs() {
        return "client/contactUs";
    }

}
