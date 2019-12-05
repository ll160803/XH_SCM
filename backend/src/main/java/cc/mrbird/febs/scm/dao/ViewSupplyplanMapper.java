package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
        }
