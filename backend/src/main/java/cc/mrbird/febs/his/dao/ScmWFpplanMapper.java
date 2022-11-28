package cc.mrbird.febs.his.dao;

import cc.mrbird.febs.his.entity.ScmWFpplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * HIS开票清单 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */
public interface ScmWFpplanMapper extends BaseMapper<ScmWFpplan> {
        void updateScmWFpplan(ScmWFpplan scmWFpplan);
        IPage<ScmWFpplan> findScmWFpplan(Page page, @Param("scmWFpplan") ScmWFpplan scmWFpplan);
        }
