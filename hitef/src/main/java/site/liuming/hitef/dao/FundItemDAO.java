package site.liuming.hitef.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import site.liuming.hitef.domain.FundItemDO;

import java.util.List;

@Repository
public interface FundItemDAO extends CrudRepository<FundItemDO, Integer> {

    /**
     * 根据指定的基金状态，获取相应的基金列表，{@link FundItemDO}的<code>status</code>注释
     */
    List<FundItemDO> findByStatus(int status);


    /**
     * 根据基金的id获取基金信息
     *
     * @param id
     * @return
     */
    FundItemDO findById(int id);

    /**
     * 根据基金的类型和状态获取基金列表
     *
     * @param type
     * @param status
     * @return
     */
    List<FundItemDO> findByTypeAndStatus(int type, int status);


}

