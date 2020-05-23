package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

        @Select("select count(1) as coun  from scm_b_gys_mater_pic \n" +
                "inner join scm_b_purcharseorder on scm_b_gys_mater_pic.matnr=scm_b_purcharseorder.matnr\n" +
                " and scm_b_gys_mater_pic.GYSACCOUNT=scm_b_purcharseorder.lifnr \n" +
                " inner join scm_b_supplyplan on scm_b_purcharseorder.ID=scm_b_supplyplan.BASE_ID\n" +
                " and scm_b_gys_mater_pic.CHARGE=scm_b_supplyplan.CHARGE\n" +
                " WHERE scm_b_gys_mater_pic.ID=#{id} ")
        Integer getCount(@Param("id") String  id);
}
