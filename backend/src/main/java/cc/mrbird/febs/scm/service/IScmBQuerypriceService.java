package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmBQueryprice;
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
 * @since 2019-12-26
 */
public interface IScmBQuerypriceService extends IService<ScmBQueryprice> {

        IPage<ScmBQueryprice> findScmBQueryprices(QueryRequest request, ScmBQueryprice scmBQueryprice);

        void createScmBQueryprice(ScmBQueryprice scmBQueryprice);

        void updateScmBQueryprice(ScmBQueryprice scmBQueryprice);

        void deleteScmBQueryprices(String[]Ids);
        }
