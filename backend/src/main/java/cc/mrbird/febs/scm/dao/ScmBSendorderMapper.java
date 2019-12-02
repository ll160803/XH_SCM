package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmBSendorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.EAN;

import java.util.List;

/**
 * <p>
 * 药品的送货清单 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-26
 */
public interface ScmBSendorderMapper extends BaseMapper<ScmBSendorder> {
        void updateScmBSendorder(ScmBSendorder scmBSendorder);

        @Update("UPDATE scm_b_sendorder set IS_DELETEMARK=0 where ID=#{lid};UPDATE scm_b_supplyplan SET SEND_ORDER_CODE = '',FPHM = '' WHERE SEND_ORDER_CODE=#{id} ")
        void updateDeleteOrder(@Param("id") String id,@Param("lid") Long lid);

        void updateSupplyPlan(@Param("ids") List<Long> ids,@Param("id") String id,@Param("fphm") String fphm);
        IPage<ScmBSendorder> findSendInfos(Page page, @Param("order") ScmBSendorder order);
        }
