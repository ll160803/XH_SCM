package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.dao.ViewSupplyplanMapper;
import cc.mrbird.febs.scm.service.IViewSupplyplanService;
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

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-12-05
 */
@Slf4j
@Service("IViewSupplyplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ViewSupplyplanServiceImpl extends ServiceImpl<ViewSupplyplanMapper, ViewSupplyplan> implements IViewSupplyplanService {


    @Override
    public IPage<ViewSupplyplan> findViewSupplyplans(QueryRequest request, ViewSupplyplan viewSupplyplan) {
        try {
            LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ViewSupplyplan::getIsDeletemark, 1);//1是未删 0是已删
            if (viewSupplyplan.getId() != null) {
                queryWrapper.eq(ViewSupplyplan::getId, viewSupplyplan.getId());
            }
            if (viewSupplyplan.getStatus()!=null) {
                queryWrapper.eq(ViewSupplyplan::getStatus, viewSupplyplan.getStatus());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getEbeln())) {
                queryWrapper.eq(ViewSupplyplan::getEbeln, viewSupplyplan.getEbeln());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
                queryWrapper.eq(ViewSupplyplan::getBaseId, viewSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerks()) && !viewSupplyplan.getWerks().equals("0")) {
                queryWrapper.eq(ViewSupplyplan::getWerks, viewSupplyplan.getWerks());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerkst())) {
                queryWrapper.like(ViewSupplyplan::getWerkst, viewSupplyplan.getWerkst());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getLgortName())) {
                queryWrapper.like(ViewSupplyplan::getLgortName, viewSupplyplan.getLgortName());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getLgort()) && !viewSupplyplan.getLgort().equals("0")) {
                queryWrapper.eq(ViewSupplyplan::getLgort, viewSupplyplan.getLgort());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysname())) {
                queryWrapper.like(ViewSupplyplan::getGysname, viewSupplyplan.getGysname());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
                queryWrapper.eq(ViewSupplyplan::getGysaccount, viewSupplyplan.getGysaccount());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getTxz01())) {
                queryWrapper.like(ViewSupplyplan::getTxz01, viewSupplyplan.getTxz01());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getMatnr())) {
                queryWrapper.eq(ViewSupplyplan::getMatnr, viewSupplyplan.getMatnr());
            }
            if(StringUtils.isNotBlank(viewSupplyplan.getNoOrder())&&viewSupplyplan.getNoOrder()=="1")
            {
               queryWrapper.and(p-> p.isNull(ViewSupplyplan::getSendOrderCode).or().eq(ViewSupplyplan::getSendOrderCode,""));
            }
            Page<ViewSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ViewSupplyplan> findViewSupplyplans2(QueryRequest request, ViewSupplyplan viewSupplyplan) {
        try {
            LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();

            if (viewSupplyplan.getId() != null) {
                queryWrapper.eq(ViewSupplyplan::getId, viewSupplyplan.getId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
                queryWrapper.eq(ViewSupplyplan::getBaseId, viewSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
                queryWrapper.eq(ViewSupplyplan::getGysaccount, viewSupplyplan.getGysaccount());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerks())) {
                queryWrapper.eq(ViewSupplyplan::getWerks, viewSupplyplan.getWerks());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getLgort())) {
                queryWrapper.eq(ViewSupplyplan::getLgort, viewSupplyplan.getLgort());
            }
            if (viewSupplyplan.getBsartD() == "1") {
                if (StringUtils.isNotBlank(viewSupplyplan.getFphm())) {
                    queryWrapper.and(wrapper -> wrapper.eq(ViewSupplyplan::getFphm, viewSupplyplan.getFphm()).or().eq(ViewSupplyplan::getFphm, null
                    ).or().eq(ViewSupplyplan::getFphm, ""
                    ));
                } else {
                    queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getFphm)
                            .or().eq(ViewSupplyplan::getFphm, ""
                            ));
                }

            } else {
                queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getSendOrderCode).or().eq(ViewSupplyplan::getSendOrderCode, ""
                ).or().eq(ViewSupplyplan::getSendOrderCode, viewSupplyplan.getSendOrderCode()));
            }
            queryWrapper.eq(ViewSupplyplan::getBsartD, viewSupplyplan.getBsartD());//订单类型
            if (viewSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ViewSupplyplan::getIsDeletemark, viewSupplyplan.getIsDeletemark());
            }
            Page<ViewSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }
    @Override
    @Transactional
    public void createViewSupplyplan(ViewSupplyplan viewSupplyplan) {

        viewSupplyplan.setCreateTime(new Date());
        viewSupplyplan.setIsDeletemark(1);
        this.save(viewSupplyplan);
    }

    @Override
    @Transactional
    public void updateViewSupplyplan(ViewSupplyplan viewSupplyplan) {
        viewSupplyplan.setModifyTime(new Date());
        this.baseMapper.updateViewSupplyplan(viewSupplyplan);
    }

    @Override
    @Transactional
    public void deleteViewSupplyplans(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }
     @Override
     @Transactional
    public List<ViewSupplyplan> getViewSupplyPlanByIds(String ids)
     {
         return  this.baseMapper.GetViewSupplyPlanByIds(ids);
     }

    @Override
    public IPage<ViewSupplyplan> findDoneViewSupplyplans(QueryRequest request, ViewSupplyplan viewSupplyplan, String statusType) {
        try {
            LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ViewSupplyplan::getIsDeletemark, 1);//1是未删 0是已删

            queryWrapper.eq(ViewSupplyplan::getBsartD, "0");
            if (StringUtils.equals(statusType, "0"))//预收
            {
                queryWrapper.eq(ViewSupplyplan::getStatus, 0);
            }
            if (StringUtils.equals(statusType, "1"))//已收
            {
                queryWrapper.eq(ViewSupplyplan::getStatus, 0);
                queryWrapper.gt(ViewSupplyplan::getDoneMenge, 0);
            }

            if (StringUtils.isNotBlank(viewSupplyplan.getEbeln())) {
                queryWrapper.eq(ViewSupplyplan::getEbeln, viewSupplyplan.getEbeln());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
                queryWrapper.eq(ViewSupplyplan::getBaseId, viewSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerks())) {
                queryWrapper.eq(ViewSupplyplan::getWerks, viewSupplyplan.getWerks());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerkst())) {
                queryWrapper.like(ViewSupplyplan::getWerkst, viewSupplyplan.getWerkst());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysname())) {
                queryWrapper.like(ViewSupplyplan::getGysname, viewSupplyplan.getGysname());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
                queryWrapper.eq(ViewSupplyplan::getGysaccount, viewSupplyplan.getGysaccount());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getTxz01())) {
                queryWrapper.like(ViewSupplyplan::getTxz01, viewSupplyplan.getTxz01());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getMatnr())) {
                queryWrapper.eq(ViewSupplyplan::getMatnr, viewSupplyplan.getMatnr());
            }


            Page<ViewSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }
}