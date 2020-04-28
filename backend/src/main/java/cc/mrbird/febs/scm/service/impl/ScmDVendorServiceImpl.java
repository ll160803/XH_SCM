package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmDVendorDMapper;
import cc.mrbird.febs.scm.entity.*;
import cc.mrbird.febs.scm.dao.ScmDVendorMapper;
import cc.mrbird.febs.scm.service.IScmDVendorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Service("IScmDVendorService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmDVendorServiceImpl extends ServiceImpl<ScmDVendorMapper, ScmDVendor> implements IScmDVendorService {

    @Autowired
    private ScmDVendorDMapper scmDVendorDMapper;

    @Override
    public IPage<ScmDVendor> findScmDVendors(QueryRequest request, ScmDVendor scmDVendor, String keyword) {
        try {
            LambdaQueryWrapper<ScmDVendor> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(scmDVendor.getName())) {
                queryWrapper.like(ScmDVendor::getName, scmDVendor.getName());
            }

            if (StringUtils.isNotBlank(scmDVendor.getCode())) {
                queryWrapper.eq(ScmDVendor::getCode, scmDVendor.getCode());
            }
            if (scmDVendor.getLb() != null) {
                queryWrapper.eq(ScmDVendor::getLb, scmDVendor.getLb());
            }
            if (scmDVendor.getState() != null && scmDVendor.getState() != -1) {
                queryWrapper.eq(ScmDVendor::getState, scmDVendor.getState());
            }
            if (StringUtils.isNotBlank(keyword)) {
                if (keyword.equals("-1")) {
                    queryWrapper.isNotNull(ScmDVendor::getCode);
                } else {
                    queryWrapper.and(wrapper -> wrapper.like(ScmDVendor::getName, keyword).or().eq(ScmDVendor::getCode, keyword));
                }
            }
            Page<ScmDVendor> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<VendorRank> findScmDVendorsRank(QueryRequest request, ScmBPurcharseorder order) {
        Page<VendorRank> page = new Page<>();

        List<VendorRank> records = this.baseMapper.getRank(order);
        List<VendorRank> backRerords = records.stream().skip((request.getPageNum() - 1) * request.getPageSize()).limit(request.getPageSize()).collect(Collectors.toList());
        page.setRecords(backRerords);
        page.setTotal(records.size());

        //  page.setCurrent(request.getPageNum());
        return page;
    }
    @Override
    public IPage<MaterPercentage> findScmDVendorsMater(QueryRequest request, ScmBPurcharseorder order) {

        Page<MaterPercentage> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);

       this.baseMapper.getMatnrPercentage(page,order);
        //  page.setCurrent(request.getPageNum());
        return page;
    }
    @Override
    public IPage<TotalStatistic> findVendorM(QueryRequest request, ScmBPurcharseorder order){
        Page<TotalStatistic> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);

        this.baseMapper.getGysAmount(page,order);
        //  page.setCurrent(request.getPageNum());
        return page;
    }
    @Override
    public IPage<TotalStatistic> findMaterVendor(QueryRequest request, ScmBPurcharseorder order){
        Page<TotalStatistic> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        log.info(order.getLgortName());
        this.baseMapper.getMatnrAmount(page,order);
        //  page.setCurrent(request.getPageNum());
        return page;
    }

    @Override
    @Transactional
    public void createScmDVendor(ScmDVendor scmDVendor) {
        scmDVendor.setId(UUID.randomUUID().toString());
        scmDVendor.setCreateTime(new Date());
        this.save(scmDVendor);
    }

    @Override
    @Transactional
    public void updateScmDVendor(ScmDVendor scmDVendor) {
        scmDVendor.setModifyTime(new Date());
        this.baseMapper.updateScmDVendor(scmDVendor);
    }

    @Override
    @Transactional
    public void deleteScmDVendors(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void createScmVendor(ScmDVendor scmDVendor, List<ScmDVendorD> scmDVendorDS) {
        if(!StringUtils.isNotBlank(scmDVendor.getId()))
        {
            scmDVendor.setId(UUID.randomUUID().toString());
        }
        scmDVendor.setCreateTime(new Date());
        scmDVendor.setState(0);//不可用
        scmDVendor.setIsDeletemark(1);
        scmDVendor.setFileState(0);
        scmDVendor.setJieKouState(0);
        this.save(scmDVendor);
        for (ScmDVendorD scmDVendorD :
                scmDVendorDS) {
            scmDVendorD.setId(UUID.randomUUID().toString());
            scmDVendorD.setBaseId(scmDVendor.getId());
            scmDVendorDMapper.insert(scmDVendorD);
        }
    }

    @Override
    @Transactional
    public void updateScmDVendor(ScmDVendor scmDVendor, List<ScmDVendorD> scmDVendorDS) {
        scmDVendor.setModifyTime(new Date());
        this.updateScmDVendor(scmDVendor);
        QueryWrapper<ScmDVendorD> queryWrapper = new QueryWrapper<>();
        String F_id = scmDVendor.getId();
        queryWrapper.lambda().eq(ScmDVendorD::getBaseId, F_id);
        scmDVendorDMapper.delete(queryWrapper);
        for (ScmDVendorD scmDVendorD :
                scmDVendorDS) {
            scmDVendorD.setId(UUID.randomUUID().toString());
            scmDVendorD.setBaseId(scmDVendor.getId());
            scmDVendorDMapper.insert(scmDVendorD);
        }
    }
}