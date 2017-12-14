package net.i2it.hit.hitef.domain.api.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一下单api的请求参数的封装对象
 *
 * @author liuming
 * @date 2017/11/16 19:37
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnifiedOrderInfoBO {

    private String appid;
    private String mch_id;
    private String openid;
    private String nonce_str;
    private String sign;
    private String sign_type = "MD5";
    private String body;
    private String detail;
    private String out_trade_no;
    private String fee_type = "CNY";
    private int total_fee;
    private String time_start;
    private String time_expire;
    private String device_info;
    private String trade_type = "JSAPI";
    private String notify_url;

    public UnifiedOrderInfoBO() {

    }

    private UnifiedOrderInfoBO(Builder builder) {
        this.appid = builder.appid;
        this.mch_id = builder.mch_id;
        this.openid = builder.openid;
        this.nonce_str = builder.nonce_str;
        this.sign = builder.sign;
        this.body = builder.body;
        this.detail = builder.detail;
        this.out_trade_no = builder.out_trade_no;
        this.total_fee = builder.total_fee;
        this.time_start = builder.time_start;
        this.time_expire = builder.time_expire;
        this.device_info = builder.device_info;
        this.notify_url = builder.notify_url;
    }

    public static class Builder {
        private String appid;
        private String mch_id;
        private String openid;
        private String nonce_str;
        private String sign;
        private String body;
        private String detail;
        private String out_trade_no;
        private int total_fee;
        private String time_start;
        private String time_expire;
        private String device_info;
        private String notify_url;

        public Builder appId(String appId) {
            this.appid = appId;
            return this;
        }

        public Builder mchId(String mchId) {
            this.mch_id = mchId;
            return this;
        }

        public Builder openId(String openId) {
            this.openid = openId;
            return this;
        }

        public Builder nonceStr(String nonceStr) {
            this.nonce_str = nonceStr;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder outTradeNo(String outTradeNo) {
            this.out_trade_no = outTradeNo;
            return this;
        }

        public Builder totalFee(int totalFee) {
            this.total_fee = totalFee;
            return this;
        }

        public Builder timeStart(String timeStart) {
            this.time_start = timeStart;
            return this;
        }

        public Builder timeExpire(String timeExpire) {
            this.time_expire = timeExpire;
            return this;
        }

        public Builder deviceInfo(String deviceInfo) {
            this.device_info = deviceInfo;
            return this;
        }

        public Builder notifyUrl(String notifyUrl) {
            this.notify_url = notifyUrl;
            return this;
        }

        public UnifiedOrderInfoBO build() {
            return new UnifiedOrderInfoBO(this);
        }
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

}
