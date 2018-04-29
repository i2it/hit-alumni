package site.liuming.hitef.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据对象，对应数据库表 hitef_donate_record
 *
 * @author liuming
 * @date 2017/11/18 14:35
 */
@Entity(name = "hitef_donate_record")
public class DonateRecordDO implements Serializable {

    @Id
    @Column(length = 32, nullable = false)
    private String outTradeNo;
    @Column(length = 128, nullable = false)
    private String openId;
    private Integer fundItemId;
    @Column(length = 128, nullable = false)
    private String fundItemName;
    @Column(precision = 10, scale = 2, nullable = false)
    private Double totalFee;
    @Column(length = 10, nullable = false)
    private String origin;
    @Column(nullable = false)
    private Date timeEnd;
    @Column(nullable = false)
    private Integer state;
    @Column(length = 1000)
    private String comment;
    @Column(length = 20)
    private String trueName;
    @Column(length = 20)
    private String phone;
    @Column(length = 4)
    private String entryYear;
    @Column(length = 30)
    private String major;
    @Column(length = 200)
    private String mailAddr;
    @Column(length = 150)
    private String company;
    @Column(length = 100)
    private String job;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getFundItemId() {
        return fundItemId;
    }

    public void setFundItemId(Integer fundItemId) {
        this.fundItemId = fundItemId;
    }

    public String getFundItemName() {
        return fundItemName;
    }

    public void setFundItemName(String fundItemName) {
        this.fundItemName = fundItemName;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(String entryYear) {
        this.entryYear = entryYear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMailAddr() {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr) {
        this.mailAddr = mailAddr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}
