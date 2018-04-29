package site.liuming.hitef.domain;

import site.liuming.hitef.domain.api.request.PayRequestVO;
import site.liuming.hitef.domain.api.response.UnifiedOrderResultDTO;

public class PrepayInfoDTO {
    private String outTradeNO;
    private UnifiedOrderResultDTO unifiedOrderResultDTO;
    private PayRequestVO payRequestVO;

    public String getOutTradeNO() {
        return outTradeNO;
    }

    public void setOutTradeNO(String outTradeNO) {
        this.outTradeNO = outTradeNO;
    }

    public UnifiedOrderResultDTO getUnifiedOrderResultDTO() {
        return unifiedOrderResultDTO;
    }

    public void setUnifiedOrderResultDTO(UnifiedOrderResultDTO unifiedOrderResultDTO) {
        this.unifiedOrderResultDTO = unifiedOrderResultDTO;
    }

    public PayRequestVO getPayRequestVO() {
        return payRequestVO;
    }

    public void setPayRequestVO(PayRequestVO payRequestVO) {
        this.payRequestVO = payRequestVO;
    }
}
