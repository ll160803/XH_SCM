package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmBPurcharseorderMapper;
import cc.mrbird.febs.scm.dao.ScmBSendinfoMapper;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanDMapper;
import cc.mrbird.febs.scm.entity.*;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanMapper;
import cc.mrbird.febs.scm.service.IScmBSupplyplanService;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 供应计划 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
@Slf4j
@Service("IScmBSupplyplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBSupplyplanServiceImpl extends ServiceImpl<ScmBSupplyplanMapper, ScmBSupplyplan> implements IScmBSupplyplanService {
    @Autowired
    private ScmBPurcharseorderMapper scmBPurcharseorderMapper;

    @Autowired
    private ScmBSendinfoMapper scmBSendinfoMapper;

    @Autowired
    private ScmBSupplyplanDMapper scmBSupplyplanDMapper;


    @Override
    public IPage<ScmBSupplyplan> findScmBSupplyplans(QueryRequest request, ScmBSupplyplan scmBSupplyplan) {
        try {
            LambdaQueryWrapper<ScmBSupplyplan> queryWrapper = new LambdaQueryWrapper<>();
            if(scmBSupplyplan.getId()!=null) {
                queryWrapper.eq(ScmBSupplyplan::getId, scmBSupplyplan.getId());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getCode())) {
                queryWrapper.eq(ScmBSupplyplan::getCode, scmBSupplyplan.getCode());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getBaseId())) {
                queryWrapper.eq(ScmBSupplyplan::getBaseId, scmBSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getSendOrderCode())) {
                queryWrapper.eq(ScmBSupplyplan::getSendOrderCode, scmBSupplyplan.getSendOrderCode());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getGysaccount())) {
                queryWrapper.eq(ScmBSupplyplan::getGysaccount, scmBSupplyplan.getGysaccount());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getFphm())) {
                queryWrapper.and(wrapper -> wrapper.eq(ScmBSupplyplan::getFphm, scmBSupplyplan.getFphm()).or().eq(ScmBSupplyplan::getFphm, null
                ).or().eq(ScmBSupplyplan::getFphm, ""
                ));
            }
            if (scmBSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ScmBSupplyplan::getIsDeletemark, scmBSupplyplan.getIsDeletemark());
            }
            Page<ScmBSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean IsExistFphm(String id, String fphm,String gys) {
        int count = this.baseMapper.IsExistFphm(id, fphm,gys);
        if (count > 0) return false;
        return true;
    }

    @Override
    public IPage<ScmBSupplyplan> findSupplyplans(QueryRequest request, ScmBSupplyplan scmBSupplyplan) {
        try {
            LambdaQueryWrapper<ScmBSupplyplan> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(scmBSupplyplan.getCode())) {
                queryWrapper.eq(ScmBSupplyplan::getCode, scmBSupplyplan.getCode());
            }
            if (scmBSupplyplan.getId() != null) {
                queryWrapper.eq(ScmBSupplyplan::getId, scmBSupplyplan.getId());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getBaseId())) {
                queryWrapper.eq(ScmBSupplyplan::getBaseId, scmBSupplyplan.getBaseId());
            }
            if (scmBSupplyplan.getStatus()!=null) {
                queryWrapper.eq(ScmBSupplyplan::getStatus, scmBSupplyplan.getStatus());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getGysaccount())) {
                queryWrapper.eq(ScmBSupplyplan::getGysaccount, scmBSupplyplan.getGysaccount());
            }
            if (scmBSupplyplan.getBsartD() == "1") {
                if (StringUtils.isNotBlank(scmBSupplyplan.getFphm())) {
                    queryWrapper.and(wrapper -> wrapper.eq(ScmBSupplyplan::getFphm, scmBSupplyplan.getFphm()).or().eq(ScmBSupplyplan::getFphm, null
                    ).or().eq(ScmBSupplyplan::getFphm, ""
                    ));
                } else {
                    queryWrapper.and(wrapper -> wrapper.isNull(ScmBSupplyplan::getFphm)
                            .or().eq(ScmBSupplyplan::getFphm, ""
                            ));
                }

            } else {
                queryWrapper.and(wrapper -> wrapper.isNull(ScmBSupplyplan::getSendOrderCode).or().eq(ScmBSupplyplan::getSendOrderCode, ""
                ).or().eq(ScmBSupplyplan::getSendOrderCode, scmBSupplyplan.getSendOrderCode()));
            }
            queryWrapper.eq(ScmBSupplyplan::getBsartD, scmBSupplyplan.getBsartD());//订单类型
            if (scmBSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ScmBSupplyplan::getIsDeletemark, scmBSupplyplan.getIsDeletemark());
            }
            Page<ScmBSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) throws FebsException {
        //scmBSupplyplan.setId(UUID.randomUUID().toString());
        Long isMenge = this.baseMapper.IsOutMenge(scmBSupplyplan);
        if (isMenge != null && isMenge > 0) {
            throw new FebsException("供应计划数量超出订单数量");
        }

        scmBSupplyplan.setCreateTime(new Date());
        this.save(scmBSupplyplan);
        log.error("BsartD:" + scmBSupplyplan.getBsartD());
        String artd = scmBSupplyplan.getBsartD();
        String typear = "1";//z真是坑爹 不这么些 就是不行
        if (artd.equals(typear)) {//物资供应商才能生成送货单
            log.error("sadasdasd");
            // if(StringUtils.isNotBlank(scmBSupplyplan.getSendOrderCode())) { //从送货单引入 不生成送货单
            ScmBPurcharseorder order = scmBPurcharseorderMapper.selectById(scmBSupplyplan.getBaseId());
            ScmBSendinfo scmBSendinfo = new ScmBSendinfo();
            scmBSendinfo.setGysname(scmBSupplyplan.getGysname());
            scmBSendinfo.setIsDeletemark(1);
            scmBSendinfo.setGysaccount(scmBSupplyplan.getGysaccount());
            scmBSendinfo.setCreateTime(new Date());
            scmBSendinfo.setAmount(scmBSupplyplan.getgMenge());
            scmBSendinfo.setGyjh(scmBSupplyplan.getId());
            scmBSendinfo.setLinkPerson(scmBSupplyplan.getLinkPerson());
            scmBSendinfo.setMatnr(order.getMatnr());
            scmBSendinfo.setMoney(scmBSupplyplan.getFpjr());
            scmBSendinfo.setPrice(order.getNetpr());
            scmBSendinfo.setSendDepart(scmBSupplyplan.getSendDepart());
            scmBSendinfo.setTxz01(order.getTxz01());
            scmBSendinfo.setMseht(order.getMseht());
            scmBSendinfo.setMeins(order.getMeins());
            scmBSendinfo.setWerks(order.getWerks());
            scmBSendinfo.setWerkst(order.getWerkst());
            scmBSendinfoMapper.insert(scmBSendinfo);
        }
        //  }
       // HandleScmBSupplyD(scmBSupplyplan);//hsc 20200819 增加箱数数据
    }

private  void HandleScmBSupplyD(ScmBSupplyplan scmBSupplyplan){
        //删除已经存在的
    scmBSupplyplanDMapper.deleteByBaseId(scmBSupplyplan.getId().toString());
    /** 增加箱数的设计 */
    String str_num = scmBSupplyplan.getPkgNumber().toString();
    String gyjhId = scmBSupplyplan.getId().toString();
    Integer num = Convert.toInt(StrUtil.sub(str_num, str_num.indexOf("."), str_num.length() - 1));
    BigDecimal menge_d = scmBSupplyplan.getPkgAmount();
    BigDecimal leftMenge=  NumberUtil.sub(scmBSupplyplan.getgMenge(),NumberUtil.mul( new BigDecimal(num-1) , menge_d));

    for (Integer i = 1; i <= num; i++) {
        ScmBSupplyplanD scmd = new ScmBSupplyplanD();
        scmd.setId(gyjhId + (i < 10 ? "0" + i.toString() : i.toString()));
        if(i.equals(num)){
            scmd.setMenge(leftMenge);
        }
        else {
            scmd.setMenge(menge_d);
        }
        scmd.setBaseId(scmBSupplyplan.getId());
        scmd.setState(0);
        scmd.setCreateTime(new Date());
        scmd.setModifyTime(new Date());
        scmBSupplyplanDMapper.insert(scmd);
    }
}
    @Override
    @Transactional
    public void updateScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) throws FebsException {
        if (scmBSupplyplan.getIsDeletemark()==null||scmBSupplyplan.getIsDeletemark() >0) {//s删除不需要做验证
            Long isMenge = this.baseMapper.IsOutMenge(scmBSupplyplan);
            if (isMenge != null && isMenge > 0) {
                throw new FebsException("供应计划数量超出订单数量");
            }
            scmBSupplyplan.setModifyTime(new Date());
        }
        this.baseMapper.updateScmBSupplyplan(scmBSupplyplan);

        LambdaQueryWrapper<ScmBSendinfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ScmBSendinfo::getGyjh, scmBSupplyplan.getId());

        ScmBSendinfo scmBSendinfo = scmBSendinfoMapper.selectOne(queryWrapper);
        if (scmBSendinfo != null) {
            if (scmBSupplyplan.getIsDeletemark() == 0) {
                scmBSendinfo.setIsDeletemark(0);
            } else {
                scmBSendinfo.setModifyTime(new Date());
                scmBSendinfo.setAmount(scmBSupplyplan.getgMenge());

                scmBSendinfo.setLinkPerson(scmBSupplyplan.getLinkPerson());

                scmBSendinfo.setMoney(scmBSupplyplan.getFpjr());

                scmBSendinfo.setSendDepart(scmBSupplyplan.getSendDepart());
            }

            scmBSendinfoMapper.updateScmBSendinfo(scmBSendinfo);
        }

       // HandleScmBSupplyD(scmBSupplyplan);//hsc 20200819 增加箱数数据
    }

    @Override
    @Transactional
    public void updateSupplyplanOnly(ScmBSupplyplan scmBSupplyplan) throws FebsException {
        this.baseMapper.updateScmBSupplyplan(scmBSupplyplan);
    }

    @Override
    @Transactional
    public void deleteScmBSupplyplans(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }
    @Override
    @Transactional
    public void updateDoneMenge(String id,String doneMenge){
       this.baseMapper.UpdateDoneMenge(id,doneMenge);
    }

    @Override
    @Transactional
    public void updateCancelDoneMenge(String id,String doneMenge)
    {
        this.baseMapper.UpdateCancelDoneMenge(id, doneMenge);
    }
    @Override
    @Transactional
    public void updateCancelDoneMengeApp(String id)
    {
        this.baseMapper.UpdateCancelDoneMengeApp(id);
    }
    @Override
    @Transactional
   public void doneSupplyPlan(List<Long> ids)
    {
        this.baseMapper.doneSupplyPlan(ids);
    }
    @Override
    @Transactional
    public void cancleSupplyPlan(List<Long> ids)
    {
        this.baseMapper.cancelSupplyPlan(ids);
    }

    @Override
    @Transactional
    public  Boolean HasSendOrder(String ids)
    {
        Long isHas=this.baseMapper.hasSendOrder(ids);
        if(isHas>0) return  false;
        return  true;
    }
    @Override
    @Transactional
    public  Boolean HasPreDone(String ids)
    {
        Long isHas=this.baseMapper.hasPreDone(ids);
        if(isHas>0) return  false;
        return  true;
    }
    @Override
    @Transactional
    public  Boolean canUpdateSendOrder(String ids){
        List<String> lids=new ArrayList<>();
        String[] arr_ids = ids.split(StringPool.COMMA);
        for (String id :
                arr_ids) {
           lids.add(id);
        }
        int count=this.baseMapper.flagRecordByIds(lids);
        if(count>0) return  false;
        return  true;
    }
    @Override
    @Transactional
   public void updateWerkAndLgort(ViewSupplyplan plan){
        this.baseMapper.UpdateWerkAndLgort(plan);
    }
}