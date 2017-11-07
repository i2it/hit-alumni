package net.i2it.hit.hitef.service.function;

import net.i2it.hit.hitef.constant.ConfigConsts;
import net.i2it.hit.hitef.domain.PrepayInfoVO;
import net.i2it.hit.hitef.domain.SimpleOrderInfoVO;
import net.i2it.hit.hitef.domain.api.request.UnifiedOrderInfoVO;
import net.i2it.hit.hitef.util.ValueGeneratorUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义微信支付相关处理操作
 *
 * @author liuming
 */
public class UnifiedOrder {

    /**
     * UnifiedOrderVO实体类的实例化和参数赋值，用于生成微信支付统一下单接口的所需的请求参数
     *
     * @param openid
     * @param simpleOrderInfo
     * @return UnifiedOrderVO实例对象对应的xml格式的字符串
     */
    public Map<String, Object> getUnifiedOrderInfo(String openid, SimpleOrderInfoVO simpleOrderInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        UnifiedOrderInfoVO orderInfo = new UnifiedOrderInfoVO();
        orderInfo.setOpenid(openid);
        orderInfo.setNonce_str(ValueGeneratorUtil.randomStr(10));
        orderInfo.setBody(simpleOrderInfo.getItemBody());
        orderInfo.setDetail(simpleOrderInfo.getItemDetail());
        //统一下单接口中支付金额的单位为分，∴需要×100
        orderInfo.setTotal_fee((int) (simpleOrderInfo.getItemMoney() * 100));
        // 借助于日期实现的字段
        Date dateTime = new Date();
        map.put("time_end", ValueGeneratorUtil.date2Str(dateTime, ValueGeneratorUtil.DATE_FORMAT_PATTERN2));
        orderInfo.setOut_trade_no(this.getOut_trade_no(dateTime));
        orderInfo.setTime_start(ValueGeneratorUtil.date2Str(dateTime, ValueGeneratorUtil.DATE_FORMAT_PATTERN));
        orderInfo.setTime_expire(ValueGeneratorUtil.date2Str(new Date(dateTime.getTime() + 10 * 60 * 1000), ValueGeneratorUtil.DATE_FORMAT_PATTERN));// 订单失效时间：10分钟
        orderInfo.setDevice_info(simpleOrderInfo.getOrigin());
        orderInfo.setNotify_url(ConfigConsts.getServer_domain_url() + "/wechat/donate/notify");
        // 最后一步才是设置sign
        orderInfo.setSign(ValueGeneratorUtil.getSign(orderInfo));
        map.put("order_info", orderInfo);
        return map;
    }

    public Map<String, Object> getUnifiedOrderInfo(String openid, PrepayInfoVO prepayInfoVO) {
        Map<String, Object> map = new HashMap<String, Object>();
        UnifiedOrderInfoVO orderInfo = new UnifiedOrderInfoVO();
        orderInfo.setOpenid(openid);
        orderInfo.setNonce_str(ValueGeneratorUtil.randomStr(10));
        orderInfo.setBody(prepayInfoVO.getName());
        orderInfo.setDetail(prepayInfoVO.getId()+"");
        //统一下单接口中支付金额的单位为分，∴需要×100
        orderInfo.setTotal_fee((int) (prepayInfoVO.getMoney() * 100));
        // 借助于日期实现的字段
        Date dateTime = new Date();
        map.put("time_end", ValueGeneratorUtil.date2Str(dateTime, ValueGeneratorUtil.DATE_FORMAT_PATTERN2));
        orderInfo.setOut_trade_no(this.getOut_trade_no(dateTime));
        orderInfo.setTime_start(ValueGeneratorUtil.date2Str(dateTime, ValueGeneratorUtil.DATE_FORMAT_PATTERN));
        orderInfo.setTime_expire(ValueGeneratorUtil.date2Str(new Date(dateTime.getTime() + 10 * 60 * 1000), ValueGeneratorUtil.DATE_FORMAT_PATTERN));// 订单失效时间：10分钟
        orderInfo.setDevice_info("web");
        orderInfo.setNotify_url(ConfigConsts.getServer_domain_url() + "/wechat/hitef/donate/resultNofity");
        // 最后一步才是设置sign
        orderInfo.setSign(ValueGeneratorUtil.getSign(orderInfo));
        map.put("order_info", orderInfo);
        return map;
    }

    // 自定义订单id的生成规则
    private String getOut_trade_no(Date dateTime) {
        String str = ValueGeneratorUtil.date2Str(dateTime, ValueGeneratorUtil.DATE_FORMAT_PATTERN);
        // 添加11个随机数，使得订单号为25位字符
        str = str + ValueGeneratorUtil.randomStr(11);
        return str;
    }

}
