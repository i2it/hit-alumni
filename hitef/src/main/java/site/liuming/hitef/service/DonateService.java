package site.liuming.hitef.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.liuming.hitef.constant.AppProperty;
import site.liuming.hitef.constant.DatePatternConsts;
import site.liuming.hitef.dao.DonateDAO;
import site.liuming.hitef.domain.ContributorVO;
import site.liuming.hitef.domain.DonateRecordDO;
import site.liuming.hitef.domain.PrepayInfoDTO;
import site.liuming.hitef.domain.PrepayInfoVO;
import site.liuming.hitef.domain.api.request.PayRequestVO;
import site.liuming.hitef.domain.api.request.UnifiedOrderInfoBO;
import site.liuming.hitef.domain.api.response.PayResultNotifyVO;
import site.liuming.hitef.domain.api.response.UnifiedOrderResultDTO;
import site.liuming.hitef.domain.api.response.WebAccessTokenDTO;
import site.liuming.hitef.manager.ApiResponseManager;
import site.liuming.hitef.manager.CertificateManager;
import site.liuming.wechat.common.Converter;
import site.liuming.wechat.common.Generator;
import site.liuming.wechat.common.util.XmlUtils;

import javax.xml.bind.JAXBException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
    private AppProperty appProperty;
    @Autowired
    private ApiResponseManager apiResponseManager;
    @Autowired
    private DonateDAO donateDAO;

    /**
     * 主要负责用于构建生成网页端js发起支付请求时需要数据的承载结构 PayRequestVO
     *
     * @param code
     * @param prepayInfoVO 封装了用户提交的订单信息，{@link PrepayInfoVO}
     * @return {@link PrepayInfoDTO}的实例对象，参数 payRequestVO 为 PayRequestVO实例对象，参数 out_trade_no 为 下单编号
     */
    public PrepayInfoDTO getPayRequestInfo(String code, PrepayInfoVO prepayInfoVO) {
        if (prepayInfoVO != null) {
            PrepayInfoDTO prepayInfoDTO = this.getUnifiedOrderResult(code, prepayInfoVO);
            if (prepayInfoDTO != null) {
                UnifiedOrderResultDTO unifiedOrderResultDTO = prepayInfoDTO.getUnifiedOrderResultDTO();
                PayRequestVO payRequestVO = new PayRequestVO();
                payRequestVO.setAppId(appProperty.getAppId());
                payRequestVO.setTimeStamp(System.currentTimeMillis() / 10 + "");
                payRequestVO.setNonceStr(Generator.generateRandomStr(20));
                payRequestVO.setPackageStr("prepay_id=" + (unifiedOrderResultDTO != null ? unifiedOrderResultDTO.getPrepay_id() : "0123456789"));
                try {
                    // 【6】、生成支付请求中的签名参数
                    payRequestVO.setPaySign(Generator.generateSign(payRequestVO, appProperty.getApiSecret()));
                } catch (Exception e) {
                    logger.info("计算统一下单中参数 签名 出错");
                }
                prepayInfoDTO.setPayRequestVO(payRequestVO);
                return prepayInfoDTO;
            }
        }
        return null;
    }

    /**
     * 统一下单
     *
     * @param code
     * @param prepayInfoVO
     * @return Map，里面有 out_trade_no 为下单编号，unified_order_result 为统一下单的结果对象
     */
    private PrepayInfoDTO getUnifiedOrderResult(String code, PrepayInfoVO prepayInfoVO) {
        try {
            //【2】、微信服务器传递的code参数，获取页面access_token
            WebAccessTokenDTO webAccessTokenDTO = apiResponseManager.getWebAccessToken(code);
            if (webAccessTokenDTO != null) {
                //构造统一下单所需参数的封装对象
                UnifiedOrderInfoBO unifiedOrderInfoBO = this.getUnifiedOrderInfo(webAccessTokenDTO.getOpenid(), prepayInfoVO);
                //将统一下单接口的请求参数转为字符串类型的xml
                String unifiedOrderXmlStr = XmlUtils.object2XmlStr(unifiedOrderInfoBO);
                //【4】、发起统一下单，获取统一下单结果
                UnifiedOrderResultDTO unifiedOrderResult = apiResponseManager.getUnifiedOrderResult(unifiedOrderXmlStr);
                //这个对象中的金额是以单位元记的，即用户传来的捐助金额单位为元
                double totalFee = prepayInfoVO.getMoney();
                Date orderCreatedTime = Converter.convertStringToDate(unifiedOrderInfoBO.getTime_start(), DatePatternConsts.SIMPLE_DATE_PATTERN);
                DonateRecordDO donateRecordSaved = donateDAO.save(this.create(unifiedOrderInfoBO, totalFee, orderCreatedTime));
                // 【5】、持久化下单信息
                if (unifiedOrderResult != null && donateRecordSaved != null) {
                    PrepayInfoDTO prepayInfoDTO = new PrepayInfoDTO();
                    prepayInfoDTO.setOutTradeNO(donateRecordSaved.getOutTradeNo());
                    prepayInfoDTO.setUnifiedOrderResultDTO(unifiedOrderResult);
                    return prepayInfoDTO;
                }
            }
        } catch (Exception e) {
            logger.info("统一下单过程出错啦，信息{}", e.getMessage());
        }
        return null;
    }

    private UnifiedOrderInfoBO getUnifiedOrderInfo(String openId, PrepayInfoVO prepayInfoVO) {
        Date dateTime = new Date();
        UnifiedOrderInfoBO unifiedOrderInfoBO = new UnifiedOrderInfoBO.Builder()
                .appId(appProperty.getAppId())
                .mchId(appProperty.getMchId())
                .openId(openId)
                .nonceStr(Generator.generateRandomStr(10))
                .body(prepayInfoVO.getName())
                .detail(prepayInfoVO.getId() + "")
                .totalFee((int) (prepayInfoVO.getMoney() * 100)) //统一下单接口中支付金额的单位为分，∴需要×100
                .outTradeNo(this.getOutTradeNo(dateTime))
                .timeStart(Converter.convertDateToString(dateTime, DatePatternConsts.SIMPLE_DATE_PATTERN))
                //订单失效时间：10分钟
                .timeExpire(this.getTimeExpire(dateTime))
                .deviceInfo("web")
                .notifyUrl(appProperty.getNotifyUrl())
                .build();
        // 最后一步才是设置sign
        try {
            //【3】、生成统一下单所需的签名参数
            unifiedOrderInfoBO.setSign(Generator.generateSign(unifiedOrderInfoBO, appProperty.getApiSecret()));
        } catch (Exception e) {
            logger.error("微信支付 统一下单 签名计算出现异常，信息：{}", e.getMessage());
        }
        return unifiedOrderInfoBO;
    }

    private String getOutTradeNo(Date dateTime) {
        // 自定义订单id的生成规则

        String str = Converter.convertDateToString(dateTime, DatePatternConsts.SIMPLE_DATE_PATTERN);
        str = str + Generator.generateRandomStr(11);
        return str;
    }

    private String getTimeExpire(Date timeStart) {
        //根据订单生成时间设置订单失效时间，并转为字符串格式的日期

        //10分钟后订单失效
        long newTimeOfMillisecond = timeStart.getTime() + 10 * 60 * 1000;
        Date expireDate = new Date(newTimeOfMillisecond);
        return Converter.convertDateToString(expireDate, DatePatternConsts.SIMPLE_DATE_PATTERN);
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
            Date date = Converter.convertStringToDate(payResultNotifyVO.getTime_end(), DatePatternConsts.SIMPLE_DATE_PATTERN);
            String time_end = Converter.convertDateToString(date, DatePatternConsts.COMMON_DATE_PATTERN);
            double total_fee = Double.parseDouble(payResultNotifyVO.getTotal_fee()) / 100;
            DonateRecordDO donateRecordDO = donateDAO.findByOutTradeNoAndTotalFee(out_trade_no, total_fee);
            if (donateRecordDO != null) {
                donateDAO.updateStateAndTimeEnd(out_trade_no, time_end);
                return true;
            }
        } catch (JAXBException e) {
            logger.info("处理用户成功支付之后的微信服务器通知消息 出现了异常，信息{}", e.getMessage());
        } catch (ParseException e) {
            logger.info("字符串转日期出现了异常，信息{}", e.getMessage());
        }
        return false;
    }

    // 更新捐赠者的信息
    public void updateContributorInfo(String out_trade_no, String comment, ContributorVO contributorVO) {
        donateDAO.updateContributorInfo(out_trade_no, comment, contributorVO);
    }

    /**
     * 创建捐助者的证书，如果生成证书图片成功，返回证书的记录ID，否则，返回null
     *
     * @param outTradeNo 商户自定义捐助记录ID
     * @return
     */
    public String createCertificate(String outTradeNo) {
        DonateRecordDO donateRecordDO = donateDAO.findOne(outTradeNo);
        String name = donateRecordDO.getTrueName();
        if (name != null && !"匿名".equals(name) && !"".equals(name)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("certificatePath", appProperty.getCertificationPath());
            map.put("out_trade_no", outTradeNo);
            map.put("name", name);
            map.put("money", this.formatNumber(donateRecordDO.getTotalFee()));
            map.put("date", Converter.convertDateToString(donateRecordDO.getTimeEnd(),
                    DatePatternConsts.LOCAL_DATE_WITHOUT_TIME_PATTERN));
            CertificateManager.drawTextInImg(map);
            return donateRecordDO.getOutTradeNo();
        }
        return null;
    }

    private String formatNumber(double number) {
        return new DecimalFormat("#########0.00").format(number);
    }

    public DonateRecordDO getDonateInfo(String out_trade_no) {
        return donateDAO.findOne(out_trade_no);
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
        Page<DonateRecordDO> page = donateDAO.findAll(pageable);
        return page.getContent();
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donateCount", donateDAO.countSuccessDonateRecord());
        map.put("fundItemStat", donateDAO.countFundItemInfo());
        return map;
    }

}