package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmDVendorService;
import cc.mrbird.febs.scm.entity.ScmDVendor;

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
 *
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmDVendor")

public class ScmDVendorController extends BaseController{

private String message;
@Autowired
public IScmDVendorService iScmDVendorService;


/**
 * 分页查询数据
 *
 * @param bootStrapTable  分页信息
 * @param scmDVendor 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmDVendor:view")
public Map<String, Object> List(QueryRequest request, ScmDVendor scmDVendor){
        return getDataTable(this.iScmDVendorService.findScmDVendors(request, scmDVendor));
        }

/**
 * 跳转添加页面
 * @param request
 * @param response
 * @param model
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmDVendor:add")
public void addScmDVendor(@Valid ScmDVendor scmDVendor)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmDVendor.setCreateUserId(currentUser.getUserId());
        this.iScmDVendorService.createScmDVendor(scmDVendor);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 跳转修改页面
 * @param request
 * @param id  实体ID
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmDVendor:update")
public void updateScmDVendor(@Valid ScmDVendor scmDVendor)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmDVendor.setModifyUserId(currentUser.getUserId());
        this.iScmDVendorService.updateScmDVendor(scmDVendor);
        }catch(Exception e){
        message="修改成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmDVendor:delete")
public void deleteScmDVendors(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmDVendorService.deleteScmDVendors(arr_ids);
        }catch(Exception e){
        message="删除成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmDVendor:export")
public void export(QueryRequest request, ScmDVendor scmDVendor, HttpServletResponse response) throws FebsException {
        try {
        List<ScmDVendor> scmDVendors = this.iScmDVendorService.findScmDVendors(request, scmDVendor).getRecords();
        ExcelKit.$Export(ScmDVendor.class, response).downXlsx(scmDVendors, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmDVendor detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmDVendor scmDVendor=this.iScmDVendorService.getById(id);
        return scmDVendor;
        }
        }