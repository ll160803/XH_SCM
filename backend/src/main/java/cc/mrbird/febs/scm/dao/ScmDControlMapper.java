package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmDControl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-04-07
 */
public interface ScmDControlMapper extends BaseMapper<ScmDControl> {
        void updateScmDControl(ScmDControl scmDControl);
        IPage<ScmDControl> findScmDControl(Page page, @Param("scmDControl") ScmDControl scmDControl);
        @Update("update scm_d_control set end_time=#{scmDControl.endTime}")
        void updateEndTime(@Param("scmDControl") ScmDControl scmDControl);
        }
