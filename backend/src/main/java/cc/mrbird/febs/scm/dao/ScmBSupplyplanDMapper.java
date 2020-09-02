package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSupplyplanD;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;

/**
 * <p>
 * 供应计划子表 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-08-19
 */
public interface ScmBSupplyplanDMapper extends BaseMapper<ScmBSupplyplanD> {
        void updateScmBSupplyplanD(ScmBSupplyplanD scmBSupplyplanD);
        IPage<ScmBSupplyplanD> findScmBSupplyplanD(Page page, @Param("scmBSupplyplanD") ScmBSupplyplanD scmBSupplyplanD);

        @Delete("delete from scm_b_supplyplan_d  where base_id=#{baseId}")
        void deleteByBaseId(@Param("baseId") String baseId );
        }
