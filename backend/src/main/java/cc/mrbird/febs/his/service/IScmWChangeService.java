package cc.mrbird.febs.his.service;

import cc.mrbird.febs.his.entity.ScmWChange;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
public interface IScmWChangeService extends IService<ScmWChange> {

        IPage<ScmWChange> findScmWChanges(QueryRequest request, ScmWChange scmWChange);

        IPage<ScmWChange> findScmWChangeList(QueryRequest request, ScmWChange scmWChange);

        void createScmWChange(ScmWChange scmWChange);

        void updateScmWChange(ScmWChange scmWChange);

        void deleteScmWChanges(String[]Ids);
        }
