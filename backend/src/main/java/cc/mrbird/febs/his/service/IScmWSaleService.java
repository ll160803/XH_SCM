package cc.mrbird.febs.his.service;

import cc.mrbird.febs.his.entity.ScmWSale;
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
public interface IScmWSaleService extends IService<ScmWSale> {

        IPage<ScmWSale> findScmWSales(QueryRequest request, ScmWSale scmWSale);

        IPage<ScmWSale> findScmWSaleList(QueryRequest request, ScmWSale scmWSale);

        void createScmWSale(ScmWSale scmWSale);

        void updateScmWSale(ScmWSale scmWSale);

        void deleteScmWSales(String[]Ids);
        }
