package site.liuming.hitef.dao;

import site.liuming.hitef.domain.DonateRecordDO;
import site.liuming.hitef.domain.ContributorVO;
import site.liuming.hitef.domain.FundItemStatVO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuming
 * @date 2017/11/18 20:45
 */
@Repository
public interface DonateDAO extends PagingAndSortingRepository<DonateRecordDO, String> {

    /**
     * 同时根据商户订单编号、支付费用查询是否存在匹配的支付记录
     *
     * @param outTradeNo 商户订单编号
     * @param totalFee   支付费用
     * @return 匹配到的支付记录
     */
    DonateRecordDO findByOutTradeNoAndTotalFee(String outTradeNo, double totalFee);

    /**
     * 统一下单后成功完成支付，更改支付记录的状态和完成时间
     *
     * @param outTradeNo 商户订单编号
     * @param timeEnd    支付订单完成时间
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE hitef_donate_record SET state=1,time_end=:timeEnd WHERE out_trade_no=:outTradeNo")
    void updateStateAndTimeEnd(@Param("outTradeNo") String outTradeNo, @Param("timeEnd") String timeEnd);

    /**
     * 更新支付记录的支付人信息（捐赠者）
     *
     * @param outTradeNo 商户字支付编号
     * @param comment    捐赠留言
     * @param contributorVO  捐赠者信息
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE hitef_donate_record SET comment=:comment," +
            "true_name=:vo.trueName,phone=:vo.phone,entry_year=:vo.entryYear,major=:vo.major,mail_addr=:vo.mailAddr,company=:vo.company,job=:vo.job " +
            "WHERE out_trade_no=:outTradeNo")
    void updateContributorInfo(@Param("outTradeNo") String outTradeNo, @Param("comment") String comment, @Param("vo") ContributorVO contributorVO);

    /**
     * 完成的支付记录数
     *
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM hitef_donate_record WHERE state=1")
    long countSuccessDonateRecord();

    /**
     * 对每个捐赠项目的捐款额进行统计
     *
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT t2.fundItemId,t1.`name` AS fundItemName,t2.totalCount,t2.totalMoney FROM " +
            "(SELECT fund_item_id AS fundItemId,SUM(total_fee) AS totalMoney,COUNT(*) AS totalCount FROM hitef_donate_record WHERE state=1 GROUP BY fund_item_id) AS t2 " +
            "LEFT JOIN hitef_fund_item AS t1 " +
            "ON t1.id=t2.fundItemId ORDER BY t2.totalCount DESC,t2.totalMoney DESC;")
    List<FundItemStatVO> countFundItemInfo();

}
