package net.i2it.hit.hit_alumni.service.function;

import net.i2it.hit.hit_alumni.constant.ConfigConsts;
import net.i2it.hit.hit_alumni.entity.vo.SimpleOrderInfoVO;
import net.i2it.hit.hit_alumni.entity.vo.api.request.UnifiedOrderInfoVO;
import net.i2it.hit.hit_alumni.util.ValueGeneratorUtil;

import java.util.Date;

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
    public UnifiedOrderInfoVO getUnifiedOrderInfo(String openid, SimpleOrderInfoVO simpleOrderInfo) {
        UnifiedOrderInfoVO orderInfo = new UnifiedOrderInfoVO();
        orderInfo.setOpenid(openid);
        orderInfo.setNonce_str(ValueGeneratorUtil.randomStr(10));
        orderInfo.setBody(simpleOrderInfo.getItemName());
        orderInfo.setDetail(simpleOrderInfo.getItemDetail());
        orderInfo.setTotal_fee((int) (simpleOrderInfo.getItemMoney() * 100));
        // 借助于日期实现的字段
        Date dateTime = new Date();
        orderInfo.setOut_trade_no(this.getOut_trade_no(dateTime));
        orderInfo.setTime_start(ValueGeneratorUtil.getTime(dateTime));
        orderInfo.setTime_expire(ValueGeneratorUtil.getTime(new Date(dateTime.getTime() + 10 * 60 * 1000)));// 订单失效时间：10分钟
        orderInfo.setDevice_info(simpleOrderInfo.getOrigin());
        orderInfo.setNotify_url(ConfigConsts.SERVER_DOMAIN + "/donate/notify");
        // 最后一步才是设置sign
        orderInfo.setSign(ValueGeneratorUtil.getSign(orderInfo));
        return orderInfo;
    }

    // 自定义订单id的生成规则
    private String getOut_trade_no(Date dateTime) {
        String str = ValueGeneratorUtil.getTime(dateTime);
        // 添加11个随机数，使得订单号为25位字符
        str = str + ValueGeneratorUtil.randomStr(11);
        return str;
    }

}