package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ViewSupplyplanNew;
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
 * @since 2022-06-09
 */
public interface IViewSupplyplanNewService extends IService<ViewSupplyplanNew> {

        IPage<ViewSupplyplanNew> findViewSupplyplanNews(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew);

        IPage<ViewSupplyplanNew> findViewSupplyplanNewList(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew);

        void createViewSupplyplanNew(ViewSupplyplanNew viewSupplyplanNew);

        void updateViewSupplyplanNew(ViewSupplyplanNew viewSupplyplanNew);

        void deleteViewSupplyplanNews(String[]Ids);

        IPage<ViewSupplyplanNew> findViewSupplyplans_byMaterCode(QueryRequest request, ViewSupplyplanNew viewSupplyplan);
        IPage<ViewSupplyplanNew> findViewSupplyplans_byCode(QueryRequest request, ViewSupplyplanNew viewSupplyplan);
        }
