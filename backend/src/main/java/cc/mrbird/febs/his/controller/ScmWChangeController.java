package cc.mrbird.febs.his.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.his.service.IScmWChangeService;
import cc.mrbird.febs.his.entity.ScmWChange;

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
 * @since 2022-10-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmWChange")

public class ScmWChangeController extends BaseController{

private String message;
@Autowired
public IScmWChangeService iScmWChangeService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmWChange 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmWChange:view")
public Map<String, Object> List(QueryRequest request, ScmWChange scmWChange){
        return getDataTable(this.iScmWChangeService.findScmWChanges(request, scmWChange));
        }

/**
 * 添加
 * @param  scmWChange
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmWChange:add")
public void addScmWChange(@Valid ScmWChange scmWChange)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmWChange.setCreateUserId(currentUser.getUserId());
        this.iScmWChangeService.createScmWChange(scmWChange);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param scmWChange
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmWChange:update")
public void updateScmWChange(@Valid ScmWChange scmWChange)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmWChange.setModifyUserId(currentUser.getUserId());
        this.iScmWChangeService.updateScmWChange(scmWChange);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmWChange:delete")
public void deleteScmWChanges(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmWChangeService.deleteScmWChanges(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmWChange:export")
public void export(QueryRequest request, ScmWChange scmWChange, HttpServletResponse response) throws FebsException {
        try {
        List<ScmWChange> scmWChanges = this.iScmWChangeService.findScmWChanges(request, scmWChange).getRecords();
        ExcelKit.$Export(ScmWChange.class, response).downXlsx(scmWChanges, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmWChange detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmWChange scmWChange=this.iScmWChangeService.getById(id);
        return scmWChange;
        }
        }