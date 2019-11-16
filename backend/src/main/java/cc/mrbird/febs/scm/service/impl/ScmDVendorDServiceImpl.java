package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmDVendorD;
import cc.mrbird.febs.scm.dao.ScmDVendorDMapper;
import cc.mrbird.febs.scm.service.IScmDVendorDService;
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


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;
/**
 * <p>
 * 供应商对应的资质文件表 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Service("IScmDVendorDService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmDVendorDServiceImpl extends ServiceImpl<ScmDVendorDMapper, ScmDVendorD> implements IScmDVendorDService {


@Override
public IPage<ScmDVendorD> findScmDVendorDs(QueryRequest request, ScmDVendorD scmDVendorD){
        try{
        LambdaQueryWrapper<ScmDVendorD> queryWrapper=new LambdaQueryWrapper<>();

        Page<ScmDVendorD> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createScmDVendorD(ScmDVendorD scmDVendorD){
        scmDVendorD.setId(UUID.randomUUID().toString());
        scmDVendorD.setCreateTime(new Date());
        this.save(scmDVendorD);
        }

@Override
@Transactional
public void updateScmDVendorD(ScmDVendorD scmDVendorD){
        scmDVendorD.setModifyTime(new Date());
        this.baseMapper.updateScmDVendorD(scmDVendorD);
        }

@Override
@Transactional
public void deleteScmDVendorDs(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }