package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 资质文件记录表 服务类
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
public interface IScmBGysMaterPicService extends IService<ScmBGysMaterPic> {

        IPage<ScmBGysMaterPic> findScmBGysMaterPics(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic);

        void createScmBGysMaterPic(ScmBGysMaterPic scmBGysMaterPic) throws FebsException ;

        void updateScmBGysMaterPic(ScmBGysMaterPic scmBGysMaterPic) throws FebsException;

        void deleteScmBGysMaterPics(String[]Ids);
        }
