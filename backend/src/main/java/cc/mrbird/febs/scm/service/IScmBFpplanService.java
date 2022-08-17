package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmBFpplan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 开票清单 服务类
 * </p>
 *
 * @author viki
 * @since 2022-04-07
 */
public interface IScmBFpplanService extends IService<ScmBFpplan> {

        IPage<ScmBFpplan> findScmBFpplans(QueryRequest request, ScmBFpplan scmBFpplan);

        IPage<ScmBFpplan> findScmBFpplans2(QueryRequest request, ScmBFpplan scmBFpplan);

        IPage<ScmBFpplan> findScmBFpplanList(QueryRequest request, ScmBFpplan scmBFpplan);

        void createScmBFpplan(ScmBFpplan scmBFpplan);

        void updateScmBFpplan(ScmBFpplan scmBFpplan);

        void deleteScmBFpplans(String[]Ids);

        List<String> findPlanIds(String sendCode);

        void updateFpData(String code,String fprq,String fphm);

        void updateFpplan(String id);

        void updateDeletePlan(String code, Long id);

        }
