package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBGysMaterPicService;
import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2019-11-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBGysMaterPic")

public class ScmBGysMaterPicController extends BaseController {

    private String message;
    @Autowired
    public IScmBGysMaterPicService iScmBGysMaterPicService;


    /**
     * 分页查询数据
     *
     * @param bootStrapTable  分页信息
     * @param scmBGysMaterPic 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("scmBGysMaterPic:view")
    public Map<String, Object> List(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic) {
        User currentUser = FebsUtil.getCurrentUser();
        scmBGysMaterPic.setGysaccount(currentUser.getUsername());
        return getDataTable(this.iScmBGysMaterPicService.findScmBGysMaterPics(request, scmBGysMaterPic));
    }

    /**
     * 跳转添加页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("scmBGysMaterPic:add")
    public void addScmBGysMaterPic(@Valid ScmBGysMaterPic scmBGysMaterPic) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBGysMaterPic.setCreateUserId(currentUser.getUserId());
            scmBGysMaterPic.setGysaccount(currentUser.getUsername());
            scmBGysMaterPic.setName(currentUser.getRealname());
            scmBGysMaterPic.setState(0);
            scmBGysMaterPic.setIsDeletemark(1);
            this.iScmBGysMaterPicService.createScmBGysMaterPic(scmBGysMaterPic);
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 跳转修改页面
     *
     * @param request
     * @param id      实体ID
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("scmBGysMaterPic:update")
    public void updateScmBGysMaterPic(@Valid ScmBGysMaterPic scmBGysMaterPic) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBGysMaterPic.setModifyUserId(currentUser.getUserId());
            scmBGysMaterPic.setGysaccount(currentUser.getUsername());
            scmBGysMaterPic.setName(currentUser.getRealname());
            this.iScmBGysMaterPicService.updateScmBGysMaterPic(scmBGysMaterPic);
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    /**
     * 审核数据
     *
     * @param request
     * @param id      实体ID
     * @return
     */
    @Log("审核")
    @PutMapping("audit")
    public void auditScmBGysMaterPic(@Valid ScmBGysMaterPic scmBGysMaterPic) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBGysMaterPic.setModifyUserId(currentUser.getUserId());
            this.iScmBGysMaterPicService.auditScmBGysMaterPic(scmBGysMaterPic);
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("scmBGysMaterPic:delete")
    public void deleteScmBGysMaterPics(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmBGysMaterPicService.deleteScmBGysMaterPics(arr_ids,0);
        } catch (Exception e) {
            message = "删除成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @Log("删除")
    @DeleteMapping("/audit/{ids}")
    @RequiresPermissions("scmBGysMaterPic:delete")
    public void deleteAuditScmBGysMaterPics(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmBGysMaterPicService.deleteScmBGysMaterPics(arr_ids,1);
        } catch (Exception e) {
            message = "删除成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @PostMapping("excel")
    @RequiresPermissions("scmBGysMaterPic:export")
    public void export(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic, HttpServletResponse response) throws FebsException {
        try {
            List<ScmBGysMaterPic> scmBGysMaterPics = this.iScmBGysMaterPicService.findScmBGysMaterPics(request, scmBGysMaterPic).getRecords();
            ExcelKit.$Export(ScmBGysMaterPic.class, response).downXlsx(scmBGysMaterPics, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmBGysMaterPic detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmBGysMaterPic scmBGysMaterPic = this.iScmBGysMaterPicService.getById(id);
        return scmBGysMaterPic;
    }
}