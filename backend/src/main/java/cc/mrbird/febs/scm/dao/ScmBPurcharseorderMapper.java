package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * SCM_B_PURCHARSEORDER Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface ScmBPurcharseorderMapper extends BaseMapper<ScmBPurcharseorder> {
        void updateScmBPurcharseorder(ScmBPurcharseorder scmBPurcharseorder);
        }
