package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBFpplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 开票清单 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-04-08
 */
public interface ScmBFpplanMapper extends BaseMapper<ScmBFpplan> {
    void updateScmBFpplan(ScmBFpplan scmBFpplan);

    IPage<ScmBFpplan> findScmBFpplan(Page page, @Param("scmBFpplan") ScmBFpplan scmBFpplan);

    void updateSupplyPlan2(@Param("ids") List<Long> ids, @Param("id") String id);
    void updateSapPlan(@Param("ids") List<Long> ids, @Param("id") String id);


    @Update("UPDATE scm_b_fpplan set IS_DELETEMARK=0 where ID=#{lid};UPDATE scm_b_supplyplan SET CODE = '',FPHM = '',fprq= null WHERE CODE=#{id}  and status=1 ;UPDATE scm_b_sapplan SET CODE = '',FP_HM = '',fprq= null WHERE CODE=#{id} ")
    void updateDeletePlan(@Param("id") String id, @Param("lid") Long lid);

    /*
    已经收获都不可以更改
     */
    @Update("UPDATE scm_b_supplyplan SET CODE = '' WHERE CODE =#{ids} and status=1 ;UPDATE scm_b_sapplan SET CODE = '' WHERE CODE =#{ids} ")
    int removeOrderCode(@Param("ids") String ids);

    @Select("Select id from scm_b_supplyplan where CODE=#{sendCode} union all SELECT CONCAT(plan_id,'*') id  from scm_b_sapplan where CODE=#{sendCode}")
    List<String> findPlanIds(@Param("sendCode") String sendCode);


    @Update("UPDATE scm_b_supplyplan SET fprq = #{fprq},fphm=#{fphm} WHERE CODE =#{code} and status=1;update scm_b_sapplan set fprq = #{fprq},fp_hm=#{fphm} where code=#{code} ")
    int updateFpData(@Param("code") String code, @Param("fprq") String fprq, @Param("fphm") String fphm);

    @Update("update scm_b_fpplan set fprq=null,fphm=null where id=#{id}")
    void updateFpplan(@Param("id") Long id);
}
