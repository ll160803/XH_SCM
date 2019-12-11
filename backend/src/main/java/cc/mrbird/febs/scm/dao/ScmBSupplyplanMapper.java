package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSupplyplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 供应计划 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface ScmBSupplyplanMapper extends BaseMapper<ScmBSupplyplan> {
        void updateScmBSupplyplan(ScmBSupplyplan scmBSupplyplan);

        Long IsOutMenge(@Param(value = "plan") ScmBSupplyplan scmBSupplyplan);


        int IsExistFphm(@Param(value="id") String id,@Param(value="fphm") String fphm,@Param(value="gys") String gys);
        }
