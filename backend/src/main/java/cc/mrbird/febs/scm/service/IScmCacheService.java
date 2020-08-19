package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;

import java.util.List;

public interface IScmCacheService {
    /**
     * 获取最近一个月的采购订单 超期的不操作 这里是限制接口获取很早之前的采购订单
     * @return
     * @throws Exception
     */
    List<ScmBPurcharseorder> getPurcharseList() throws Exception;

    /**
     * 存储最近一个月采购订单
     * @throws Exception
     */
    void  savePurcharseList() throws Exception;
}
