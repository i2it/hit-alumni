package site.liuming.hitef.domain;

/**
 * 每个基金项目的相关统计量类：捐款次数、捐款总额等
 * Created by liuming on 2017/6/20.
 */
public class FundItemStatVO {

    private int fundItemId;
    private String fundItemName;
    private String totalCount;
    private double totalMoney;

    public int getFundItemId() {
        return fundItemId;
    }

    public void setFundItemId(int fundItemId) {
        this.fundItemId = fundItemId;
    }

    public String getFundItemName() {
        return fundItemName;
    }

    public void setFundItemName(String fundItemName) {
        this.fundItemName = fundItemName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "FundItemStatVO{" +
                "fundItemId=" + fundItemId +
                ", fundItemName='" + fundItemName + '\'' +
                ", totalCount='" + totalCount + '\'' +
                ", totalMoney=" + totalMoney +
                '}';
    }

}
