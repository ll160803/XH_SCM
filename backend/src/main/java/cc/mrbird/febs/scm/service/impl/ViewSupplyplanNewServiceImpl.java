package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanMapper;
import cc.mrbird.febs.scm.dao.ScmDControlMapper;
import cc.mrbird.febs.scm.dao.ViewSupplyplanMapper;
import cc.mrbird.febs.scm.entity.ScmBSapplan;
import cc.mrbird.febs.scm.entity.ScmDControl;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.entity.ViewSupplyplanNew;
import cc.mrbird.febs.scm.dao.ViewSupplyplanNewMapper;
import cc.mrbird.febs.scm.service.IScmBSapplanService;
import cc.mrbird.febs.scm.service.IViewSupplyplanNewService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */
@Slf4j
@Service("IViewSupplyplanNewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ViewSupplyplanNewServiceImpl extends ServiceImpl<ViewSupplyplanNewMapper, ViewSupplyplanNew> implements IViewSupplyplanNewService {
    @Autowired
    ScmBSupplyplanMapper scmBSupplyplanMapper;
    @Autowired
    ScmDControlMapper scmDControlMapper;
    @Autowired
    ViewSupplyplanMapper viewSupplyplanMapper;
    @Autowired
    IViewSupplyplanNewService iViewSupplyplanNewService;

    @Override
    public IPage<ViewSupplyplanNew> findViewSupplyplanNews(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew) {
        try {
            LambdaQueryWrapper<ViewSupplyplanNew> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ViewSupplyplanNew::getIsDeletemark, 1);//1是未删 0是已删


            Page<ViewSupplyplanNew> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private int findTotalCount(ViewSupplyplanNew viewSupplyplan, String time) {
        LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.and(wrapper -> wrapper.ne(ViewSupplyplan::getMaterCode, ""));
        if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeFrom())) {
            queryWrapper.ge(ViewSupplyplan::getMaterCode, viewSupplyplan.getMaterCodeFrom());
        }
        if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeTo())) {
            queryWrapper.le(ViewSupplyplan::getMaterCode, viewSupplyplan.getMaterCodeTo());
        }

        queryWrapper.le(ViewSupplyplan::getMaterCode, time);// 只有在截至时间之前的数据 才可以
        if (StringUtils.isNotEmpty(viewSupplyplan.getMatnr())) {
            queryWrapper.eq(ViewSupplyplan::getMatnr, viewSupplyplan.getMatnr());// 药品编码
        }
        if (StringUtils.isNotEmpty(viewSupplyplan.getTxz01())) {
            queryWrapper.like(ViewSupplyplan::getTxz01, viewSupplyplan.getTxz01());// 药品名称
        }
        if (StringUtils.isNotEmpty(viewSupplyplan.getSendDeaprtContact())) {
            queryWrapper.like(ViewSupplyplan::getSendDeaprtContact, viewSupplyplan.getSendDeaprtContact());// 是否集中采购
        }

        queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getFphm).or().eq(ViewSupplyplan::getFphm, ""
        ).or().eq(ViewSupplyplan::getFphm, viewSupplyplan.getFphm()));
        queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getCoder).or().eq(ViewSupplyplan::getCoder, ""
        ));
        queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getCode).or().eq(ViewSupplyplan::getCode, ""
        ).or().eq(ViewSupplyplan::getCode, viewSupplyplan.getCode()));
        if (viewSupplyplan.getId() != null) {
            queryWrapper.eq(ViewSupplyplan::getId, viewSupplyplan.getId());
        }
        if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
            queryWrapper.eq(ViewSupplyplan::getBaseId, viewSupplyplan.getBaseId());
        }
        if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
            queryWrapper.eq(ViewSupplyplan::getGysaccount, viewSupplyplan.getGysaccount());
        }
        if (viewSupplyplan.getStatus() != null) {
            queryWrapper.eq(ViewSupplyplan::getStatus, viewSupplyplan.getStatus());
        }
        if (StringUtils.isNotBlank(viewSupplyplan.getWerks())) {
            queryWrapper.eq(ViewSupplyplan::getWerks, viewSupplyplan.getWerks());
        }
        if (StringUtils.isNotBlank(viewSupplyplan.getLgort())) {
            queryWrapper.eq(ViewSupplyplan::getLgort, viewSupplyplan.getLgort());
        }
        if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("0")){
            queryWrapper.apply("view_supplyplan.ID in (select plan_id from scm_b_sapplan)");
        }
        if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("1")){
            queryWrapper.apply("view_supplyplan.ID not in (select plan_id from scm_b_sapplan)");
        }

        if (viewSupplyplan.getIsDeletemark() != null) {
            queryWrapper.eq(ViewSupplyplan::getIsDeletemark, viewSupplyplan.getIsDeletemark());
        }
        int count2 = this.viewSupplyplanMapper.selectCount(queryWrapper);
        return count2;
    }

    private int findTotalCount2(ViewSupplyplanNew viewSupplyplan, String time) {
        LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();


        queryWrapper.eq(ViewSupplyplan::getCode, viewSupplyplan.getCode());


        if (viewSupplyplan.getIsDeletemark() != null) {
            queryWrapper.eq(ViewSupplyplan::getIsDeletemark, viewSupplyplan.getIsDeletemark());
        }
        int count2 = this.viewSupplyplanMapper.selectCount(queryWrapper);
        return count2;
    }


    private List<ViewSupplyplan> findViewSupplyplans(QueryRequest request, ViewSupplyplanNew viewSupplyplan) {
        try {
            List<ScmDControl> control = this.scmDControlMapper.selectList(null);
            String time = DateUtil.format(control.get(0).getEndTime(), "yyyy-MM-dd");
            LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.and(wrapper -> wrapper.ne(ViewSupplyplan::getMaterCode, ""));
            if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeFrom())) {
                queryWrapper.ge(ViewSupplyplan::getMaterCode, viewSupplyplan.getMaterCodeFrom());
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeTo())) {
                queryWrapper.le(ViewSupplyplan::getMaterCode, viewSupplyplan.getMaterCodeTo());
            }

            queryWrapper.le(ViewSupplyplan::getMaterCode, time);// 只有在截至时间之前的数据 才可以
            if (StringUtils.isNotEmpty(viewSupplyplan.getMatnr())) {
                queryWrapper.eq(ViewSupplyplan::getMatnr, viewSupplyplan.getMatnr());// 药品编码
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getTxz01())) {
                queryWrapper.like(ViewSupplyplan::getTxz01, viewSupplyplan.getTxz01());// 药品名称
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getSendDeaprtContact())) {
                queryWrapper.like(ViewSupplyplan::getSendDeaprtContact, viewSupplyplan.getSendDeaprtContact());// 是否集中采购
            }

            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getFphm).or().eq(ViewSupplyplan::getFphm, ""
            ).or().eq(ViewSupplyplan::getFphm, viewSupplyplan.getFphm()));
            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getCoder).or().eq(ViewSupplyplan::getCoder, ""
            ));
            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplan::getCode).or().eq(ViewSupplyplan::getCode, ""
            ).or().eq(ViewSupplyplan::getCode, viewSupplyplan.getCode()));
            if (viewSupplyplan.getId() != null) {
                queryWrapper.eq(ViewSupplyplan::getId, viewSupplyplan.getId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
                queryWrapper.eq(ViewSupplyplan::getBaseId, viewSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
                queryWrapper.eq(ViewSupplyplan::getGysaccount, viewSupplyplan.getGysaccount());
            }
            if (viewSupplyplan.getStatus() != null) {
                queryWrapper.eq(ViewSupplyplan::getStatus, viewSupplyplan.getStatus());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerks())) {
                queryWrapper.eq(ViewSupplyplan::getWerks, viewSupplyplan.getWerks());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getLgort())) {
                queryWrapper.eq(ViewSupplyplan::getLgort, viewSupplyplan.getLgort());
            }

            if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("0")){
                queryWrapper.apply("view_supplyplan.ID in (select plan_id from scm_b_sapplan)");
            }
            if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("1")){
                queryWrapper.apply("view_supplyplan.ID not in (select plan_id from scm_b_sapplan)");
            }

            if (viewSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ViewSupplyplan::getIsDeletemark, viewSupplyplan.getIsDeletemark());
            }
            Page<ViewSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            page.setSearchCount(false);
            return this.viewSupplyplanMapper.selectPage(page, queryWrapper).getRecords();
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private List<ViewSupplyplan> findViewSupplyplans2(QueryRequest request, ViewSupplyplanNew viewSupplyplan) {
        try {

            LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();


            queryWrapper.eq(ViewSupplyplan::getCode, viewSupplyplan.getCode());


            if (viewSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ViewSupplyplan::getIsDeletemark, viewSupplyplan.getIsDeletemark());
            }
            Page<ViewSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            page.setSearchCount(false);
            return this.viewSupplyplanMapper.selectPage(page, queryWrapper).getRecords();
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ViewSupplyplanNew> findViewSupplyplans_byMaterCode(QueryRequest request, ViewSupplyplanNew viewSupplyplan) {
        try {
            List<ScmDControl> control = this.scmDControlMapper.selectList(null);
            String time = DateUtil.format(control.get(0).getEndTime(), "yyyy-MM-dd");
            LambdaQueryWrapper<ViewSupplyplanNew> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.and(wrapper -> wrapper.ne(ViewSupplyplanNew::getMaterCode, ""));
            if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeFrom())) {
                queryWrapper.ge(ViewSupplyplanNew::getMaterCode, viewSupplyplan.getMaterCodeFrom());
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getMaterCodeTo())) {
                queryWrapper.le(ViewSupplyplanNew::getMaterCode, viewSupplyplan.getMaterCodeTo());
            }

            queryWrapper.le(ViewSupplyplanNew::getMaterCode, time);// 只有在截至时间之前的数据 才可以
            if (StringUtils.isNotEmpty(viewSupplyplan.getMatnr())) {
                queryWrapper.eq(ViewSupplyplanNew::getMatnr, viewSupplyplan.getMatnr());// 药品编码
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getTxz01())) {
                queryWrapper.like(ViewSupplyplanNew::getTxz01, viewSupplyplan.getTxz01());// 药品名称
            }
            if (StringUtils.isNotEmpty(viewSupplyplan.getSendDeaprtContact())) {
                queryWrapper.like(ViewSupplyplanNew::getSendDeaprtContact, viewSupplyplan.getSendDeaprtContact());// 是否集中采购
            }

            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplanNew::getFphm).or().eq(ViewSupplyplanNew::getFphm, ""
            ).or().eq(ViewSupplyplanNew::getFphm, viewSupplyplan.getFphm()));
            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplanNew::getCoder).or().eq(ViewSupplyplanNew::getCoder, ""
            ));
            queryWrapper.and(wrapper -> wrapper.isNull(ViewSupplyplanNew::getCode).or().eq(ViewSupplyplanNew::getCode, ""
            ).or().eq(ViewSupplyplanNew::getCode, viewSupplyplan.getCode()));
            if (viewSupplyplan.getId() != null) {
                queryWrapper.eq(ViewSupplyplanNew::getId, viewSupplyplan.getId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getBaseId())) {
                queryWrapper.eq(ViewSupplyplanNew::getBaseId, viewSupplyplan.getBaseId());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getGysaccount())) {
                queryWrapper.eq(ViewSupplyplanNew::getGysaccount, viewSupplyplan.getGysaccount());
            }
            if (viewSupplyplan.getStatus() != null) {
                queryWrapper.eq(ViewSupplyplanNew::getStatus, viewSupplyplan.getStatus());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getWerks())) {
                queryWrapper.eq(ViewSupplyplanNew::getWerks, viewSupplyplan.getWerks());
            }
            if (StringUtils.isNotBlank(viewSupplyplan.getLgort())) {
                queryWrapper.eq(ViewSupplyplanNew::getLgort, viewSupplyplan.getLgort());
            }


            if (viewSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ViewSupplyplanNew::getIsDeletemark, viewSupplyplan.getIsDeletemark());
            }
                int count1 =0;
                int count2 =0;
            if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("0")){
                    count1 = this.baseMapper.selectCount(queryWrapper);
            }
            else if(StringUtils.isNotBlank(viewSupplyplan.getMseht()) && viewSupplyplan.getMseht().equals("1")){
                    count2 = this.findTotalCount(viewSupplyplan, time);
            }
            else{
                    count2 = this.findTotalCount(viewSupplyplan, time);
                    count1 = this.baseMapper.selectCount(queryWrapper);
                }


            Page<ViewSupplyplanNew> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            page.setTotal(count1 + count2);
            List<ViewSupplyplanNew> list2 = new ArrayList<>();
            if (request.getPageNum() * request.getPageSize() <= count2) {


                List<ViewSupplyplan> viewSupplyplanList = this.findViewSupplyplans(request, viewSupplyplan);

                viewSupplyplanList.forEach(p -> {
                    ViewSupplyplanNew plan = new ViewSupplyplanNew();
                    BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                    list2.add(plan);
                });
                page.setRecords(list2);
            } else if ((request.getPageNum() - 1) * request.getPageSize() < count2) {
                List<ViewSupplyplan> viewSupplyplanList = this.findViewSupplyplans(request, viewSupplyplan);

                viewSupplyplanList.forEach(p -> {
                    ViewSupplyplanNew plan = new ViewSupplyplanNew();
                    BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                    list2.add(plan);
                });
                Page<ViewSupplyplanNew> page2 = new Page<>();
                page2.setPages(1);
                page2.setSize(request.getPageNum() * request.getPageSize() - count2);
                page2.setSearchCount(false);
                List<ViewSupplyplanNew> list3 = this.baseMapper.selectPage(page2, queryWrapper).getRecords();
                list3.forEach(p -> {
                    list2.add(p);
                });
            } else {
                queryWrapper.last(String.format(" limit %1$s ,%2$s", (request.getPageNum() - 1) * request.getPageSize() - count2, request.getPageSize()));
                List<ViewSupplyplanNew> list4 = this.baseMapper.selectList(queryWrapper);
                list4.forEach(p -> {
                    list2.add(p);
                });

            }
            page.setRecords(list2);


            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ViewSupplyplanNew> findViewSupplyplans_byMaterCode2(QueryRequest request, ViewSupplyplanNew viewSupplyplan) {
        try {
            List<ScmDControl> control = this.scmDControlMapper.selectList(null);
            String time = DateUtil.format(control.get(0).getEndTime(), "yyyy-MM-dd");

            Page<ViewSupplyplanNew> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);

            List<ViewSupplyplanNew> list2 = new ArrayList<>();
            List<ViewSupplyplan> viewSupplyplanList = this.findViewSupplyplans(request, viewSupplyplan);


           int count2 = this.findTotalCount(viewSupplyplan, time);
            page.setTotal(count2);


            List<String> ids= viewSupplyplanList.stream().map(p->p.getId().toString()).collect(Collectors.toList());

            if(ids.size()>0) {
                LambdaQueryWrapper<ViewSupplyplanNew> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.in(ViewSupplyplanNew::getId, ids);
                List<ViewSupplyplanNew> listSapPlan = this.iViewSupplyplanNewService.list(lambdaQueryWrapper);
                list2.addAll(listSapPlan);
            }
            viewSupplyplanList.forEach(p -> {
                ViewSupplyplanNew plan = new ViewSupplyplanNew();
                BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                list2.add(plan);
            });
            List<ViewSupplyplanNew> list3= list2.stream().sorted(Comparator.comparing(ViewSupplyplanNew::getId)).collect(Collectors.toList());
            page.setRecords(list3);



            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ViewSupplyplanNew> findViewSupplyplans_byCode(QueryRequest request, ViewSupplyplanNew viewSupplyplan) {
        try {
            List<ScmDControl> control = this.scmDControlMapper.selectList(null);
            String time = DateUtil.format(control.get(0).getEndTime(), "yyyy-MM-dd");
            LambdaQueryWrapper<ViewSupplyplanNew> queryWrapper = new LambdaQueryWrapper<>();
            if (viewSupplyplan.getIsDeletemark() != null) {
                queryWrapper.eq(ViewSupplyplanNew::getIsDeletemark, viewSupplyplan.getIsDeletemark());
            }

            queryWrapper.eq(ViewSupplyplanNew::getCode, viewSupplyplan.getCode());

          int  count2 = this.findTotalCount2(viewSupplyplan, time);
           int count1 = this.baseMapper.selectCount(queryWrapper);


            Page<ViewSupplyplanNew> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            page.setTotal(count1 + count2);
            List<ViewSupplyplanNew> list2 = new ArrayList<>();
            if (request.getPageNum() * request.getPageSize() <= count2) {


                List<ViewSupplyplan> viewSupplyplanList = this.findViewSupplyplans2(request, viewSupplyplan);

                viewSupplyplanList.forEach(p -> {
                    ViewSupplyplanNew plan = new ViewSupplyplanNew();
                    BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                    list2.add(plan);
                });
                page.setRecords(list2);
            } else if ((request.getPageNum() - 1) * request.getPageSize() < count2) {
                List<ViewSupplyplan> viewSupplyplanList = this.findViewSupplyplans2(request, viewSupplyplan);

                viewSupplyplanList.forEach(p -> {
                    ViewSupplyplanNew plan = new ViewSupplyplanNew();
                    BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                    list2.add(plan);
                });
                Page<ViewSupplyplanNew> page2 = new Page<>();
                page2.setPages(1);
                page2.setSize(request.getPageNum() * request.getPageSize() - count2);
                page2.setSearchCount(false);
                List<ViewSupplyplanNew> list3 = this.baseMapper.selectPage(page2, queryWrapper).getRecords();
                list3.forEach(p -> {
                    list2.add(p);
                });
            } else {
                queryWrapper.last(String.format(" limit %1$s ,%2$s", (request.getPageNum() - 1) * request.getPageSize() - count2, request.getPageSize()));
                List<ViewSupplyplanNew> list4 = this.baseMapper.selectList(queryWrapper);
                list4.forEach(p -> {
                    list2.add(p);
                });

            }
            page.setRecords(list2);


            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ViewSupplyplanNew> findViewSupplyplanNewList(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew) {
        try {
            Page<ViewSupplyplanNew> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findViewSupplyplanNew(page, viewSupplyplanNew);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createViewSupplyplanNew(ViewSupplyplanNew viewSupplyplanNew) {
        viewSupplyplanNew.setCreateTime(new Date());
        viewSupplyplanNew.setIsDeletemark(1);
        this.save(viewSupplyplanNew);
    }

    @Override
    @Transactional
    public void updateViewSupplyplanNew(ViewSupplyplanNew viewSupplyplanNew) {
        viewSupplyplanNew.setModifyTime(new Date());
        this.baseMapper.updateViewSupplyplanNew(viewSupplyplanNew);
    }

    @Override
    @Transactional
    public void deleteViewSupplyplanNews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}