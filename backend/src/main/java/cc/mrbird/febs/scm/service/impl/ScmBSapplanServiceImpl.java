package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmBSapplan;
import cc.mrbird.febs.scm.dao.ScmBSapplanMapper;
import cc.mrbird.febs.scm.service.IScmBSapplanService;
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
 * sap供应计划 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */
@Slf4j
@Service("IScmBSapplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBSapplanServiceImpl extends ServiceImpl<ScmBSapplanMapper, ScmBSapplan> implements IScmBSapplanService {


@Override
public IPage<ScmBSapplan> findScmBSapplans(QueryRequest request, ScmBSapplan scmBSapplan){
        try{
        LambdaQueryWrapper<ScmBSapplan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmBSapplan::getIsDeletemark, 1);//1是未删 0是已删

                                if (StringUtils.isNotBlank(scmBSapplan.getCode())) {
                                queryWrapper.like(ScmBSapplan::getCode, scmBSapplan.getCode());
                                }

        Page<ScmBSapplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmBSapplan> findScmBSapplanList (QueryRequest request, ScmBSapplan scmBSapplan){
        try{
        Page<ScmBSapplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmBSapplan(page,scmBSapplan);
        }catch(Exception e){
        log.error("获取sap供应计划失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmBSapplan(ScmBSapplan scmBSapplan){
                scmBSapplan.setCreateTime(new Date());
        scmBSapplan.setIsDeletemark(1);
        this.save(scmBSapplan);
        }

@Override
@Transactional
public void updateScmBSapplan(ScmBSapplan scmBSapplan){
        scmBSapplan.setModifyTime(new Date());
        this.baseMapper.updateScmBSapplan(scmBSapplan);
        }

@Override
@Transactional
public void deleteScmBSapplans(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }