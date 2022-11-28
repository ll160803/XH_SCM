package cc.mrbird.febs.his.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.his.service.IScmWSaleService;
import cc.mrbird.febs.his.entity.ScmWSale;

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
@RequestMapping("scmWSale")

public class ScmWSaleController extends BaseController{

private String message;
@Autowired
public IScmWSaleService iScmWSaleService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmWSale 查询条件
 * @return
 */
@GetMapping
public Map<String, Object> List(QueryRequest request, ScmWSale scmWSale){
    User currentUser= FebsUtil.getCurrentUser();
    scmWSale.setGysName(currentUser.getRealname());
        return getDataTable(this.iScmWSaleService.findScmWSales(request, scmWSale));
        }
    @GetMapping("all")
    public Map<String, Object> List2(QueryRequest request, ScmWSale scmWSale){

        return getDataTable(this.iScmWSaleService.findScmWSales(request, scmWSale));
    }


    /**
 * 添加
 * @param  scmWSale
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmWSale:add")
public void addScmWSale(@Valid ScmWSale scmWSale)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmWSale.setCreateUserId(currentUser.getUserId());
        this.iScmWSaleService.createScmWSale(scmWSale);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param scmWSale
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmWSale:update")
public void updateScmWSale(@Valid ScmWSale scmWSale)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmWSale.setModifyUserId(currentUser.getUserId());
        this.iScmWSaleService.updateScmWSale(scmWSale);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmWSale:delete")
public void deleteScmWSales(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmWSaleService.deleteScmWSales(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
public void export(QueryRequest request, ScmWSale scmWSale, HttpServletResponse response) throws FebsException {
        try {
            request.setPageSize(10000);
            request.setPageNum(1);
        List<ScmWSale> scmWSales = this.iScmWSaleService.findScmWSales(request, scmWSale).getRecords();
        ExcelKit.$Export(ScmWSale.class, response).downXlsx(scmWSales, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmWSale detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmWSale scmWSale=this.iScmWSaleService.getById(id);
        return scmWSale;
        }
        }