package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmDVendorDMapper;
import cc.mrbird.febs.scm.entity.ScmDVendor;
import cc.mrbird.febs.scm.dao.ScmDVendorMapper;
import cc.mrbird.febs.scm.entity.ScmDVendorD;
import cc.mrbird.febs.scm.service.IScmDVendorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Service("IScmDVendorService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmDVendorServiceImpl extends ServiceImpl<ScmDVendorMapper, ScmDVendor> implements IScmDVendorService {

    @Autowired
    private ScmDVendorDMapper scmDVendorDMapper;

    @Override
    public IPage<ScmDVendor> findScmDVendors(QueryRequest request, ScmDVendor scmDVendor) {
        try {
            LambdaQueryWrapper<ScmDVendor> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(scmDVendor.getName())) {
                queryWrapper.like(ScmDVendor::getName, scmDVendor.getName());
            }
            if (StringUtils.isNotBlank(scmDVendor.getCode())) {
                queryWrapper.eq(ScmDVendor::getCode, scmDVendor.getCode());
            }
            if (scmDVendor.getLb() != null) {
                queryWrapper.eq(ScmDVendor::getLb, scmDVendor.getLb());
            }
            Page<ScmDVendor> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmDVendor(ScmDVendor scmDVendor) {
        scmDVendor.setId(UUID.randomUUID().toString());
        scmDVendor.setCreateTime(new Date());
        this.save(scmDVendor);
    }

    @Override
    @Transactional
    public void updateScmDVendor(ScmDVendor scmDVendor) {
        scmDVendor.setModifyTime(new Date());
        this.baseMapper.updateScmDVendor(scmDVendor);
    }

    @Override
    @Transactional
    public void deleteScmDVendors(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void createScmVendor(ScmDVendor scmDVendor, List<ScmDVendorD> scmDVendorDS) {
        String F_id = UUID.randomUUID().toString();
        scmDVendor.setId(F_id);
        scmDVendor.setCreateTime(new Date());
        scmDVendor.setState(0);//不可用
        scmDVendor.setIsDeletemark(1);
        scmDVendor.setFileState(0);
        scmDVendor.setJieKouState(0);
        this.save(scmDVendor);
        for (ScmDVendorD scmDVendorD :
                scmDVendorDS) {
            scmDVendorD.setId(UUID.randomUUID().toString());
            scmDVendorD.setBaseId(F_id);
            scmDVendorDMapper.insert(scmDVendorD);
        }
    }
    @Override
    @Transactional
    public void updateScmDVendor(ScmDVendor scmDVendor, List<ScmDVendorD> scmDVendorDS) {
        scmDVendor.setModifyTime(new Date());
        this.updateScmDVendor(scmDVendor);
        QueryWrapper<ScmDVendorD> queryWrapper = new QueryWrapper<>();
        String F_id= scmDVendor.getId();
        queryWrapper.lambda().eq(ScmDVendorD::getBaseId,F_id);
        scmDVendorDMapper.delete(queryWrapper);
        for (ScmDVendorD scmDVendorD :
                scmDVendorDS) {
            scmDVendorD.setId(UUID.randomUUID().toString());
            scmDVendorD.setBaseId(scmDVendor.getId());
            scmDVendorDMapper.insert(scmDVendorD);
        }
    }
}