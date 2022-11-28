package cc.mrbird.febs.his.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.his.entity.ScmWChange;
import cc.mrbird.febs.his.dao.ScmWChangeMapper;
import cc.mrbird.febs.his.service.IScmWChangeService;
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
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
@Slf4j
@Service("IScmWChangeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmWChangeServiceImpl extends ServiceImpl<ScmWChangeMapper, ScmWChange> implements IScmWChangeService {


    @Override
    public IPage<ScmWChange> findScmWChanges(QueryRequest request, ScmWChange scmWChange) {
        try {
            LambdaQueryWrapper<ScmWChange> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmWChange::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(scmWChange.getId())) {
                queryWrapper.eq(ScmWChange::getId, scmWChange.getId());
            }
            if (StringUtils.isNotBlank(scmWChange.getFyId())) {
                queryWrapper.like(ScmWChange::getFyId, scmWChange.getFyId());
            }
            if (StringUtils.isNotBlank(scmWChange.getYpMc())) {
                queryWrapper.like(ScmWChange::getYpMc, scmWChange.getYpMc());
            }
            if (StringUtils.isNotBlank(scmWChange.getGysName())) {
                queryWrapper.like(ScmWChange::getGysName, scmWChange.getGysName());
            }
            if (StringUtils.isNotBlank(scmWChange.getNy())) {
                queryWrapper.eq(ScmWChange::getNy, scmWChange.getNy());
            }
            if (StringUtils.isNotBlank(scmWChange.getCode())) {
                queryWrapper.like(ScmWChange::getCode, scmWChange.getCode());
            }
            if (scmWChange.getState() != null) {
                queryWrapper.like(ScmWChange::getState, scmWChange.getState());
            }
            Page<ScmWChange> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ScmWChange> findScmWChangeList(QueryRequest request, ScmWChange scmWChange) {
        try {
            Page<ScmWChange> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findScmWChange(page, scmWChange);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmWChange(ScmWChange scmWChange) {
        if(StringUtils.isEmpty(scmWChange.getId())) {
            scmWChange.setId(UUID.randomUUID().toString());
        }
        scmWChange.setCreateTime(new Date());
        scmWChange.setIsDeletemark(1);
        this.save(scmWChange);
    }

    @Override
    @Transactional
    public void updateScmWChange(ScmWChange scmWChange) {
        scmWChange.setModifyTime(new Date());
        this.baseMapper.updateScmWChange(scmWChange);
    }

    @Override
    @Transactional
    public void deleteScmWChanges(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}