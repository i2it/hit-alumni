package site.liuming.hitef.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuming.hitef.dao.FundItemDAO;
import site.liuming.hitef.domain.FundItemDO;

import java.util.List;

@Service
public class FundInfoService {

    @Autowired
    private FundItemDAO fundItemDAO;

    public List<FundItemDO> getNormalFundItems() {
        return fundItemDAO.findByStatus(1);
    }

    public FundItemDO getFundItemById(int id) {
        return fundItemDAO.findById(id);
    }

    public List<FundItemDO> getSchoolNormalFundItems() {
        return fundItemDAO.findByTypeAndStatus(0, 1);
    }

    public List<FundItemDO> getAcademyNormalFundItems() {
        return fundItemDAO.findByTypeAndStatus(1, 1);
    }

}
