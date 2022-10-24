package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.entity.PlanOrder;
import cc.mrbird.febs.scm.service.IScmBPurcharseorderService;
import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.beust.jcommander.internal.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
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
@RequestMapping("scmBPurcharseorder")

public class ScmBPurcharseorderController extends BaseController{

private String message;
@Autowired
public IScmBPurcharseorderService iScmBPurcharseorderService;



@GetMapping
@RequiresPermissions("scmBPurcharseorder:view")
public Map<String, Object> List(QueryRequest request, ScmBPurcharseorder scmBPurcharseorder){
        return getDataTable(this.iScmBPurcharseorderService.findScmBPurcharseorders(request, scmBPurcharseorder));
        }


@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmBPurcharseorder:add")
public void addScmBPurcharseorder(@Valid ScmBPurcharseorder scmBPurcharseorder)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        scmBPurcharseorder.setCreateUserId(currentUser.getUserId());
        this.iScmBPurcharseorderService.createScmBPurcharseorder(scmBPurcharseorder);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("修改")
@PutMapping
@RequiresPermissions("scmBPurcharseorder:update")
public void updateScmBPurcharseorder(@Valid ScmBPurcharseorder scmBPurcharseorder)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBPurcharseorder.setModifyUserId(currentUser.getUserId());
        this.iScmBPurcharseorderService.updateScmBPurcharseorder(scmBPurcharseorder);
        }catch(Exception e){
        message="修改成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBPurcharseorder:delete")
public void deleteScmBPurcharseorders(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmBPurcharseorderService.deleteScmBPurcharseorders(arr_ids);
        }catch(Exception e){
        message="删除成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBPurcharseorder:export")
public void export(QueryRequest request, ScmBPurcharseorder scmBPurcharseorder, HttpServletResponse response) throws FebsException {
        try {
        List<ScmBPurcharseorder> scmBPurcharseorders = this.iScmBPurcharseorderService.findScmBPurcharseorders(request, scmBPurcharseorder).getRecords();
        ExcelKit.$Export(ScmBPurcharseorder.class, response).downXlsx(scmBPurcharseorders, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBPurcharseorder detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBPurcharseorder scmBPurcharseorder=this.iScmBPurcharseorderService.getOrderById(id);
        return scmBPurcharseorder;
        }
    @RequestMapping(value = "downTemplate", method = RequestMethod.POST)
    public void downTemplate(HttpServletResponse response) {
        List<PlanOrder> publishList = new ArrayList<>();
        ExcelKit.$Export(PlanOrder.class, response).downXlsx(publishList, true);
    }
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public ResponseEntity<?> importUser(@RequestParam MultipartFile file)
            throws IOException {
        long beginMillis = System.currentTimeMillis();

        List<PlanOrder> successList = Lists.newArrayList();
        List<Map<String, Object>> errorList = Lists.newArrayList();
        List<Map<String, Object>> resultList = Lists.newArrayList();

        User currentUser=FebsUtil.getCurrentUser();


        ExcelKit.$Import(PlanOrder.class)
                .readXlsx(file.getInputStream(), new ExcelReadHandler<PlanOrder>() {

                    @Override
                    public void onSuccess(int sheetIndex, int rowIndex, PlanOrder entity) {
                        successList.add(entity); // 单行读取成功，加入入库队列。
                    }

                    @Override
                    public void onError(int sheetIndex, int rowIndex,
                                        java.util.List<ExcelErrorField> errorFields) {
                        // 读取数据失败，记录了当前行所有失败的数据
                        errorList.add(MapUtil.of("sheetIndex", sheetIndex));
                        errorList.add(MapUtil.of("rowIndex", rowIndex));
                        errorList.add(MapUtil.of("errorFields", errorFields));
                    }
                });

        // TODO: 执行successList的入库操作。
        if(CollectionUtil.isEmpty(errorList)){
            for (PlanOrder mdlBArchiveImport:successList
            ) {

                ScmBPurcharseorder scmBPurcharseorder= new ScmBPurcharseorder();
                if(StringUtils.isEmpty(mdlBArchiveImport.getSendDeaprtContact())){
                    scmBPurcharseorder.setSendDeaprtContact("");
                }
                else {
                    scmBPurcharseorder.setSendDeaprtContact(mdlBArchiveImport.getSendDeaprtContact());
                }
                LambdaQueryWrapper<ScmBPurcharseorder> lm= new LambdaQueryWrapper<>();
                lm.eq(ScmBPurcharseorder::getEbeln,mdlBArchiveImport.getEbeln());
                lm.eq(ScmBPurcharseorder::getEbelp,mdlBArchiveImport.getEbelp());
                this.iScmBPurcharseorderService.update(scmBPurcharseorder,lm);
            }
        }

        resultList.add(MapUtil.of("data", successList));
        resultList.add(MapUtil.of("haveError", !CollectionUtil.isEmpty(errorList)));
        resultList.add(MapUtil.of("error", errorList));
        resultList.add(MapUtil.of("timeConsuming", (System.currentTimeMillis() - beginMillis) / 1000L));
        return ResponseEntity.ok(resultList);
    }
        }