package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.service.IViewSupplyplanNewService;
import cc.mrbird.febs.scm.entity.ViewSupplyplanNew;

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
@RequestMapping("viewSupplyplanNew")

public class ViewSupplyplanNewController extends BaseController{

private String message;
@Autowired
public IViewSupplyplanNewService iViewSupplyplanNewService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param viewSupplyplanNew 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("viewSupplyplanNew:view")
public Map<String, Object> List(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew){
        return getDataTable(this.iViewSupplyplanNewService.findViewSupplyplanNews(request, viewSupplyplanNew));
        }

/**
 * 添加
 * @param  viewSupplyplanNew
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("viewSupplyplanNew:add")
public void addViewSupplyplanNew(@Valid ViewSupplyplanNew viewSupplyplanNew)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        viewSupplyplanNew.setCreateUserId(currentUser.getUserId());
        this.iViewSupplyplanNewService.createViewSupplyplanNew(viewSupplyplanNew);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param viewSupplyplanNew
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("viewSupplyplanNew:update")
public void updateViewSupplyplanNew(@Valid ViewSupplyplanNew viewSupplyplanNew)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      viewSupplyplanNew.setModifyUserId(currentUser.getUserId());
        this.iViewSupplyplanNewService.updateViewSupplyplanNew(viewSupplyplanNew);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
    @GetMapping("code")
    public Map<String, Object> ListOrder3(QueryRequest request, ViewSupplyplanNew scmBSupplyplan) {
        scmBSupplyplan.setIsDeletemark(1);
        User currentUser = FebsUtil.getCurrentUser();
        scmBSupplyplan.setGysaccount(currentUser.getUsername());

        return getDataTable(this.iViewSupplyplanNewService.findViewSupplyplans_byMaterCode2(request, scmBSupplyplan));
    }
    @GetMapping("fpcode")
    public Map<String, Object> ListOrder5(QueryRequest request, ViewSupplyplanNew scmBSupplyplan) {
        scmBSupplyplan.setIsDeletemark(1);
        User currentUser = FebsUtil.getCurrentUser();
        //scmBSupplyplan.setGysaccount(currentUser.getUsername());
        return getDataTable(this.iViewSupplyplanNewService.findViewSupplyplans_byCode(request, scmBSupplyplan));
    }

@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("viewSupplyplanNew:delete")
public void deleteViewSupplyplanNews(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iViewSupplyplanNewService.deleteViewSupplyplanNews(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("viewSupplyplanNew:export")
public void export(QueryRequest request, ViewSupplyplanNew viewSupplyplanNew, HttpServletResponse response) throws FebsException {
        try {
        List<ViewSupplyplanNew> viewSupplyplanNews = this.iViewSupplyplanNewService.findViewSupplyplanNews(request, viewSupplyplanNew).getRecords();
        ExcelKit.$Export(ViewSupplyplanNew.class, response).downXlsx(viewSupplyplanNews, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ViewSupplyplanNew detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ViewSupplyplanNew viewSupplyplanNew=this.iViewSupplyplanNewService.getById(id);
        return viewSupplyplanNew;
        }
        }