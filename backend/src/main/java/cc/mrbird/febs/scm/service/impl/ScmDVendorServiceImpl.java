package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmDVendor;
import cc.mrbird.febs.scm.dao.ScmDVendorMapper;
import cc.mrbird.febs.scm.service.IScmDVendorService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
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


@Override
public IPage<ScmDVendor> findScmDVendors(QueryRequest request, ScmDVendor scmDVendor){
        try{
        LambdaQueryWrapper<ScmDVendor> queryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(scmDVendor.getCode())) {
        queryWrapper.eq(ScmDVendor::getCode, scmDVendor.getCode());
        }
        Page<ScmDVendor> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createScmDVendor(ScmDVendor scmDVendor){
        scmDVendor.setId(UUID.randomUUID().toString());
        scmDVendor.setCreateTime(LocalDateTime.now());
        this.save(scmDVendor);
        }

@Override
@Transactional
public void updateScmDVendor(ScmDVendor scmDVendor){
        scmDVendor.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateScmDVendor(scmDVendor);
        }

@Override
@Transactional
public void deleteScmDVendors(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }