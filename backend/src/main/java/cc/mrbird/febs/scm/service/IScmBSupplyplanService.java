package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 供应计划 服务类
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface IScmBSupplyplanService extends IService<ScmBSupplyplan> {

        IPage<ScmBSupplyplan> findScmBSupplyplans(QueryRequest request, ScmBSupplyplan scmBSupplyplan);
        IPage<ScmBSupplyplan> findSupplyplans(QueryRequest request, ScmBSupplyplan scmBSupplyplan);

        void createScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) throws FebsException;

        void updateScmBSupplyplan(ScmBSupplyplan scmBSupplyplan) throws FebsException;

        void updateSupplyplanOnly(ScmBSupplyplan scmBSupplyplan) throws FebsException;

        void deleteScmBSupplyplans(String[]Ids);
        }
