package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmDVendor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 供应商表 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2019-11-14
 */
public interface ScmDVendorMapper extends BaseMapper<ScmDVendor> {
        void updateScmDVendor(ScmDVendor scmDVendor);
        }
