package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import cc.mrbird.febs.scm.entity.ScmBSupplyplanD;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 供应计划子表 服务类
 * </p>
 *
 * @author viki
 * @since 2020-08-19
 */
public interface IScmBSupplyplanDService extends IService<ScmBSupplyplanD> {

        IPage<ScmBSupplyplanD> findScmBSupplyplanDs(QueryRequest request, ScmBSupplyplanD scmBSupplyplanD);

        IPage<ScmBSupplyplanD> findScmBSupplyplanDList(QueryRequest request, ScmBSupplyplanD scmBSupplyplanD);

        void createScmBSupplyplanD(ScmBSupplyplanD scmBSupplyplanD);

        void updateScmBSupplyplanD(ScmBSupplyplanD scmBSupplyplanD);

        void deleteScmBSupplyplanDs(String[]Ids);

        void HandlePackage(ScmBSupplyplan scmBSupplyplan);
        }
