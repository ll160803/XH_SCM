package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBSendorderService;
import cc.mrbird.febs.scm.entity.ScmBSendorder;

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
 * @since 2019-11-26
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSendorder")

public class ScmBSendorderController extends BaseController{

private String message;
@Autowired
public IScmBSendorderService iScmBSendorderService;


/**
 * 分页查询数据
 *
 * @param bootStrapTable  分页信息
 * @param scmBSendorder 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmBSendorder:view")
public Map<String, Object> List(QueryRequest request, ScmBSendorder scmBSendorder){
        return getDataTable(this.iScmBSendorderService.findScmBSendorders(request, scmBSendorder));
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
@RequiresPermissions("scmBSendorder:add")
public void addScmBSendorder(@Valid ScmBSendorder scmBSendorder)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBSendorder.setCreateUserId(currentUser.getUserId());
        this.iScmBSendorderService.createScmBSendorder(scmBSendorder);
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
@RequiresPermissions("scmBSendorder:update")
public void updateScmBSendorder(@Valid ScmBSendorder scmBSendorder)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBSendorder.setModifyUserId(currentUser.getUserId());
        this.iScmBSendorderService.updateScmBSendorder(scmBSendorder);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBSendorder:delete")
public void deleteScmBSendorders(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBSendorderService.deleteScmBSendorders(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBSendorder:export")
public void export(QueryRequest request, ScmBSendorder scmBSendorder, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBSendorder> scmBSendorders = this.iScmBSendorderService.findScmBSendorders(request, scmBSendorder).getRecords();
        ExcelKit.$Export(ScmBSendorder.class, response).downXlsx(scmBSendorders, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBSendorder detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBSendorder scmBSendorder=this.iScmBSendorderService.getById(id);
        return scmBSendorder;
        }
        }