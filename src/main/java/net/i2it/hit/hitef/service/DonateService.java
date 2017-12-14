package net.i2it.hit.hitef.service;

import net.i2it.hit.hitef.constant.ConfigConsts;
import net.i2it.hit.hitef.dao.DonateDao;
import net.i2it.hit.hitef.domain.*;
import net.i2it.hit.hitef.domain.api.request.PayRequestVO;
import net.i2it.hit.hitef.domain.api.request.UnifiedOrderInfoVO;
import net.i2it.hit.hitef.domain.api.response.PayResultNotifyVO;
import net.i2it.hit.hitef.domain.api.response.UnifiedOrderResultVO;
import net.i2it.hit.hitef.domain.api.response.WebAccessTokenVO;
import net.i2it.hit.hitef.service.function.UnifiedOrder;
import net.i2it.hit.hitef.service.function.WeChatApi;
import net.i2it.hit.hitef.util.DonateCertificateUtil;
import net.i2it.hit.hitef.util.ValueGeneratorUtil;
import net.i2it.hit.hitef.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理前端控制器传来的参数，进行业务分发和处理
 */
@Service
public class DonateService {

    @Autowired
    private DonateDao donateDao;

    private Map<String, Object> getUnifiedOrderResult(String code, PrepayInfoVO prepayInfoVO) {
        WeChatApi weChatApi = new WeChatApi();
        //获取页面access_token
        WebAccessTokenVO webAccessTokenVO = weChatApi.getWebAccessToken(code);
        if (webAccessTokenVO != null) {
            //获取统一下单的提交参数
            Map<String, Object> map = new UnifiedOrder().getUnifiedOrderInfo(webAccessTokenVO.getOpenid(), prepayInfoVO);
            //将统一下单接口接收的参数转为字符串类型的xml
            UnifiedOrderInfoVO unifiedOrderInfoVO = (UnifiedOrderInfoVO) map.get("order_info");//得到的对象中的金额是以单位分记的
            String unifiedOrderXmlStr = XmlUtil.object2XmlStr(unifiedOrderInfoVO);
            //获取统一下单结果
            UnifiedOrderResultVO unifiedOrderResult = weChatApi.getUnifiedOrderResult(unifiedOrderXmlStr);
            double totalFee = prepayInfoVO.getMoney();//这个对象中的金额是以单位元记的
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
     * 主要负责用于构建生成网页端js发起支付请求时需要数据的承载结构 PayRequestVO
     */
    public Map<String, Object> getPayRequestInfo(String code, PrepayInfoVO prepayInfoVO) {
        Map<String, Object> map = null;
        if (prepayInfoVO != null) {
            map = this.getUnifiedOrderResult(code, prepayInfoVO);
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
        double total_fee = Double.parseDouble(payResultNotifyVO.getTotal_fee()) / 100;
        if (donateDao.updateState(out_trade_no, total_fee, time_end) == 1) {
            return true;
        }
        return false;
    }

    public void updateDonatorInfo(String out_trade_no, String comment, DonatorVO donatorVO) {
        donateDao.updateDonatorInfo(out_trade_no, comment, donatorVO);
    }

    public String createCer(String out_trade_no) {
        DonateRecordDO donateRecordDO = donateDao.get(out_trade_no);
        String name = donateRecordDO.getTrueName();
        if (name != null && !"匿名".equals(name) && !"".equals(name)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("out_trade_no", out_trade_no);
            map.put("name", name);
            map.put("money", this.formatNumber(donateRecordDO.getTotalFee()));
            map.put("date", ValueGeneratorUtil.date2Str(donateRecordDO.getTimeEnd(),
                    ValueGeneratorUtil.DATE_FORMAT_PATTERN3));
            DonateCertificateUtil.drawTextInImg(map);
            return donateRecordDO.getOutTradeNo();
        }
        return null;
    }

    public DonateRecordDO getDonateInfo(String out_trade_no) {
        return donateDao.get(out_trade_no);
    }

    private String formatNumber(double number) {
        return new DecimalFormat("#########0.00").format(number);
    }

    public PrepayInfoVO getPrepayVO(String itemInfo) {
        String[] arr = itemInfo.split("__");
        if (arr.length == 3) {
            return new PrepayInfoVO(Integer.parseInt(arr[0]), arr[1], Double.parseDouble(arr[2]));
        }
        return null;
    }

    public List<SimpleDonateVO> getPageDonateInfos(Date date) {
        return donateDao.getSucecessList(date, 20);
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donateCount", donateDao.donateCount());
        map.put("fundItemStat", donateDao.getAllFundItemStat());
        return map;
    }

}