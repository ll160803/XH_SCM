package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmBSendorder;
import cc.mrbird.febs.scm.dao.ScmBSendorderMapper;
import cc.mrbird.febs.scm.service.IScmBSendorderService;
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
 * 药品的送货清单 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-26
 */
@Slf4j
@Service("IScmBSendorderService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBSendorderServiceImpl extends ServiceImpl<ScmBSendorderMapper, ScmBSendorder> implements IScmBSendorderService {


@Override
public IPage<ScmBSendorder> findScmBSendorders(QueryRequest request, ScmBSendorder scmBSendorder){
        try{
        LambdaQueryWrapper<ScmBSendorder> queryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(scmBSendorder.getCode())) {
        queryWrapper.eq(ScmBSendorder::getCode, scmBSendorder.getCode());
        }
        queryWrapper.eq(ScmBSendorder::getIsDeletemark, 1);
        Page<ScmBSendorder> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createScmBSendorder(ScmBSendorder scmBSendorder){
        //scmBSendorder.setId(UUID.randomUUID().toString());
        scmBSendorder.setIsDeletemark(1);
        scmBSendorder.setCreateTime(new Date());
        this.save(scmBSendorder);
        }

@Override
@Transactional
public void updateScmBSendorder(ScmBSendorder scmBSendorder){
        scmBSendorder.setModifyTime(new Date());
        this.baseMapper.updateScmBSendorder(scmBSendorder);
        }

@Override
@Transactional
public void deleteScmBSendorders(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }