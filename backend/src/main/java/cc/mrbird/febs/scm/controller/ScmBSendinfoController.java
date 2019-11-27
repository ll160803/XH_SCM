package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBSendinfoService;
import cc.mrbird.febs.scm.entity.ScmBSendinfo;

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
 * @since 2019-11-26
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSendinfo")

public class ScmBSendinfoController extends BaseController {

    private String message;
    @Autowired
    public IScmBSendinfoService iScmBSendinfoService;


    /**
     * 分页查询数据
     *
     * @param bootStrapTable 分页信息
     * @param scmBSendinfo   查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("scmBSendinfo:view")
    public Map<String, Object> List(QueryRequest request, ScmBSendinfo scmBSendinfo) {
        User currentUser = FebsUtil.getCurrentUser();
        scmBSendinfo.setGysaccount(currentUser.getUsername());
        //scmBSendinfo.setGysname(currentUser.getRealname());
        return getDataTable(this.iScmBSendinfoService.findScmBSendinfos(request, scmBSendinfo));
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
    @RequiresPermissions("scmBSendinfo:add")
    public void addScmBSendinfo(@Valid ScmBSendinfo scmBSendinfo) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSendinfo.setCreateUserId(currentUser.getUserId());

            scmBSendinfo.setGysaccount(currentUser.getUsername());
            scmBSendinfo.setGysname(currentUser.getRealname());
            this.iScmBSendinfoService.createScmBSendinfo(scmBSendinfo);
        } catch (Exception e) {
            message = "新增/按钮失败";
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
    @RequiresPermissions("scmBSendinfo:update")
    public void updateScmBSendinfo(@Valid ScmBSendinfo scmBSendinfo) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSendinfo.setModifyUserId(currentUser.getUserId());
            this.iScmBSendinfoService.updateScmBSendinfo(scmBSendinfo);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("scmBSendinfo:delete")
    public void deleteScmBSendinfos(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmBSendinfoService.deleteScmBSendinfos(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmBSendinfo:export")
    public void export(QueryRequest request, ScmBSendinfo scmBSendinfo, HttpServletResponse response) throws FebsException {
        try {
            List<ScmBSendinfo> scmBSendinfos = this.iScmBSendinfoService.findScmBSendinfos(request, scmBSendinfo).getRecords();
            ExcelKit.$Export(ScmBSendinfo.class, response).downXlsx(scmBSendinfos, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmBSendinfo detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmBSendinfo scmBSendinfo = this.iScmBSendinfoService.getById(id);
        return scmBSendinfo;
    }
}