package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmDControl;
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
 * @since 2022-04-07
 */
public interface IScmDControlService extends IService<ScmDControl> {

        IPage<ScmDControl> findScmDControls(QueryRequest request, ScmDControl scmDControl);

        IPage<ScmDControl> findScmDControlList(QueryRequest request, ScmDControl scmDControl);

        void createScmDControl(ScmDControl scmDControl);

        void updateScmDControl(ScmDControl scmDControl);

        void deleteScmDControls(String[]Ids);

        void updateEndTime(ScmDControl scmDControl);
        }
