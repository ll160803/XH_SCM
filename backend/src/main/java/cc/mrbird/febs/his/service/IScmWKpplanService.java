package cc.mrbird.febs.his.service;

import cc.mrbird.febs.his.entity.ScmWKpplan;
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
 * @since 2022-10-27
 */
public interface IScmWKpplanService extends IService<ScmWKpplan> {

        IPage<ScmWKpplan> findScmWKpplans(QueryRequest request, ScmWKpplan scmWKpplan);

        IPage<ScmWKpplan> findScmWKpplanList(QueryRequest request, ScmWKpplan scmWKpplan);

        void createScmWKpplan(ScmWKpplan scmWKpplan);

        void updateScmWKpplan(ScmWKpplan scmWKpplan);

        void deleteScmWKpplans(String[]Ids);
        }
