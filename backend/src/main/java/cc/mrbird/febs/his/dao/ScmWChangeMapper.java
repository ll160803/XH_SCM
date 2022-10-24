package cc.mrbird.febs.his.dao;

import cc.mrbird.febs.his.entity.ScmWChange;
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
public interface ScmWChangeMapper extends BaseMapper<ScmWChange> {
        void updateScmWChange(ScmWChange scmWChange);
        IPage<ScmWChange> findScmWChange(Page page, @Param("scmWChange") ScmWChange scmWChange);
        }
