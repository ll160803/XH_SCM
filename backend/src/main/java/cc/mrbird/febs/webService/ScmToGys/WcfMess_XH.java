package cc.mrbird.febs.webService.ScmToGys;

import java.util.List;

public class WcfMess_XH {
    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<Purchase> getPurchasePlans() {
        return purchasePlans;
    }

    public void setPurchasePlans(List<Purchase> purchasePlans) {
        this.purchasePlans = purchasePlans;
    }

    /// <summary>
    /// 返回信息
    /// </summary>
    private String mess ;

    /// <summary>
    /// 是否成功调用
    /// </summary>
    private Boolean isSuccess ;

    /**
     * 返回采购订单信息
     */
    private List<Purchase> purchasePlans ;
}
