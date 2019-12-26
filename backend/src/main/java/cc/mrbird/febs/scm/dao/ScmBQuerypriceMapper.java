package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBQueryprice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-12-26
 */
public interface ScmBQuerypriceMapper extends BaseMapper<ScmBQueryprice> {
        void updateScmBQueryprice(ScmBQueryprice scmBQueryprice);
        }
