package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.entity.ScmBFpplan;
import cc.mrbird.febs.scm.dao.ScmBFpplanMapper;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.service.IScmBFpplanService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 开票清单 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-04-07
 */
@Slf4j
@Service("IScmBFpplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBFpplanServiceImpl extends ServiceImpl<ScmBFpplanMapper, ScmBFpplan> implements IScmBFpplanService {


    @Override
    public IPage<ScmBFpplan> findScmBFpplans(QueryRequest request, ScmBFpplan scmBFpplan) {
        try {
            LambdaQueryWrapper<ScmBFpplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmBFpplan::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(scmBFpplan.getCreateTimeFrom()) ) {
                queryWrapper
                        .ge(ScmBFpplan::getCreateTime, scmBFpplan.getCreateTimeFrom())
                        ;
            }
            if(StringUtils.isNotBlank(scmBFpplan.getCreateTimeTo())){
                queryWrapper.le(ScmBFpplan::getCreateTime, scmBFpplan.getCreateTimeTo()+" 23:59:59");
            }
            if (StringUtils.isNotBlank(scmBFpplan.getModifyTimeFrom()) && StringUtils.isNotBlank(scmBFpplan.getModifyTimeTo())) {
                queryWrapper
                        .ge(ScmBFpplan::getModifyTime, scmBFpplan.getModifyTimeFrom())
                        .le(ScmBFpplan::getModifyTime, scmBFpplan.getModifyTimeTo());
            }
            if(StringUtils.isNotEmpty(scmBFpplan.getGysaccount())){
                queryWrapper.eq(ScmBFpplan::getGysaccount,scmBFpplan.getGysaccount());
            }
            if(scmBFpplan.getId()!=null){
                queryWrapper.eq(ScmBFpplan::getId,scmBFpplan.getId());
            }
            Page<ScmBFpplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<ScmBFpplan> findScmBFpplans2(QueryRequest request, ScmBFpplan scmBFpplan) {
        try {
            LambdaQueryWrapper<ScmBFpplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmBFpplan::getIsDeletemark, 1);//1是未删 0是已删

            if(StringUtils.isNotEmpty(scmBFpplan.getFphm())){
                queryWrapper.and(wrap->wrap.eq(ScmBFpplan::getFphm,"").or().eq(ScmBFpplan::getFphm,scmBFpplan.getFphm())
                .or().isNull(ScmBFpplan::getFphm));
            }
            else {
                queryWrapper.and(wrap->wrap.eq(ScmBFpplan::getFphm,"")
                        .or().isNull(ScmBFpplan::getFphm));
            }

            if (StringUtils.isNotBlank(scmBFpplan.getCreateTimeFrom()) ) {
                queryWrapper
                        .ge(ScmBFpplan::getCreateTime, scmBFpplan.getCreateTimeFrom())
                ;
            }
            if(StringUtils.isNotBlank(scmBFpplan.getCreateTimeTo())){
                queryWrapper.le(ScmBFpplan::getCreateTime, scmBFpplan.getCreateTimeTo() +" 23:59:59");
            }
            queryWrapper.eq(ScmBFpplan::getFpjr,scmBFpplan.getFpjr());
            Page<ScmBFpplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ScmBFpplan> findScmBFpplanList(QueryRequest request, ScmBFpplan scmBFpplan) {
        try {
            Page<ScmBFpplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findScmBFpplan(page, scmBFpplan);
        } catch (Exception e) {
            log.error("获取开票清单失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public List<Long> findPlanIds(String sendCode) {
        return this.baseMapper.findPlanIds(sendCode);
    }

    @Override
    @Transactional
    public void createScmBFpplan(ScmBFpplan scmBFpplan) {
        scmBFpplan.setCreateTime(new Date());
        scmBFpplan.setIsDeletemark(1);
        this.save(scmBFpplan);
        String supplyPlanIds = scmBFpplan.supplyPlanIds;

        if (StringUtils.isNotBlank(supplyPlanIds)) {
            String[] arr_ids = supplyPlanIds.split(StringPool.COMMA);

            List<Long> ids = new ArrayList<>();
            for (String idStr : arr_ids
            ) {
                ids.add(Long.parseLong(idStr));
            }
            this.baseMapper.updateSupplyPlan2(ids, scmBFpplan.getId().toString());
        }
    }

    @Override
    @Transactional
    public void updateScmBFpplan(ScmBFpplan scmBFpplan) {
        scmBFpplan.setModifyTime(new Date());
        this.baseMapper.updateScmBFpplan(scmBFpplan);
        String supplyPlanIds = scmBFpplan.supplyPlanIds;

        if (StringUtils.isNotBlank(supplyPlanIds)) {
            this.baseMapper.removeOrderCode(scmBFpplan.getId().toString());//先清空之前的
            String[] arr_ids = supplyPlanIds.split(StringPool.COMMA);
            List<Long> ids = new ArrayList<>();
            for (String idStr : arr_ids
            ) {
                ids.add(Long.parseLong(idStr));
            }

            // String str_ids = "'" + supplyPlanIds.replace(",", "','") + "'";
            this.baseMapper.updateSupplyPlan2(ids, scmBFpplan.getId().toString());

        }
    }

    @Override
    @Transactional
    public void deleteScmBFpplans(String[] Ids) {

    }

    @Override
    @Transactional
    public void updateFpplan(String id){
        this.baseMapper.updateFpplan(Long.parseLong(id));
    }
    @Override
    @Transactional
    public void updateFpData(String code,String fprq,String fphm){
        this.baseMapper.updateFpData(code,fprq,fphm);
    }

    @Override
    @Transactional
    public void updateDeletePlan(String code, Long id){
        this.baseMapper.updateDeletePlan(code,id);
    }
}