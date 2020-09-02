package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBSupplyplanDService;
import cc.mrbird.febs.scm.entity.ScmBSupplyplanD;

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
 * @since 2020-08-19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSupplyplanD")

public class ScmBSupplyplanDController extends BaseController{

private String message;
@Autowired
public IScmBSupplyplanDService iScmBSupplyplanDService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmBSupplyplanD 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmBSupplyplanD:view")
public Map<String, Object> List(QueryRequest request, ScmBSupplyplanD scmBSupplyplanD){
        return getDataTable(this.iScmBSupplyplanDService.findScmBSupplyplanDs(request, scmBSupplyplanD));
        }

/**
 * 添加
 * @param  scmBSupplyplanD
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmBSupplyplanD:add")
public void addScmBSupplyplanD(@Valid ScmBSupplyplanD scmBSupplyplanD)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBSupplyplanD.setCreateUserId(currentUser.getUserId());
        this.iScmBSupplyplanDService.createScmBSupplyplanD(scmBSupplyplanD);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param scmBSupplyplanD
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmBSupplyplanD:update")
public void updateScmBSupplyplanD(@Valid ScmBSupplyplanD scmBSupplyplanD)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBSupplyplanD.setModifyUserId(currentUser.getUserId());
        this.iScmBSupplyplanDService.updateScmBSupplyplanD(scmBSupplyplanD);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBSupplyplanD:delete")
public void deleteScmBSupplyplanDs(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBSupplyplanDService.deleteScmBSupplyplanDs(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBSupplyplanD:export")
public void export(QueryRequest request, ScmBSupplyplanD scmBSupplyplanD, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBSupplyplanD> scmBSupplyplanDs = this.iScmBSupplyplanDService.findScmBSupplyplanDs(request, scmBSupplyplanD).getRecords();
        ExcelKit.$Export(ScmBSupplyplanD.class, response).downXlsx(scmBSupplyplanDs, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBSupplyplanD detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBSupplyplanD scmBSupplyplanD=this.iScmBSupplyplanDService.getById(id);
        return scmBSupplyplanD;
        }
        }