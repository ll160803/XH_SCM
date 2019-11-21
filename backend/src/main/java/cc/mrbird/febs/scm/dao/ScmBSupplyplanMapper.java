package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 供应计划 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface ScmBSupplyplanMapper extends BaseMapper<ScmBSupplyplan> {
        void updateScmBSupplyplan(ScmBSupplyplan scmBSupplyplan);
        }
