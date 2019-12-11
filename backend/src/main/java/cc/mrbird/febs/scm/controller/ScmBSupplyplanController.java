package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.service.IScmBPurcharseorderService;
import cc.mrbird.febs.scm.service.IScmBSupplyplanService;
import cc.mrbird.febs.scm.entity.ScmBSupplyplan;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.service.IViewSupplyplanService;
import cc.mrbird.febs.system.domain.User;
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
import java.util.*;

/**
 * @author viki
 * @since 2019-11-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSupplyplan")

public class ScmBSupplyplanController extends BaseController {

    private String message;
    @Autowired
    public IScmBSupplyplanService iScmBSupplyplanService;
    @Autowired
    public IViewSupplyplanService iViewSupplyplanService;
    @Autowired
    public IScmBPurcharseorderService iScmBPurcharseorderService;

    /**
     * 分页查询数据
     *
     * @param bootStrapTable 分页信息
     * @param scmBSupplyplan 查询条件
     * @return
     */
    @GetMapping
    public Map<String, Object> List(QueryRequest request, ScmBSupplyplan scmBSupplyplan) {
        scmBSupplyplan.setIsDeletemark(1);
        return getDataTable(this.iScmBSupplyplanService.findScmBSupplyplans(request, scmBSupplyplan));
    }

    @GetMapping("sendOrder")
    public Map<String, Object> ListOrder(QueryRequest request, ScmBSupplyplan scmBSupplyplan) {
        scmBSupplyplan.setIsDeletemark(1);
        User currentUser = FebsUtil.getCurrentUser();
        scmBSupplyplan.setGysaccount(currentUser.getUsername());

        return getDataTable(this.iScmBSupplyplanService.findSupplyplans(request, scmBSupplyplan));
    }

    private Boolean IsExistFphm(String baseid, String id, String fphm, String gysAccount) {
        ScmBPurcharseorder entity = this.iScmBPurcharseorderService.getById(baseid);
        if (entity.getWerks() == "2000" & (entity.getLgort() == "1001" || entity.getLgort() == "1008")) {
            return true;
        }
        if (entity.getWerks() == "2200" & (entity.getLgort() == "1002" || entity.getLgort() == "1005")) {
            return true;
        }
        if (entity.getWerks() == "2100") {
            return true;
        }
        return this.iScmBSupplyplanService.IsExistFphm(id, fphm, gysAccount);
    }

    /**
     * 跳转添加页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("scmBSupplyplan:add")
    public void addScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan) throws FebsException {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, 6);
            Date m = c.getTime();
            if (scmBSupplyplan.getVfdat().compareTo(m) < 0) {
                throw new FebsException("药品剩余效期不足6个月！");
            }
            User currentUser = FebsUtil.getCurrentUser();
            scmBSupplyplan.setCreateUserId(currentUser.getUserId());
            scmBSupplyplan.setGysaccount(currentUser.getUsername());
            scmBSupplyplan.setGysname(currentUser.getRealname());

            Boolean flag = IsExistFphm(scmBSupplyplan.getBaseId(), "", scmBSupplyplan.getFphm(), currentUser.getUsername());
            if (!flag) {
                throw new FebsException("发票号码已经存在，一个发票号只对应一个供应计划！");
            }
            this.iScmBSupplyplanService.createScmBSupplyplan(scmBSupplyplan);

            List<ViewSupplyplan> list = new ArrayList<>();
            list.add(this.iViewSupplyplanService.getById(scmBSupplyplan.getId()));
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "C");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                ScmBSupplyplan deletesupplan=new ScmBSupplyplan();
                deletesupplan.setId(scmBSupplyplan.getId());
                deletesupplan.setIsDeletemark(0);
                this.iScmBSupplyplanService.updateSupplyplanOnly(deletesupplan);//发送失败 删除数据  假删除
                throw new FebsException("SAP端接收失败");
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 跳转修改页面
     *
     * @param request
     * @param id      实体ID
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("scmBSupplyplan:update")
    public void updateScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSupplyplan.setModifyUserId(currentUser.getUserId());
            Boolean flag = IsExistFphm(scmBSupplyplan.getBaseId(), scmBSupplyplan.getId().toString(), scmBSupplyplan.getFphm(), currentUser.getUsername());
            if (!flag) {
                throw new FebsException("发票号码已经存在，一个发票号只对应一个供应计划！");
            }
            this.iScmBSupplyplanService.updateScmBSupplyplan(scmBSupplyplan);
            List<ViewSupplyplan> list = new ArrayList<>();
            list.add(this.iViewSupplyplanService.getById(scmBSupplyplan.getId()));
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error(scmBSupplyplan.getId().toString()+"SAP端处理失败");
                throw new FebsException("SAP端处理失败");
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("物资去除送货单号")
    @DeleteMapping("deleteSendOrder/{ids}")
    public void deleteSendOrders(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setSendOrderCode("");
                scmBSupplyplan.setFphm("");
                this.iScmBSupplyplanService.updateSupplyplanOnly(scmBSupplyplan);
            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("药品去除送货单号")
    @DeleteMapping("deleteSendOrder2/{ids}")
    public void deleteSendOrders2(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setSendOrderCode("");
                this.iScmBSupplyplanService.updateSupplyplanOnly(scmBSupplyplan);
            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    public void deleteScmBSupplyplans(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setIsDeletemark(0);
                this.iScmBSupplyplanService.updateScmBSupplyplan(scmBSupplyplan);
            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
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
        ScmBSupplyplan scmBSupplyplan = this.iScmBSupplyplanService.getById(id);
        return scmBSupplyplan;
    }
}