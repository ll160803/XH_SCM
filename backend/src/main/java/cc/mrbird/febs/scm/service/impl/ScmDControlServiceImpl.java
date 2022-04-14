package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmDControl;
import cc.mrbird.febs.scm.dao.ScmDControlMapper;
import cc.mrbird.febs.scm.service.IScmDControlService;
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
 * @since 2022-04-07
 */
@Slf4j
@Service("IScmDControlService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmDControlServiceImpl extends ServiceImpl<ScmDControlMapper, ScmDControl> implements IScmDControlService {


@Override
public IPage<ScmDControl> findScmDControls(QueryRequest request, ScmDControl scmDControl){
        try{
        LambdaQueryWrapper<ScmDControl> queryWrapper=new LambdaQueryWrapper<>();



        Page<ScmDControl> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmDControl> findScmDControlList (QueryRequest request, ScmDControl scmDControl){
        try{
        Page<ScmDControl> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmDControl(page,scmDControl);
        }catch(Exception e){
        log.error("获取失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmDControl(ScmDControl scmDControl){

        this.save(scmDControl);
        }

@Override
@Transactional
public void updateScmDControl(ScmDControl scmDControl){

        this.baseMapper.updateScmDControl(scmDControl);
        }

@Override
@Transactional
public void deleteScmDControls(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }

        @Override
        @Transactional
        public    void updateEndTime(ScmDControl scmDControl){
          this.baseMapper.updateEndTime(scmDControl);
        }
        }