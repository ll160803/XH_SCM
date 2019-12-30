package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.dao.ScmBQuerypriceDMapper;
import cc.mrbird.febs.scm.entity.ScmBQueryprice;
import cc.mrbird.febs.scm.dao.ScmBQuerypriceMapper;
import cc.mrbird.febs.scm.entity.ScmBQuerypriceD;
import cc.mrbird.febs.scm.service.IScmBQuerypriceService;
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

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2019-12-26
 */
@Slf4j
@Service("IScmBQuerypriceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmBQuerypriceServiceImpl extends ServiceImpl<ScmBQuerypriceMapper, ScmBQueryprice> implements IScmBQuerypriceService {


    @Autowired
    private ScmBQuerypriceDMapper scmBQuerypriceDMapper;

    @Override
    public IPage<ScmBQueryprice> findScmBQueryprices(QueryRequest request, ScmBQueryprice scmBQueryprice) {
        try {
            LambdaQueryWrapper<ScmBQueryprice> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(scmBQueryprice.getCode())) {
                queryWrapper.eq(ScmBQueryprice::getCode, scmBQueryprice.getCode());
            }
            queryWrapper.eq(ScmBQueryprice::getIsDeletemark, 1);//1是未删 0是已删
            Page<ScmBQueryprice> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createScmBQueryprice(ScmBQueryprice scmBQueryprice) {
        scmBQueryprice.setCreateTime(new Date());
        scmBQueryprice.setIsDeletemark(1);
        this.save(scmBQueryprice);
    }

    @Override
    @Transactional
    public void createScmBQuerypriceNew(List<ScmBQueryprice> maters, List<ScmBQuerypriceD> gys, Long userid,Long deptid,int state) {
        for (ScmBQueryprice scmBQueryprice : maters
        ) {
            scmBQueryprice.setCreateTime(new Date());
            scmBQueryprice.setIsDeletemark(1);
            scmBQueryprice.setCreateUserId(userid);
            scmBQueryprice.setDeptId(deptid);
            scmBQueryprice.setState(state);
            scmBQueryprice.setQueryState(state);
            this.save(scmBQueryprice);
            if(gys!=null) {
                for (ScmBQuerypriceD scmBQuerypriceD : gys) {
                    scmBQuerypriceD.setId(UUID.randomUUID().toString());
                    scmBQuerypriceD.setCreateTime(new Date());
                    scmBQuerypriceD.setIsDeletemark(1);
                    scmBQuerypriceD.setBaseId(scmBQueryprice.getId());
                    this.scmBQuerypriceDMapper.insert(scmBQuerypriceD);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateScmBQueryprice(ScmBQueryprice scmBQueryprice) {
        scmBQueryprice.setModifyTime(new Date());
        this.baseMapper.updateScmBQueryprice(scmBQueryprice);
    }

    @Override
    @Transactional
    public void deleteScmBQueryprices(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}