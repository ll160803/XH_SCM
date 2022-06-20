package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmBSapplan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * sap供应计划 服务类
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */
public interface IScmBSapplanService extends IService<ScmBSapplan> {

        IPage<ScmBSapplan> findScmBSapplans(QueryRequest request, ScmBSapplan scmBSapplan);

        IPage<ScmBSapplan> findScmBSapplanList(QueryRequest request, ScmBSapplan scmBSapplan);

        void createScmBSapplan(ScmBSapplan scmBSapplan);

        void updateScmBSapplan(ScmBSapplan scmBSapplan);

        void deleteScmBSapplans(String[]Ids);
        }
