package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;
import cc.mrbird.febs.scm.dao.ScmBGysMaterPicMapper;
import cc.mrbird.febs.scm.service.IScmBGysMaterPicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/**
 * <p>
 * 资质文件记录表 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
@Slf4j
@Service("IScmBGysMaterPicService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBGysMaterPicServiceImpl extends ServiceImpl<ScmBGysMaterPicMapper, ScmBGysMaterPic> implements IScmBGysMaterPicService {


@Override
public IPage<ScmBGysMaterPic> findScmBGysMaterPics(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic){
        try{
        LambdaQueryWrapper<ScmBGysMaterPic> queryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(scmBGysMaterPic.getCode())) {
        queryWrapper.eq(ScmBGysMaterPic::getCode, scmBGysMaterPic.getCode());
        }
        Page<ScmBGysMaterPic> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createScmBGysMaterPic(ScmBGysMaterPic scmBGysMaterPic){
        scmBGysMaterPic.setId(UUID.randomUUID().toString());
        scmBGysMaterPic.setCreateTime(new Date());
        this.save(scmBGysMaterPic);
        }

@Override
@Transactional
public void updateScmBGysMaterPic(ScmBGysMaterPic scmBGysMaterPic){
        scmBGysMaterPic.setModifyTime(new Date());
        this.baseMapper.updateScmBGysMaterPic(scmBGysMaterPic);
        }

@Override
@Transactional
public void deleteScmBGysMaterPics(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }