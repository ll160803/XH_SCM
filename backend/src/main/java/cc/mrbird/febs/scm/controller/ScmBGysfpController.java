package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.entity.ScmBFpplan;
import cc.mrbird.febs.scm.entity.VScmbgysfpuser;
import cc.mrbird.febs.scm.service.IScmBFpplanService;
import cc.mrbird.febs.scm.service.IScmBGysfpService;
import cc.mrbird.febs.scm.entity.ScmBGysfp;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.service.IVScmbgysfpuserService;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2020-07-10
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBGysfp")

public class ScmBGysfpController extends BaseController{

private String message;
@Autowired
public IScmBGysfpService iScmBGysfpService;

@Autowired
public IVScmbgysfpuserService ivScmbgysfpuserService;

@Autowired
public IScmBFpplanService iScmBFpplanService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param scmBGysfp 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmBGysfp:view")
public Map<String, Object> List(QueryRequest request, ScmBGysfp scmBGysfp){
    User currentUser = FebsUtil.getCurrentUser();
    scmBGysfp.setGysaccount(currentUser.getUsername());
        return getDataTable(this.iScmBGysfpService.findScmBGysfps(request, scmBGysfp));
        }

    @GetMapping("audit")
    @RequiresPermissions("scmBGysfp:view")
    public Map<String, Object> List2(QueryRequest request, ScmBGysfp scmBGysfp) {
        User currentUser = FebsUtil.getCurrentUser();
        String userId= currentUser.getUserId().toString();
        scmBGysfp.setUserid(userId);
      
        return getDataTable(this.iScmBGysfpService.findScmBGysfpsAudit(request, scmBGysfp));
    }

/**
 * 添加
 * @param  scmBGysfp
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmBGysfp:add")
public void addScmBGysfp(@Valid ScmBGysfp scmBGysfp)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        if(this.iScmBGysfpService.IsExist(scmBGysfp.getFpHm(),currentUser.getUsername(),""))
        {
            throw new Exception("已经存在的的发票号码");
        }
        if(!StringUtils.isNotBlank(scmBGysfp.getFileId())){
                throw new Exception("请上传药厂发票附件");
            }
            if(!StringUtils.isNotBlank(scmBGysfp.getMaterId())){
                throw new Exception("请上传供应商发票附件");
            }
//            if(!StringUtils.isNotBlank(scmBGysfp.getFpBm())){
//                throw new Exception("请选择开票单据");
//            }
        scmBGysfp.setCreateUserId(currentUser.getUserId());

        scmBGysfp.setGysaccount(currentUser.getUsername());
        scmBGysfp.setGysName(currentUser.getRealname());
        scmBGysfp.setState(0);
        scmBGysfp.setIsDeletemark(1);
        this.iScmBGysfpService.createScmBGysfp(scmBGysfp);

        if(StringUtils.isNotEmpty(scmBGysfp.getFpBm())) {
            ScmBFpplan scmBFpplan = new ScmBFpplan();
            scmBFpplan.setId(Long.parseLong(scmBGysfp.getFpBm()));
            scmBFpplan.setFphm(scmBGysfp.getFpHm());
            scmBFpplan.setFprq(scmBGysfp.getFprq());
            this.iScmBFpplanService.updateScmBFpplan(scmBFpplan);
            this.iScmBFpplanService.updateFpData(scmBGysfp.getFpBm(), DateUtil.format(scmBGysfp.getFprq(),"yyyy-MM-dd"),scmBGysfp.getFpHm());
        }

        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(e.getMessage());
        }
        }

/**
 * 修改
 * @param scmBGysfp
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmBGysfp:update")
public void updateScmBGysfp(@Valid ScmBGysfp scmBGysfp)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      scmBGysfp.setModifyUserId(currentUser.getUserId());
      scmBGysfp.setState(0);
            if(!StringUtils.isNotBlank(scmBGysfp.getFileId())){
                throw new Exception("请上传药厂发票附件");
            }
            if(!StringUtils.isNotBlank(scmBGysfp.getMaterId())){
                throw new Exception("请上传供应商发票附件");
            }
            if(this.iScmBGysfpService.IsExist(scmBGysfp.getFpHm(),currentUser.getUsername(),scmBGysfp.getId()))
            {
                throw new Exception("已经存在的的发票号码");
            }
        this.iScmBGysfpService.updateScmBGysfp(scmBGysfp);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(e.getMessage());
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmBGysfp:delete")
public void deleteScmBGysfps(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
            for (String id:arr_ids
                 ) {
                ScmBGysfp scmBGysfp= this.iScmBGysfpService.getById(id);
                if(scmBGysfp.getState().equals(1)){
                    throw  new FebsException("发票已经审核，不能删除");
                }
if(StringUtils.isNotEmpty(scmBGysfp.getFpBm())) {
    this.iScmBFpplanService.updateFpplan(scmBGysfp.getFpBm());
    this.iScmBFpplanService.updateFpData(scmBGysfp.getFpBm(), null, "");
}
                this.iScmBGysfpService.removeById(id);
            }
        //this.iScmBGysfpService.deleteScmBGysfps(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmBGysfp:add")
public void export(QueryRequest request, ScmBGysfp scmBGysfp, HttpServletResponse response) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBGysfp.setGysaccount(currentUser.getUsername());
        List<ScmBGysfp> scmBGysfps = this.iScmBGysfpService.findScmBGysfps(request, scmBGysfp).getRecords();
        ExcelKit.$Export(ScmBGysfp.class, response).downXlsx(scmBGysfps, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmBGysfp detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmBGysfp scmBGysfp=this.iScmBGysfpService.getById(id);
        return scmBGysfp;
        }
    @Log("审核配送")
    @PostMapping("auditAdmin")
    public void auditAdmin(@Valid ScmBGysfp scmBGysfp) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();

            this.iScmBGysfpService.updateScmBGysfp(scmBGysfp);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
        }