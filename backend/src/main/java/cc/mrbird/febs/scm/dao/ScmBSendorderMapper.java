package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSendorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 药品的送货清单 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-26
 */
public interface ScmBSendorderMapper extends BaseMapper<ScmBSendorder> {
        void updateScmBSendorder(ScmBSendorder scmBSendorder);
        }
