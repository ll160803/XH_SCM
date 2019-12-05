package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.dao.ViewSupplyplanMapper;
import cc.mrbird.febs.scm.service.IViewSupplyplanService;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-12-05
 */
@Slf4j
@Service("IViewSupplyplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ViewSupplyplanServiceImpl extends ServiceImpl<ViewSupplyplanMapper, ViewSupplyplan> implements IViewSupplyplanService {


@Override
public IPage<ViewSupplyplan> findViewSupplyplans(QueryRequest request, ViewSupplyplan viewSupplyplan){
        try{
        LambdaQueryWrapper<ViewSupplyplan> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ViewSupplyplan::getIsDeletemark, 1);//1是未删 0是已删
        Page<ViewSupplyplan> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createViewSupplyplan(ViewSupplyplan viewSupplyplan){

        viewSupplyplan.setCreateTime(new Date());
        viewSupplyplan.setIsDeletemark(1);
        this.save(viewSupplyplan);
        }

@Override
@Transactional
public void updateViewSupplyplan(ViewSupplyplan viewSupplyplan){
        viewSupplyplan.setModifyTime(new Date());
        this.baseMapper.updateViewSupplyplan(viewSupplyplan);
        }

@Override
@Transactional
public void deleteViewSupplyplans(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }