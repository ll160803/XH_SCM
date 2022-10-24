package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.entity.ComFile;
import cc.mrbird.febs.scm.entity.VScmBGyspicUser;
import cc.mrbird.febs.scm.service.IComFileService;
import cc.mrbird.febs.scm.service.IScmBGysMaterPicService;
import cc.mrbird.febs.scm.entity.ScmBGysMaterPic;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.service.IVScmBGyspicUserService;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    public IComFileService iComFileService;

    @Autowired
    public IVScmBGyspicUserService ivScmBGyspicUserService;
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
        return getDataTable(this.iScmBGysMaterPicService.findScmBGysMaterPics(request, scmBGysMaterPic, "", ""));
    }

    /**
     * 分页查询数据
     *
     * @param bootStrapTable  分页信息
     * @param scmBGysMaterPic 查询条件
     * @return
     */
    @GetMapping("audit")
    @RequiresPermissions("scmBGysMaterPic:view")
    public Map<String, Object> List2(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic, String keyword_mater, String keyword_gys) {
        User currentUser = FebsUtil.getCurrentUser();
        String userId= currentUser.getUserId().toString();
        if(StringUtils.isNotBlank(request.getSortField()))
        {
            request.setSortField("scm_b_gys_mater_pic."+request.getSortField());
        }
        return getDataTable(this.iScmBGysMaterPicService.findScmBGysMaterPicsAudit(request, scmBGysMaterPic, keyword_mater, keyword_gys,userId));
    }

    @GetMapping("noFile")
    public Map<String, Object> List22(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic, String keyword_mater, String keyword_gys) {
        User currentUser = FebsUtil.getCurrentUser();

        return getDataTable(this.iScmBGysMaterPicService.findScmBGysMaterPicsNoFile(request, scmBGysMaterPic, keyword_mater, keyword_gys));
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
            scmBGysMaterPic.setCharge(scmBGysMaterPic.getCharge().trim());

            String matnr = scmBGysMaterPic.getMaterId();
            LambdaQueryWrapper<ScmBGysMaterPic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmBGysMaterPic::getMaterId, matnr);
            queryWrapper.eq(ScmBGysMaterPic::getGysaccount, scmBGysMaterPic.getGysaccount());
            queryWrapper.eq(ScmBGysMaterPic::getCharge, scmBGysMaterPic.getCharge());
            queryWrapper.eq(ScmBGysMaterPic::getIsDeletemark, 1);
            int count =this.iScmBGysMaterPicService.count(queryWrapper);
            if(count>0)
            {
                throw new FebsException("存在相同的药品和批次");

            }

            ComFile comFile = this.iComFileService.getById(scmBGysMaterPic.getFileId());
            if (comFile != null) {
                Boolean flag = RfcNOC.SendUploadInfo_RFC(currentUser.getUsername(), scmBGysMaterPic.getMatnr(), scmBGysMaterPic.getCharge(), comFile.getServerName(), "I");
                if (flag) {
                    this.iScmBGysMaterPicService.createScmBGysMaterPic(scmBGysMaterPic);
                } else {
                    throw new FebsException("操作失败，SAP端接收验收报告失败");
                }
            }
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
            scmBGysMaterPic.setCharge(scmBGysMaterPic.getCharge().trim());

             if(this.iScmBGysMaterPicService.IsDelete(scmBGysMaterPic.getId())){
                 LambdaQueryWrapper<ScmBGysMaterPic> lambdaQueryWrapper= new LambdaQueryWrapper<>();
                 lambdaQueryWrapper.eq(ScmBGysMaterPic::getId,scmBGysMaterPic.getId());
                 ScmBGysMaterPic  pic= new ScmBGysMaterPic();
                 pic.setMtart(scmBGysMaterPic.getMtart());
                 this.iScmBGysMaterPicService.update(pic,lambdaQueryWrapper);
                 return;
                // throw new FebsException("已经使用的批次号，不能修改");
             }
            String matnr = scmBGysMaterPic.getMaterId();
            LambdaQueryWrapper<ScmBGysMaterPic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmBGysMaterPic::getMaterId, matnr);

            queryWrapper.eq(ScmBGysMaterPic::getGysaccount, scmBGysMaterPic.getGysaccount());
            queryWrapper.eq(ScmBGysMaterPic::getCharge, scmBGysMaterPic.getCharge());
            queryWrapper.ne(ScmBGysMaterPic::getId, scmBGysMaterPic.getId());
            queryWrapper.eq(ScmBGysMaterPic::getIsDeletemark, 1);
            int count =this.iScmBGysMaterPicService.count(queryWrapper);
            if(count>0)
            {
                throw new FebsException("存在相同的药品和批次");
            }

            ComFile comFile = this.iComFileService.getById(scmBGysMaterPic.getFileId());
            if (comFile != null) {
                Boolean flag = RfcNOC.SendUploadInfo_RFC(currentUser.getUsername(), scmBGysMaterPic.getMatnr(), scmBGysMaterPic.getCharge().trim(), comFile.getServerName(), "I");
                if (flag) {
                    this.iScmBGysMaterPicService.updateScmBGysMaterPic(scmBGysMaterPic);
                } else {
                    throw new FebsException("操作失败，SAP端接收验收报告失败");
                }
            }
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
            this.iScmBGysMaterPicService.deleteScmBGysMaterPics(arr_ids, 0);
        } catch (Exception e) {
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }

    @Log("审核数据")
    @DeleteMapping("/audit/{ids}")
    @RequiresPermissions("scmBGysMaterPic:audit")
    public void deleteAuditScmBGysMaterPics(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmBGysMaterPicService.deleteScmBGysMaterPics(arr_ids, 1);
        } catch (Exception e) {
            message = "审核成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmBGysMaterPic:add")
    public void export(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic, HttpServletResponse response) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBGysMaterPic.setGysaccount(currentUser.getUsername());
            List<ScmBGysMaterPic> scmBGysMaterPics = this.iScmBGysMaterPicService.findScmBGysMaterPics(request, scmBGysMaterPic, "", "").getRecords();
            ExcelKit.$Export(ScmBGysMaterPic.class, response).downXlsx(scmBGysMaterPics, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excelNoFile")
    public void exportNoFile(QueryRequest request, ScmBGysMaterPic scmBGysMaterPic, HttpServletResponse response, String keyword_mater, String keyword_gys) throws FebsException {
        try {
            request.setPageSize(10000);
            request.setPageNum(1);
            User currentUser = FebsUtil.getCurrentUser();
            List<ScmBGysMaterPic> scmBGysMaterPics = this.iScmBGysMaterPicService.findScmBGysMaterPicsNoFile(request, scmBGysMaterPic, keyword_mater, keyword_gys).getRecords();
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

    @GetMapping("/charge/{id}")
    public List<String> GetChargeByBaseId(@NotBlank(message = "{required}") @PathVariable String id) {
        User currentUser = FebsUtil.getCurrentUser();
        String account = currentUser.getUsername();
        return this.iScmBGysMaterPicService.findChargeByBaseId(id, account);
    }
}