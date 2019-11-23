package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBSupplyplanService;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;

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
 * @since 2019-11-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSupplyplan")

public class ScmBSupplyplanController extends BaseController{

private String message;
@Autowired
public IScmBSupplyplanService iScmBSupplyplanService;


/**
 * 分页查询数据
 *
 * @param bootStrapTable  分页信息
 * @param scmBSupplyplan 查询条件
 * @return
 */
@GetMapping
public Map<String, Object> List(QueryRequest request, ScmBSupplyplan scmBSupplyplan){
        return getDataTable(this.iScmBSupplyplanService.findScmBSupplyplans(request, scmBSupplyplan));
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
@RequiresPermissions("scmBSupplyplan:add")
public void addScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBSupplyplan.setCreateUserId(currentUser.getUserId());
        this.iScmBSupplyplanService.createScmBSupplyplan(scmBSupplyplan);
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
@RequiresPermissions("scmBSupplyplan:update")
public void updateScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBSupplyplan.setModifyUserId(currentUser.getUserId());
        this.iScmBSupplyplanService.updateScmBSupplyplan(scmBSupplyplan);
        }catch(Exception e){
        message="修改成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBSupplyplan:delete")
public void deleteScmBSupplyplans(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        }catch(Exception e){
        message="删除成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBSupplyplan:export")
public void export(QueryRequest request, ScmBSupplyplan scmBSupplyplan, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBSupplyplan> scmBSupplyplans = this.iScmBSupplyplanService.findScmBSupplyplans(request, scmBSupplyplan).getRecords();
        ExcelKit.$Export(ScmBSupplyplan.class, response).downXlsx(scmBSupplyplans, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBSupplyplan detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBSupplyplan scmBSupplyplan=this.iScmBSupplyplanService.getById(id);
        return scmBSupplyplan;
        }
        }