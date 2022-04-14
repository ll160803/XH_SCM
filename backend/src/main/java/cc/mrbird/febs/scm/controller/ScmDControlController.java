package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmDControlService;
import cc.mrbird.febs.scm.entity.ScmDControl;

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
 * @since 2022-04-07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmDControl")

public class ScmDControlController extends BaseController{

private String message;
@Autowired
public IScmDControlService iScmDControlService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmDControl 查询条件
 * @return
 */
@GetMapping
public Map<String, Object> List(QueryRequest request, ScmDControl scmDControl){
        return getDataTable(this.iScmDControlService.findScmDControls(request, scmDControl));
        }

/**
 * 添加
 * @param  scmDControl
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmDControl:add")
public void addScmDControl(@Valid ScmDControl scmDControl)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        this.iScmDControlService.createScmDControl(scmDControl);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param scmDControl
 * @return
 */
@Log("修改")
@PutMapping
public void updateScmDControl(@Valid ScmDControl scmDControl)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        this.iScmDControlService.updateEndTime(scmDControl);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmDControl:delete")
public void deleteScmDControls(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmDControlService.deleteScmDControls(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmDControl:export")
public void export(QueryRequest request, ScmDControl scmDControl, HttpServletResponse response) throws FebsException {
        try {
        List<ScmDControl> scmDControls = this.iScmDControlService.findScmDControls(request, scmDControl).getRecords();
        ExcelKit.$Export(ScmDControl.class, response).downXlsx(scmDControls, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmDControl detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmDControl scmDControl=this.iScmDControlService.getById(id);
        return scmDControl;
        }
        }