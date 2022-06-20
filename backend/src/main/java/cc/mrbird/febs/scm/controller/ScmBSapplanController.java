package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBSapplanService;
import cc.mrbird.febs.scm.entity.ScmBSapplan;

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
 * @since 2022-06-09
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSapplan")

public class ScmBSapplanController extends BaseController{

private String message;
@Autowired
public IScmBSapplanService iScmBSapplanService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmBSapplan 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmBSapplan:view")
public Map<String, Object> List(QueryRequest request, ScmBSapplan scmBSapplan){
        return getDataTable(this.iScmBSapplanService.findScmBSapplans(request, scmBSapplan));
        }

/**
 * 添加
 * @param  scmBSapplan
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmBSapplan:add")
public void addScmBSapplan(@Valid ScmBSapplan scmBSapplan)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBSapplan.setCreateUserId(currentUser.getUserId());
        this.iScmBSapplanService.createScmBSapplan(scmBSapplan);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param scmBSapplan
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmBSapplan:update")
public void updateScmBSapplan(@Valid ScmBSapplan scmBSapplan)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBSapplan.setModifyUserId(currentUser.getUserId());
        this.iScmBSapplanService.updateScmBSapplan(scmBSapplan);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBSapplan:delete")
public void deleteScmBSapplans(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBSapplanService.deleteScmBSapplans(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBSapplan:export")
public void export(QueryRequest request, ScmBSapplan scmBSapplan, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBSapplan> scmBSapplans = this.iScmBSapplanService.findScmBSapplans(request, scmBSapplan).getRecords();
        ExcelKit.$Export(ScmBSapplan.class, response).downXlsx(scmBSapplans, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBSapplan detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBSapplan scmBSapplan=this.iScmBSapplanService.getById(id);
        return scmBSapplan;
        }
        }