package cc.mrbird.febs.his.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.his.entity.ScmWFpplan;
import cc.mrbird.febs.his.dao.ScmWFpplanMapper;
import cc.mrbird.febs.his.service.IScmWFpplanService;
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
 * HIS开票清单 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
@Slf4j
@Service("IScmWFpplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmWFpplanServiceImpl extends ServiceImpl<ScmWFpplanMapper, ScmWFpplan> implements IScmWFpplanService {


@Override
public IPage<ScmWFpplan> findScmWFpplans(QueryRequest request, ScmWFpplan scmWFpplan){
        try{
        LambdaQueryWrapper<ScmWFpplan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmWFpplan::getIsDeletemark, 1);//1是未删 0是已删

            if(scmWFpplan.getId()!=null){
                queryWrapper.eq(ScmWFpplan::getId,scmWFpplan.getId());
            }
     if(StringUtils.isNotEmpty(scmWFpplan.getGysname())){
             queryWrapper.eq(ScmWFpplan::getGysname,scmWFpplan.getGysname());
     }
            if (StringUtils.isNotBlank(scmWFpplan.getCreateTimeFrom()) ) {
                queryWrapper
                        .ge(ScmWFpplan::getCreateTime, scmWFpplan.getCreateTimeFrom())
                ;
            }
            if(StringUtils.isNotBlank(scmWFpplan.getCreateTimeTo())){
                queryWrapper.le(ScmWFpplan::getCreateTime, scmWFpplan.getCreateTimeTo()+" 23:59:59");
            }
        Page<ScmWFpplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmWFpplan> findScmWFpplanList (QueryRequest request, ScmWFpplan scmWFpplan){
        try{
        Page<ScmWFpplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmWFpplan(page,scmWFpplan);
        }catch(Exception e){
        log.error("获取HIS开票清单失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmWFpplan(ScmWFpplan scmWFpplan){
                scmWFpplan.setCreateTime(new Date());
        scmWFpplan.setIsDeletemark(1);
        this.save(scmWFpplan);
        }

@Override
@Transactional
public void updateScmWFpplan(ScmWFpplan scmWFpplan){
        scmWFpplan.setModifyTime(new Date());
        this.baseMapper.updateScmWFpplan(scmWFpplan);
        }

@Override
@Transactional
public void deleteScmWFpplans(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }