package cc.mrbird.febs.his.dao;

import cc.mrbird.febs.his.entity.ScmWSale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
public interface ScmWSaleMapper extends BaseMapper<ScmWSale> {
        void updateScmWSale(ScmWSale scmWSale);
        IPage<ScmWSale> findScmWSale(Page page, @Param("scmWSale") ScmWSale scmWSale);
        }
