package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmDArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 药房院区表 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-11
 */
public interface ScmDAreaMapper extends BaseMapper<ScmDArea> {
        void updateScmDArea(ScmDArea scmDArea);
        }
