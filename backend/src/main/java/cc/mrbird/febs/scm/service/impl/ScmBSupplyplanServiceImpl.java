package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanMapper;
import cc.mrbird.febs.scm.service.IScmBSupplyplanService;
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
 * 供应计划 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
@Slf4j
@Service("IScmBSupplyplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBSupplyplanServiceImpl extends ServiceImpl<ScmBSupplyplanMapper, ScmBSupplyplan> implements IScmBSupplyplanService {


    @Override
    public IPage<ScmBSupplyplan> findScmBSupplyplans(QueryRequest request, ScmBSupplyplan scmBSupplyplan) {
        try {
            LambdaQueryWrapper<ScmBSupplyplan> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(scmBSupplyplan.getCode())) {
                queryWrapper.eq(ScmBSupplyplan::getCode, scmBSupplyplan.getCode());
            }
            if (StringUtils.isNotBlank(scmBSupplyplan.getBaseId())) {
                queryWrapper.eq(ScmBSupplyplan::getBaseId, scmBSupplyplan.getBaseId());
            }
            if (scmBSupplyplan.getIsDeletemark()!=null) {
                queryWrapper.eq(ScmBSupplyplan::getIsDeletemark, scmBSupplyplan.getIsDeletemark());
            }
            Page<ScmBSupplyplan> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) {
        //scmBSupplyplan.setId(UUID.randomUUID().toString());
        scmBSupplyplan.setCreateTime(new Date());
        this.save(scmBSupplyplan);
    }

    @Override
    @Transactional
    public void updateScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) {
        scmBSupplyplan.setModifyTime(new Date());
        this.baseMapper.updateScmBSupplyplan(scmBSupplyplan);
    }

    @Override
    @Transactional
    public void deleteScmBSupplyplans(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}