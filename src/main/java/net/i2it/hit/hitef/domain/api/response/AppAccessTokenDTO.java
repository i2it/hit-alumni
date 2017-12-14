package net.i2it.hit.hitef.domain.api.response;

/**
 * 请求微信API获取access_token,得到正确结果的,对应的json格式为：{"access_token":"ACCESS_TOKEN","expires_in":7200}
 *
 * @author liuming
 * @date 2017/11/15 18:27
 */
public class AppAccessTokenDTO {

    private String access_token;
    private Integer expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

}
