package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import cc.mrbird.febs.scm.entity.ScmBSupplyplanD;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanDMapper;
import cc.mrbird.febs.scm.service.IScmBSupplyplanDService;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
/**
 * <p>
 * 供应计划子表 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-19
 */
@Slf4j
@Service("IScmBSupplyplanDService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBSupplyplanDServiceImpl extends ServiceImpl<ScmBSupplyplanDMapper, ScmBSupplyplanD> implements IScmBSupplyplanDService {


@Override
public IPage<ScmBSupplyplanD> findScmBSupplyplanDs(QueryRequest request, ScmBSupplyplanD scmBSupplyplanD){
        try{
        LambdaQueryWrapper<ScmBSupplyplanD> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmBSupplyplanD::getIsDeletemark, 1);//1是未删 0是已删

                                if (scmBSupplyplanD.getState()!=null) {
                                queryWrapper.eq(ScmBSupplyplanD::getState, scmBSupplyplanD.getState());
                                }
                                if (StringUtils.isNotBlank(scmBSupplyplanD.getCreateTimeFrom()) && StringUtils.isNotBlank(scmBSupplyplanD.getCreateTimeTo())) {
                                queryWrapper
                                .ge(ScmBSupplyplanD::getCreateTime, scmBSupplyplanD.getCreateTimeFrom())
                                .le(ScmBSupplyplanD::getCreateTime, scmBSupplyplanD.getCreateTimeTo());
                                }
                                if (StringUtils.isNotBlank(scmBSupplyplanD.getModifyTimeFrom()) && StringUtils.isNotBlank(scmBSupplyplanD.getModifyTimeTo())) {
                                queryWrapper
                                .ge(ScmBSupplyplanD::getModifyTime, scmBSupplyplanD.getModifyTimeFrom())
                                .le(ScmBSupplyplanD::getModifyTime, scmBSupplyplanD.getModifyTimeTo());
                                }

        Page<ScmBSupplyplanD> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<ScmBSupplyplanD> findScmBSupplyplanDList (QueryRequest request, ScmBSupplyplanD scmBSupplyplanD){
        try{
        Page<ScmBSupplyplanD> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findScmBSupplyplanD(page,scmBSupplyplanD);
        }catch(Exception e){
        log.error("获取供应计划子表失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createScmBSupplyplanD(ScmBSupplyplanD scmBSupplyplanD){
                scmBSupplyplanD.setId(UUID.randomUUID().toString());
        scmBSupplyplanD.setCreateTime(new Date());
        scmBSupplyplanD.setIsDeletemark(1);
        this.save(scmBSupplyplanD);
        }

@Override
@Transactional
public void updateScmBSupplyplanD(ScmBSupplyplanD scmBSupplyplanD){
        scmBSupplyplanD.setModifyTime(new Date());
        this.baseMapper.updateScmBSupplyplanD(scmBSupplyplanD);
        }

@Override
@Transactional
public void deleteScmBSupplyplanDs(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }
    @Override
    @Transactional
   public void HandlePackage(ScmBSupplyplan scmBSupplyplan){
        //删除已经存在的
        this.baseMapper.deleteByBaseId(scmBSupplyplan.getId().toString());
        /** 增加箱数的设计 */
        String str_num = scmBSupplyplan.getPkgNumber().toString();
        String gyjhId = scmBSupplyplan.getId().toString();
        int num = Convert.toInt(str_num);
        log.error(num+"");
        BigDecimal menge_d = scmBSupplyplan.getPkgAmount();
        BigDecimal leftMenge=  NumberUtil.sub(scmBSupplyplan.getgMenge(),NumberUtil.mul( new BigDecimal(num-1) , menge_d));
        log.error(leftMenge.toString());
        for (int i = 1; i <= num; i++) {
            ScmBSupplyplanD scmd = new ScmBSupplyplanD();
            scmd.setId(gyjhId+"_" +  i);
            if(i==num){
                scmd.setMenge(leftMenge);
            }
            else {
                scmd.setMenge(menge_d);
            }
            scmd.setBaseId(scmBSupplyplan.getId());
            scmd.setState(0);
            scmd.setIsDeletemark(1);
            scmd.setCreateTime(new Date());
            scmd.setModifyTime(new Date());
            this.baseMapper.insert(scmd);
        }
    }
        }