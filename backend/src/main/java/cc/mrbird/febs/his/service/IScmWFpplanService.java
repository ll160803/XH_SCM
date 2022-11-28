package cc.mrbird.febs.his.service;

import cc.mrbird.febs.his.entity.ScmWFpplan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * HIS开票清单 服务类
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
public interface IScmWFpplanService extends IService<ScmWFpplan> {

        IPage<ScmWFpplan> findScmWFpplans(QueryRequest request, ScmWFpplan scmWFpplan);

        IPage<ScmWFpplan> findScmWFpplanList(QueryRequest request, ScmWFpplan scmWFpplan);

        void createScmWFpplan(ScmWFpplan scmWFpplan);

        void updateScmWFpplan(ScmWFpplan scmWFpplan);

        void deleteScmWFpplans(String[]Ids);
        }
