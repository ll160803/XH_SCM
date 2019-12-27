package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmBQuotedpriceService;
import cc.mrbird.febs.scm.entity.ScmBQuotedprice;

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
 * @since 2019-12-27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBQuotedprice")

public class ScmBQuotedpriceController extends BaseController{

private String message;
@Autowired
public IScmBQuotedpriceService iScmBQuotedpriceService;


/**
 * 分页查询数据
 *
 * @param bootStrapTable  分页信息
 * @param scmBQuotedprice 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmBQuotedprice:view")
public Map<String, Object> List(QueryRequest request, ScmBQuotedprice scmBQuotedprice){
        return getDataTable(this.iScmBQuotedpriceService.findScmBQuotedprices(request, scmBQuotedprice));
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
@RequiresPermissions("scmBQuotedprice:add")
public void addScmBQuotedprice(@Valid ScmBQuotedprice scmBQuotedprice)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBQuotedprice.setCreateUserId(currentUser.getUserId());
        this.iScmBQuotedpriceService.createScmBQuotedprice(scmBQuotedprice);
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
@RequiresPermissions("scmBQuotedprice:update")
public void updateScmBQuotedprice(@Valid ScmBQuotedprice scmBQuotedprice)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBQuotedprice.setModifyUserId(currentUser.getUserId());
        this.iScmBQuotedpriceService.updateScmBQuotedprice(scmBQuotedprice);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBQuotedprice:delete")
public void deleteScmBQuotedprices(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBQuotedpriceService.deleteScmBQuotedprices(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBQuotedprice:export")
public void export(QueryRequest request, ScmBQuotedprice scmBQuotedprice, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBQuotedprice> scmBQuotedprices = this.iScmBQuotedpriceService.findScmBQuotedprices(request, scmBQuotedprice).getRecords();
        ExcelKit.$Export(ScmBQuotedprice.class, response).downXlsx(scmBQuotedprices, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBQuotedprice detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBQuotedprice scmBQuotedprice=this.iScmBQuotedpriceService.getById(id);
        return scmBQuotedprice;
        }
        }