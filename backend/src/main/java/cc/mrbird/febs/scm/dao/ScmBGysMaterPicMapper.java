package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
                " inner join scm_b_supplyplan on scm_b_purcharseorder.ID=scm_b_supplyplan.BASE_ID and scm_b_supplyplan.IS_DELETEMARK=1\n" +
                " and scm_b_gys_mater_pic.CHARGE=scm_b_supplyplan.CHARGE\n" +
                " WHERE scm_b_gys_mater_pic.ID=#{id} and scm_b_gys_mater_pic.IS_DELETEMARK=1 ")
        Integer getCount(@Param("id") String  id);

        IPage<ScmBGysMaterPic> findVScmBGyspicUser(Page page, @Param("vScmBGyspicUser") ScmBGysMaterPic vScmBGyspicUser,@Param("keyword_mater") String keyword_mater,@Param("keyword_gys") String keyword_gys,@Param("userid") String userid);

        @ Select("select b.id from  scm_b_purcharseorder a\n" +
                "inner join scm_b_supplyplan b on a.ID=b.BASE_ID\n" +
                "where a.matnr=#{matnr} and b.CHARGE=#{charge} and b.GYSACCOUNT=#{gysaccount} and b.IS_DELETEMARK=1 LIMIT 0,1")
        List<String> findByCharge(@Param("matnr") String matnr,@Param("charge") String charge,@Param("gysaccount") String gysaccount);
}
