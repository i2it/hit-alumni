package net.i2it.hit.hitef.service;

import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.hit.hitef.constant.DatePatternConsts;
import net.i2it.hit.hitef.dao.DonateDao;
import net.i2it.hit.hitef.domain.DonateRecordDO;
import net.i2it.hit.hitef.domain.DonatorVO;
import net.i2it.hit.hitef.domain.PrepayInfoVO;
import net.i2it.hit.hitef.domain.api.request.PayRequestVO;
import net.i2it.hit.hitef.domain.api.request.UnifiedOrderInfoBO;
import net.i2it.hit.hitef.domain.api.response.PayResultNotifyVO;
import net.i2it.hit.hitef.domain.api.response.UnifiedOrderResultVO;
import net.i2it.hit.hitef.domain.api.response.WebAccessTokenVO;
import net.i2it.hit.hitef.processor.ApiResponseProcessor;
import net.i2it.hit.hitef.processor.UnifiedOrderProcessor;
import net.i2it.hit.hitef.util.CertificateUtils;
import net.i2it.wechat.util.AlgorithmUtils;
import net.i2it.wechat.util.common.DateUtils;
import net.i2it.wechat.util.common.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理前端控制器传来的参数，进行业务分发和处理
 *
 * @author liuming
 * @date 2017/11/18 20:44
 */
@Service
public class DonateService {

    private static final Logger logger = LoggerFactory.getLogger(DonateService.class);

    @Autowired
    private AppConfigProperties appConfigProperties;
    @Autowired
    private ApiResponseProcessor apiResponseProcessor;
    @Autowired
    private UnifiedOrderProcessor unifiedOrderProcessor;
    @Autowired
    private DonateDao donateDao;

    /**
     * 主要负责用于构建生成网页端js发起支付请求时需要数据的承载结构 PayRequestVO
     *
     * @param code
     * @param prepayInfoVO
     * @return Map，参数 payRequestVO 为 PayRequestVO实例对象，参数 out_trade_no 为 下单编号
     */
    public Map<String, Object> getPayRequestInfo(String code, PrepayInfoVO prepayInfoVO) {
        if (prepayInfoVO != null) {
            Map<String, Object> map = this.getUnifiedOrderResult(code, prepayInfoVO);
            UnifiedOrderResultVO unifiedOrderResultVO = (UnifiedOrderResultVO) map.get("unified_order_result");
            PayRequestVO payRequestVO = new PayRequestVO();
            payRequestVO.setAppId(appConfigProperties.getAppId());
            payRequestVO.setTimeStamp(System.currentTimeMillis() / 10 + "");
            payRequestVO.setNonceStr(AlgorithmUtils.randomStr(20));
            payRequestVO.setPackageStr("prepay_id=" + ((unifiedOrderResultVO != null) ? unifiedOrderResultVO.getPrepay_id() : "0123456789"));
            try {
                payRequestVO.setPaySign(AlgorithmUtils.getSign(payRequestVO, appConfigProperties.getApiSecret()));
            } catch (Exception e) {
                logger.info("计算统一下单中参数 签名 出错");
            }
            map.remove("unified_order_result");
            map.put("payRequestVO", payRequestVO);
            return map;
        }
        return new HashMap<String, Object>();
    }

    /**
     * @param code
     * @param prepayInfoVO
     * @return Map，里面有 out_trade_no 为下单编号，unified_order_result 为统一下单的结果对象
     */
    private Map<String, Object> getUnifiedOrderResult(String code, PrepayInfoVO prepayInfoVO) {
        try {
            //获取页面access_token
            WebAccessTokenVO webAccessTokenVO = apiResponseProcessor.getWebAccessToken(code);
            if (webAccessTokenVO != null) {
                //获取统一下单的提交参数 其中 得到的对象中的金额是以单位分记的
                UnifiedOrderInfoBO unifiedOrderInfoBO = unifiedOrderProcessor.getUnifiedOrderInfo(webAccessTokenVO.getOpenid(), prepayInfoVO);
                //将统一下单接口接收的参数转为字符串类型的xml
                String unifiedOrderXmlStr = XmlUtils.object2XmlStr(unifiedOrderInfoBO);
                //获取统一下单结果
                UnifiedOrderResultVO unifiedOrderResult = apiResponseProcessor.getUnifiedOrderResult(unifiedOrderXmlStr);
                double totalFee = prepayInfoVO.getMoney();//这个对象中的金额是以单位元记的
                Date orderCreatedTime = DateUtils.str2Date(unifiedOrderInfoBO.getTime_start(), DatePatternConsts.SIMPLE_DATE_PATTERN);
                DonateRecordDO donateRecordSaved = donateDao.save(this.create(unifiedOrderInfoBO, totalFee, orderCreatedTime));
                // 持久化下单信息
                if (unifiedOrderResult != null && donateRecordSaved != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("out_trade_no", donateRecordSaved.getOutTradeNo());
                    map.put("unified_order_result", unifiedOrderResult);
                    return map;
                }
            }
        } catch (Exception e) {
            logger.info("统一下单过程出错啦");
        }
        return null;
    }

    private DonateRecordDO create(UnifiedOrderInfoBO unifiedOrderInfoBO, double totalFee, Date timeEnd) {
        DonateRecordDO donateRecordDO = new DonateRecordDO();
        donateRecordDO.setOutTradeNo(unifiedOrderInfoBO.getOut_trade_no());
        donateRecordDO.setOpenId(unifiedOrderInfoBO.getOpenid());
        donateRecordDO.setFundItemId(Integer.parseInt(unifiedOrderInfoBO.getDetail()));
        donateRecordDO.setFundItemName(unifiedOrderInfoBO.getBody());
        donateRecordDO.setTotalFee(totalFee);
        donateRecordDO.setOrigin(unifiedOrderInfoBO.getDevice_info());
        donateRecordDO.setTimeEnd(timeEnd);
        donateRecordDO.setState(0);
        return donateRecordDO;
    }

    /**
     * 处理用户支付成功后，微信付服务器发过来的支付结果通知
     *
     * @param notifyResult
     * @return 如果与统一下单时的信息一致，返回true，否则返回false
     */
    public boolean updatePayState(String notifyResult) {
        try {
            PayResultNotifyVO payResultNotifyVO = (PayResultNotifyVO) XmlUtils.xmlStr2Object(notifyResult, PayResultNotifyVO.class);
            String out_trade_no = payResultNotifyVO.getOut_trade_no();
            Date date = DateUtils.str2Date(payResultNotifyVO.getTime_end(), DatePatternConsts.SIMPLE_DATE_PATTERN);
            String time_end = DateUtils.date2Str(date, DatePatternConsts.COMMON_DATE_PATTERN);
            double total_fee = Double.parseDouble(payResultNotifyVO.getTotal_fee()) / 100;
            DonateRecordDO donateRecordDO = donateDao.findByOutTradeNoAndTotalFee(out_trade_no, total_fee);
            if (donateRecordDO != null) {
                donateDao.updateStateAndTimeEnd(out_trade_no, time_end);
                return true;
            }
        } catch (JAXBException e) {
            logger.info("处理用户成功支付之后的微信服务器通知消息 出现了异常");
        }
        return false;
    }

    // 更新捐赠者的信息
    public void updateDonatorInfo(String out_trade_no, String comment, DonatorVO donatorVO) {
        donateDao.updateDonatorInfo(out_trade_no, comment, donatorVO);
    }

    /**
     * 创建捐助者的证书
     *
     * @param outTradeNo 商户自定义捐助记录ID
     * @return
     */
    public String createCertificate(String outTradeNo) {
        DonateRecordDO donateRecordDO = donateDao.findOne(outTradeNo);
        String name = donateRecordDO.getTrueName();
        if (name != null && !"匿名".equals(name) && !"".equals(name)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("certificatePath", appConfigProperties.getCertificationPath());
            map.put("out_trade_no", outTradeNo);
            map.put("name", name);
            map.put("money", this.formatNumber(donateRecordDO.getTotalFee()));
            map.put("date", DateUtils.date2Str(donateRecordDO.getTimeEnd(),
                    DatePatternConsts.LOCAL_DATE_WITHOUT_TIME_PATTERN));
            CertificateUtils.drawTextInImg(map);
            return donateRecordDO.getOutTradeNo();
        }
        return null;
    }

    private String formatNumber(double number) {
        return new DecimalFormat("#########0.00").format(number);
    }

    public DonateRecordDO getDonateInfo(String out_trade_no) {
        return donateDao.findOne(out_trade_no);
    }

    /**
     * 分页，每页20条记录
     *
     * @param pageIndex 页索引，从1开始
     * @return
     */
    public List<DonateRecordDO> getDonateRecordByPage(int pageIndex) {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "out_trade_no");
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(pageIndex - 1, 20, sort);
        Page<DonateRecordDO> page = donateDao.findAll(pageable);
        return page.getContent();
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donateCount", donateDao.countSuccessDonateRecord());
        map.put("fundItemStat", donateDao.countFundItemInfo());
        return map;
    }

}