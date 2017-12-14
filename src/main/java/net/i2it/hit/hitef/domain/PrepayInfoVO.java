package net.i2it.hit.hitef.domain;

/**
 * 简略的支付信息类
 *
 * @author liuming
 * @date 2017/11/16 19:15
 */
public class PrepayInfoVO {

    private int id;
    private String name;
    private double money;

    public PrepayInfoVO() {
    }

    public PrepayInfoVO(int id, String name, double money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}
