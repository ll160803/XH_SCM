package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBGysfp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-10
 */
public interface ScmBGysfpMapper extends BaseMapper<ScmBGysfp> {
    void updateScmBGysfp(ScmBGysfp scmBGysfp);

    IPage<ScmBGysfp> findScmBGysfp(Page page, @Param("scmBGysfp") ScmBGysfp scmBGysfp);

    @Select("SELECT count(1) FROM `scm_b_gysfp` WHERE fp_hm=#{fphm} and GYSACCOUNT=#{gys} and DATE_FORMAT(fprq,'%Y')=#{year}")
    Integer IsExist(@Param("fphm") String fphm, @Param("gys") String gys, @Param("year") String year);

    @Select("SELECT count(1) FROM `scm_b_gysfp` WHERE fp_hm=#{fphm} and GYSACCOUNT=#{gys} and id!=#{id} and DATE_FORMAT(fprq,'%Y')=#{year} ")
    Integer IsExistById(@Param("fphm") String fphm, @Param("gys") String gys, @Param("id") String id, @Param("year") String year);

    @Update("update scm_b_gysfp set STATE=#{state} where fp_hm=#{fphm} and GYSACCOUNT=#{gys}")
    void updateFpState(@Param("fphm") String fphm, @Param("gys") String gys, @Param("state") int state);

}
