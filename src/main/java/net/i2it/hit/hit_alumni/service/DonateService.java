package net.i2it.hit.hit_alumni.service;

import net.i2it.hit.hit_alumni.constant.ConfigConsts;
import net.i2it.hit.hit_alumni.dao.DonateDao;
import net.i2it.hit.hit_alumni.dao.ItemDao;
import net.i2it.hit.hit_alumni.entity.po.DonatePO;
import net.i2it.hit.hit_alumni.entity.vo.DonatorVO;
import net.i2it.hit.hit_alumni.entity.vo.SimpleOrderInfoVO;
import net.i2it.hit.hit_alumni.entity.vo.api.request.JsSdkConfigVO;
import net.i2it.hit.hit_alumni.entity.vo.api.request.PayRequestVO;
import net.i2it.hit.hit_alumni.entity.vo.api.request.UnifiedOrderInfoVO;
import net.i2it.hit.hit_alumni.entity.vo.api.response.PayResultNotifyVO;
import net.i2it.hit.hit_alumni.entity.vo.api.response.UnifiedOrderResultVO;
import net.i2it.hit.hit_alumni.entity.vo.api.response.WebAccessTokenVO;
import net.i2it.hit.hit_alumni.service.function.JsSdkConfig;
import net.i2it.hit.hit_alumni.service.function.UnifiedOrder;
import net.i2it.hit.hit_alumni.service.function.WeChatApi;
import net.i2it.hit.hit_alumni.util.DonateCertificateUtil;
import net.i2it.hit.hit_alumni.util.ValueGeneratorUtil;
import net.i2it.hit.hit_alumni.util.WebUtil;
import net.i2it.hit.hit_alumni.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理前端控制器传来的参数，进行业务分发和处理
 */
@Service
public class DonateService {

    @Autowired
    private DonateDao donateDao;
    @Autowired
    private ItemDao itemDao;

    private Map<String, Object> getUnifiedOrderResult(String code, SimpleOrderInfoVO simpleOrderInfo) {
        WeChatApi weChatApi = new WeChatApi();
        //获取页面access_token
        WebAccessTokenVO webAccessTokenVO = weChatApi.getWebAccessToken(code);
        if (webAccessTokenVO != null) {
            //获取统一下单的提交参数
            Map<String, Object> map = new UnifiedOrder()
                    .getUnifiedOrderInfo(webAccessTokenVO.getOpenid(), simpleOrderInfo);
            //将统一下单接口接收的参数转为字符串类型的xml
            UnifiedOrderInfoVO unifiedOrderInfoVO = (UnifiedOrderInfoVO) map.get("order_info");//得到的对象中的金额是以单位分记的
            String unifiedOrderXmlStr = XmlUtil.object2XmlStr(unifiedOrderInfoVO);
            //获取统一下单结果
            UnifiedOrderResultVO unifiedOrderResult = weChatApi.getUnifiedOrderResult(unifiedOrderXmlStr);
            double totalFee = simpleOrderInfo.getItemMoney();//这个对象中的金额是以单位元记的
            map.put("total_fee", totalFee);
            //todo   donateDao.save(map)库操作可能会抛出异常
            if (unifiedOrderResult != null && donateDao.save(map) == 1) {
                map.clear();
                map.put("out_trade_no", unifiedOrderInfoVO.getOut_trade_no());
                map.put("unifiedOrderResult", unifiedOrderResult);
                return map;
            }
        }
        return null;
    }

    /**
     * 处理参数字符串，获取包含的订单简单信息
     *
     * @param itemInfo
     * @return
     */
    public SimpleOrderInfoVO getSimpleOrderInfo(String itemInfo) {
        String[] arr = itemInfo.split("_");
        if (arr.length == 4) {
            return new SimpleOrderInfoVO(arr[0], arr[1], arr[2], arr[3]);
        } else if (arr.length == 5) {
            return new SimpleOrderInfoVO(arr[0], arr[1], arr[2], arr[3], arr[4]);
        }
        return null;
    }

    /**
     * 获取微信js-sdk配置信息的数据
     *
     * @param request
     * @return
     */
    public JsSdkConfigVO getJsSdkConfig(HttpServletRequest request) {
        String url = WebUtil.getFullUrl(request);
        JsSdkConfig jsSdkConfig = new JsSdkConfig();
        return jsSdkConfig.getJsSdkConfig(url);
    }

    /**
     * 主要负责用于构建生成网页端js发起支付请求时需要数据的承载结构 PayRequestVO
     *
     * @param code
     * @param simpleOrderInfo
     * @return
     */
    public Map<String, Object> getPayRequestInfo(String code, SimpleOrderInfoVO simpleOrderInfo) {
        Map<String, Object> map = null;
        if (simpleOrderInfo != null) {
            map = this.getUnifiedOrderResult(code, simpleOrderInfo);
        }
        UnifiedOrderResultVO unifiedOrderResult = (UnifiedOrderResultVO) map.get("unifiedOrderResult");
        PayRequestVO payRequestVO = new PayRequestVO();
        payRequestVO.setAppId(ConfigConsts.getApp_id());
        payRequestVO.setTimeStamp(System.currentTimeMillis() / 10 + "");
        payRequestVO.setNonceStr(ValueGeneratorUtil.randomStr(20));
        payRequestVO.setPackageStr("prepay_id=" + ((unifiedOrderResult != null) ? unifiedOrderResult.getPrepay_id() : "0123456789"));
        payRequestVO.setPaySign(ValueGeneratorUtil.getSign(payRequestVO));
        map.remove("unifiedOrderResult");
        map.put("payRequestVO", payRequestVO);
        return map;
    }

    public boolean updatePayState(String notifyResult) {
        PayResultNotifyVO payResultNotifyVO = (PayResultNotifyVO) XmlUtil.xmlStr2Object(notifyResult, PayResultNotifyVO.class);
        String out_trade_no = payResultNotifyVO.getOut_trade_no();
        Date date = ValueGeneratorUtil.str2Date(payResultNotifyVO.getTime_end(), ValueGeneratorUtil.DATE_FORMAT_PATTERN);
        String time_end = ValueGeneratorUtil.date2Str(date, ValueGeneratorUtil.DATE_FORMAT_PATTERN2);
        if (donateDao.updateState(out_trade_no, time_end) == 1) {
            return true;
        }
        return false;
    }

    public void updateDonatorInfo(String out_trade_no, String comment, DonatorVO donatorVO) {
        donateDao.updateDonatorInfo(out_trade_no, comment, donatorVO);
    }

    public boolean updateRaisedFund(String itemId, String out_trade_no) {
        if (itemDao.updateRaisedFund(itemId, out_trade_no) == 1) {
            return true;
        }
        return false;
    }

    public String createCer(String out_trade_no) {
        DonatePO donatePO = donateDao.get(out_trade_no);
        String name = donatePO.getTrue_name();
        if (name != null && !"匿名".equals(name) && !"".equals(name)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("out_trade_no", out_trade_no);
            map.put("name", name);
            map.put("money", this.formatNumber(donatePO.getTotal_fee()));
            map.put("date", ValueGeneratorUtil.date2Str(donatePO.getTime_end(),
                    ValueGeneratorUtil.DATE_FORMAT_PATTERN3));
            DonateCertificateUtil.drawTextInImg(map);
            return donatePO.getOut_trade_no();
        }
        return null;
    }

    public DonatePO getDonateInfo(String out_trade_no) {
        return donateDao.get(out_trade_no);
    }

    private String formatNumber(double number) {
        return new DecimalFormat("#########0.00").format(number);
    }

}
