package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-12-05
 */
public interface ViewSupplyplanMapper extends BaseMapper<ViewSupplyplan> {
        void updateViewSupplyplan(ViewSupplyplan viewSupplyplan);

        @Select("select *  from view_supplyplan where id in (${ids})")
        List<ViewSupplyplan> GetViewSupplyPlanByIds(@Param(value = "ids") String ids);

        }
