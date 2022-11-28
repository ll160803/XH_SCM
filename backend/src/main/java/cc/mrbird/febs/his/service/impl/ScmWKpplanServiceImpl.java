package cc.mrbird.febs.his.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.his.entity.ScmWFpplan;
import cc.mrbird.febs.his.entity.ScmWKpplan;
import cc.mrbird.febs.his.dao.ScmWKpplanMapper;
import cc.mrbird.febs.his.service.IScmWKpplanService;
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
 * @since 2022-10-27
 */
@Slf4j
@Service("IScmWKpplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmWKpplanServiceImpl extends ServiceImpl<ScmWKpplanMapper, ScmWKpplan> implements IScmWKpplanService {


@Override
public IPage<ScmWKpplan> findScmWKpplans(QueryRequest request, ScmWKpplan scmWKpplan){
        try{
        LambdaQueryWrapper<ScmWKpplan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmWKpplan::getIsDeletemark, 1);//1是未删 0是已删

                if(StringUtils.isNotEmpty(scmWKpplan.getGysname())){
                        queryWrapper.eq(ScmWKpplan::getGysname,scmWKpplan.getGysname());
                }
                if(scmWKpplan.getId()!=null){
                        queryWrapper.eq(ScmWKpplan::getId,scmWKpplan.getId());
                }
                if (StringUtils.isNotBlank(scmWKpplan.getCreateTimeFrom()) ) {
                        queryWrapper
                                .ge(ScmWKpplan::getCreateTime, scmWKpplan.getCreateTimeFrom())
                        ;
                }
                if(StringUtils.isNotBlank(scmWKpplan.getCreateTimeTo())){
                        queryWrapper.le(ScmWKpplan::getCreateTime, scmWKpplan.getCreateTimeTo()+" 23:59:59");
                }

        Page<ScmWKpplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmWKpplan> findScmWKpplanList (QueryRequest request, ScmWKpplan scmWKpplan){
        try{
        Page<ScmWKpplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmWKpplan(page,scmWKpplan);
        }catch(Exception e){
        log.error("获取HIS开票清单失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmWKpplan(ScmWKpplan scmWKpplan){
                scmWKpplan.setCreateTime(new Date());
        scmWKpplan.setIsDeletemark(1);
        this.save(scmWKpplan);
        }

@Override
@Transactional
public void updateScmWKpplan(ScmWKpplan scmWKpplan){
        scmWKpplan.setModifyTime(new Date());
        this.baseMapper.updateScmWKpplan(scmWKpplan);
        }

@Override
@Transactional
public void deleteScmWKpplans(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }