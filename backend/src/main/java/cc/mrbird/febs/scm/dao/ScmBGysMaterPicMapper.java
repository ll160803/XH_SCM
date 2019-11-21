package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 资质文件记录表 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface ScmBGysMaterPicMapper extends BaseMapper<ScmBGysMaterPic> {
        void updateScmBGysMaterPic(ScmBGysMaterPic scmBGysMaterPic);
        }
