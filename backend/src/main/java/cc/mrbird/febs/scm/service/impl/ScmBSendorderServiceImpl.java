package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmBSupplyplanMapper;
import cc.mrbird.febs.scm.entity.ScmBSendorder;
import cc.mrbird.febs.scm.dao.ScmBSendorderMapper;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import cc.mrbird.febs.scm.service.IScmBSendorderService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private ScmBSupplyplanMapper scmBSupplyplanMapper;

    @Override
    public IPage<ScmBSendorder> findScmBSendorders(QueryRequest request, ScmBSendorder scmBSendorder) {
        try {
//        LambdaQueryWrapper<ScmBSendorder> queryWrapper=new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(scmBSendorder.getCode())) {
//        queryWrapper.eq(ScmBSendorder::getCode, scmBSendorder.getCode());
//        }
//        queryWrapper.eq(ScmBSendorder::getIsDeletemark, 1);
//        Page<ScmBSendorder> page=new Page<>();
//        SortUtil.handlePageSort(request,page,true);
// return this.page(page,queryWrapper);
            Page<ScmBSendorder> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            // return this.page(page, queryWrapper);
            return this.baseMapper.findSendInfos(page, scmBSendorder);

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmBSendorder(ScmBSendorder scmBSendorder) {
        //scmBSendorder.setId(UUID.randomUUID().toString());
        scmBSendorder.setIsDeletemark(1);
        scmBSendorder.setCreateTime(new Date());
        this.save(scmBSendorder);
        String supplyPlanIds = scmBSendorder.supplyPlanIds;

        if (StringUtils.isNotBlank(supplyPlanIds)) {
            String[] arr_ids = supplyPlanIds.split(StringPool.COMMA);

            List<Long> ids = new ArrayList<>();
            for (String idStr : arr_ids
            ) {
                ids.add(Long.parseLong(idStr));
            }
            if (scmBSendorder.getBsart() == "1") {//物资
                String fphm = scmBSendorder.getFphm();
                this.baseMapper.updateSupplyPlan(ids, scmBSendorder.getId().toString(), fphm);
            }
            else//药品
            {
                this.baseMapper.updateSupplyPlan2(ids, scmBSendorder.getId().toString());
            }
        }
    }

    @Override
    @Transactional
    public void updateScmBSendorder(ScmBSendorder scmBSendorder) {
        scmBSendorder.setModifyTime(new Date());
        this.baseMapper.updateScmBSendorder(scmBSendorder);
    }

    /*
    设置为0 并清除供应计划里的发票号码值
     */
    @Override
    @Transactional
    public void deleteScmBSendorders(String[] Ids) {
        for (String id :
                Ids) {
            this.baseMapper.updateDeleteOrder(id, Long.parseLong(id));
        }

    }


}