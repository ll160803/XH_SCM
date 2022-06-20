package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ViewSupplyplanNew;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */
public interface ViewSupplyplanNewMapper extends BaseMapper<ViewSupplyplanNew> {
        void updateViewSupplyplanNew(ViewSupplyplanNew viewSupplyplanNew);
        IPage<ViewSupplyplanNew> findViewSupplyplanNew(Page page, @Param("viewSupplyplanNew") ViewSupplyplanNew viewSupplyplanNew);
        int findViewSupplyplanNewCount(@Param("viewSupplyplanNew") ViewSupplyplanNew viewSupplyplanNew);

        }
