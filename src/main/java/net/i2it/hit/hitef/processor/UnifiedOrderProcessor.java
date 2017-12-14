package net.i2it.hit.hitef.processor;

import net.i2it.hit.hitef.constant.AppConfigProperties;
import net.i2it.hit.hitef.constant.DatePatternConsts;
import net.i2it.hit.hitef.domain.PrepayInfoVO;
import net.i2it.hit.hitef.domain.api.request.UnifiedOrderInfoBO;
import net.i2it.wechat.util.AlgorithmUtils;
import net.i2it.wechat.util.common.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付 统一下单 请求参数组装
 *
 * @author liuming
 * @date 2017/11/16 20:41
 */
@Component
public class UnifiedOrderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedOrderProcessor.class);

    @Autowired
    private AppConfigProperties appConfigProperties;

    private final String notifyUrl = appConfigProperties.getServerDomainUrl() + appConfigProperties.getContextPath() + "/donate/resultNofity";

    /**
     * @param openId
     * @param prepayInfoVO
     * @return
     */
    public UnifiedOrderInfoBO getUnifiedOrderInfo(String openId, PrepayInfoVO prepayInfoVO) {
        Map<String, Object> map = new HashMap<String, Object>();
        Date dateTime = new Date();
        UnifiedOrderInfoBO unifiedOrderInfoBO = new UnifiedOrderInfoBO.Builder()
                .appId(appConfigProperties.getAppId())
                .mchId(appConfigProperties.getMchId())
                .openId(openId)
                .nonceStr(AlgorithmUtils.randomStr(10))
                .body(prepayInfoVO.getName())
                .detail(prepayInfoVO.getId() + "")
                .totalFee((int) (prepayInfoVO.getMoney() * 100)) //统一下单接口中支付金额的单位为分，∴需要×100
                .outTradeNo(this.getOutTradeNo(dateTime))
                .timeStart(DateUtils.date2Str(dateTime, DatePatternConsts.SIMPLE_DATE_PATTERN))
                //订单失效时间：10分钟
                .timeExpire(this.getTimeExpire(dateTime))
                .deviceInfo("web")
                .notifyUrl(notifyUrl)
                .build();

        // 最后一步才是设置sign
        try {
            unifiedOrderInfoBO.setSign(AlgorithmUtils.getSign(unifiedOrderInfoBO, appConfigProperties.getApiSecret()));
        } catch (Exception e) {
            logger.error("微信支付 统一下单 签名计算出现异常，信息：{}", e.getStackTrace());
        }
        return unifiedOrderInfoBO;
    }

    // 自定义订单id的生成规则
    private String getOutTradeNo(Date dateTime) {
        String str = DateUtils.date2Str(dateTime, DatePatternConsts.SIMPLE_DATE_PATTERN);
        // 添加11个随机数，使得订单号为25位字符
        str = str + AlgorithmUtils.randomStr(11);
        return str;
    }

    //根据订单生成时间设置订单失效时间，并转为字符串格式的日期
    private String getTimeExpire(Date timeStart) {
        long newTimeOfMillisecond = timeStart.getTime() + 10 * 60 * 1000;//10分钟后订单失效
        Date expireDate = new Date(newTimeOfMillisecond);
        return DateUtils.date2Str(expireDate, DatePatternConsts.SIMPLE_DATE_PATTERN);
    }

}
