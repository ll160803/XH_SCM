package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2019-12-05
 */
public interface IViewSupplyplanService extends IService<ViewSupplyplan> {

        IPage<ViewSupplyplan> findViewSupplyplans(QueryRequest request, ViewSupplyplan viewSupplyplan);

        void createViewSupplyplan(ViewSupplyplan viewSupplyplan);

        void updateViewSupplyplan(ViewSupplyplan viewSupplyplan);

        void deleteViewSupplyplans(String[]Ids);
        }
