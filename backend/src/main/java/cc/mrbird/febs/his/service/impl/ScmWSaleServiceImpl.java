package cc.mrbird.febs.his.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.his.entity.ScmWSale;
import cc.mrbird.febs.his.dao.ScmWSaleMapper;
import cc.mrbird.febs.his.service.IScmWSaleService;
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
 *  服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
@Slf4j
@Service("IScmWSaleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmWSaleServiceImpl extends ServiceImpl<ScmWSaleMapper, ScmWSale> implements IScmWSaleService {


@Override
public IPage<ScmWSale> findScmWSales(QueryRequest request, ScmWSale scmWSale){
        try{
        LambdaQueryWrapper<ScmWSale> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmWSale::getIsDeletemark, 1);//1是未删 0是已删

                                if (StringUtils.isNotBlank(scmWSale.getId())) {
                                queryWrapper.like(ScmWSale::getId, scmWSale.getId());
                                }

        Page<ScmWSale> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmWSale> findScmWSaleList (QueryRequest request, ScmWSale scmWSale){
        try{
        Page<ScmWSale> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmWSale(page,scmWSale);
        }catch(Exception e){
        log.error("获取失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmWSale(ScmWSale scmWSale){
                scmWSale.setId(UUID.randomUUID().toString());
        scmWSale.setCreateTime(new Date());
        scmWSale.setIsDeletemark(1);
        this.save(scmWSale);
        }

@Override
@Transactional
public void updateScmWSale(ScmWSale scmWSale){
        scmWSale.setModifyTime(new Date());
        this.baseMapper.updateScmWSale(scmWSale);
        }

@Override
@Transactional
public void deleteScmWSales(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }