package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

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


        List<String> findChargeByBaseId(String base_Id,String account);
}
