package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSapplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * sap供应计划 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */
public interface ScmBSapplanMapper extends BaseMapper<ScmBSapplan> {
        void updateScmBSapplan(ScmBSapplan scmBSapplan);
        IPage<ScmBSapplan> findScmBSapplan(Page page, @Param("scmBSapplan") ScmBSapplan scmBSapplan);
        }
