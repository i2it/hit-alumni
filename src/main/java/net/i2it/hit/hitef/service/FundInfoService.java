package net.i2it.hit.hitef.service;

import net.i2it.hit.hitef.dao.FundItemDAO;
import net.i2it.hit.hitef.domain.FundItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundInfoService {

    @Autowired
    private FundItemDAO fundItemDAO;

    public List<FundItemDO> getNormalFundItems() {
        return fundItemDAO.listAllFundItemByStatus(1);
    }

    public FundItemDO getFundItemById(int id) {
        return fundItemDAO.getById(id);
    }

    public List<FundItemDO> getSchoolNormalFundItems() {
        return fundItemDAO.listAllFundItemByTypeAndStatus(0, 1);
    }

    public List<FundItemDO> getAcademyNormalFundItems() {
        return fundItemDAO.listAllFundItemByTypeAndStatus(1, 1);
    }

}
