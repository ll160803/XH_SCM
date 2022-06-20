package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.service.IScmBFpplanService;
import cc.mrbird.febs.scm.entity.ScmBFpplan;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.service.IViewSupplyplanService;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.convert.Convert;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2022-04-07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBFpplan")

public class ScmBFpplanController extends BaseController {

    private String message;
    @Autowired
    public IScmBFpplanService iScmBFpplanService;

    @Autowired
    public IViewSupplyplanService iViewSupplyplanService;


    /**
     * 分页查询数据
     *
     * @param request    分页信息
     * @param scmBFpplan 查询条件
     * @return
     */
    @GetMapping

    public Map<String, Object> List(QueryRequest request, ScmBFpplan scmBFpplan) {
        scmBFpplan.setIsDeletemark(1);
        User currentUser = FebsUtil.getCurrentUser();
        scmBFpplan.setGysaccount(currentUser.getUsername());
        return getDataTable(this.iScmBFpplanService.findScmBFpplans(request, scmBFpplan));
    }
    @GetMapping("fp")
    public Map<String, Object> List3(QueryRequest request, ScmBFpplan scmBFpplan) {
        return getDataTable(this.iScmBFpplanService.findScmBFpplans2(request, scmBFpplan));
    }

    @GetMapping("planIds/{sendCode}")
    public FebsResponse GetPlanIds(@PathVariable(value = "sendCode") String sendCode) {
        FebsResponse febsResponse = new FebsResponse();
        febsResponse.data(this.iScmBFpplanService.findPlanIds(sendCode));
        return febsResponse;
    }

    /**
     * 添加
     *
     * @param scmBFpplan
     * @return
     */
    @Log("新增/按钮")
    @PostMapping

    public void addScmBFpplan(@Valid ScmBFpplan scmBFpplan) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBFpplan.setCreateUserId(currentUser.getUserId());
            this.iScmBFpplanService.createScmBFpplan(scmBFpplan);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("药品新增/按钮")
    @PostMapping("planAdd")
    @RequiresPermissions("fpplan:add")
    public void addFpPlan(@Valid ScmBFpplan scmBFpplan, String supplyPlanIds) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBFpplan.setCreateUserId(currentUser.getUserId());
            scmBFpplan.setGysaccount(currentUser.getUsername());
            scmBFpplan.setGysname(currentUser.getRealname());
            scmBFpplan.supplyPlanIds = supplyPlanIds;

            String werks = "";
            String lgort = "";
            String zq=""; //是否同一个账期
            if (StringUtils.isNotBlank(supplyPlanIds)) {


                String[] pIds=supplyPlanIds.split(",");
                List<String> pid1= new ArrayList<>();
                List<String> pid2= new ArrayList<>();
                for (String iid:pIds
                     ) {
                    if(iid.contains("*")){
                        pid2.add(iid.replace("*",""));
                    }
                    else{
                        pid1.add(iid);
                    }
                }


                List<ViewSupplyplan> listvp = new ArrayList<>();
                if(pid1.size()>0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyPlanByIds(pid1));
                }
                if(pid2.size()>0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyNewPlanByIds(pid2));
                }
                double jr = listvp.stream().mapToDouble(p -> Convert.toDouble(p.getFpjr())).sum();

                for (ViewSupplyplan vp2 : listvp
                ) {
                    if (werks.equals("")) {
                        werks = vp2.getWerks();
                        lgort = vp2.getLgort();
                        zq= vp2.getMaterCode();
                    } else {
                        if (!(werks.equals(vp2.getWerks()) && lgort.equals(vp2.getLgort()) && zq.substring(0,7).equals(vp2.getMaterCode().substring(0,7)))) {
                            throw new FebsException(vp2.getId().toString() + ":" + vp2.getWerkst() + "-" + vp2.getLgortName() + "不一致或入账月份不一致");
                        }
                    }
                }

                scmBFpplan.setWerks(listvp.get(0).getWerks());
                scmBFpplan.setWerkst(listvp.get(0).getWerkst());
                scmBFpplan.setLgort(listvp.get(0).getLgort());
                scmBFpplan.setLgortName(listvp.get(0).getLgortName());
                scmBFpplan.setFpjr(new BigDecimal(jr));

            }

            this.iScmBFpplanService.createScmBFpplan(scmBFpplan);


        } catch (Exception e) {

            //log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }


    @Log("药品修改")
    @PutMapping("orderEdit")
    @RequiresPermissions("fpplan:update")
    public void updateSendorder(@Valid ScmBFpplan scmBFpplan, String supplyPlanIds) throws FebsException {
        try {
            message = "";
            User currentUser = FebsUtil.getCurrentUser();
            scmBFpplan.setModifyUserId(currentUser.getUserId());
            scmBFpplan.supplyPlanIds = supplyPlanIds;
            scmBFpplan.setFpjr(new BigDecimal(0d));

            ScmBFpplan  dataScmBFpplan= this.iScmBFpplanService.getById(scmBFpplan.getId());


            if (StringUtils.isNotEmpty(dataScmBFpplan.getFphm())) {
                throw new FebsException("已经绑定了发票号，请解除发票号后编辑");
            }
            String werks = "";
            String lgort = "";
            String zq= "";
            if (StringUtils.isNotBlank(supplyPlanIds)) {

                String[] pIds=supplyPlanIds.split(",");
                List<String> pid1= new ArrayList<>();
                List<String> pid2= new ArrayList<>();
                for (String iid:pIds
                ) {
                    if(iid.contains("*")){
                        pid2.add(iid.replace("*",""));
                    }
                    else{
                        pid1.add(iid);
                    }
                }
                List<ViewSupplyplan> listvp = new ArrayList<>();
                if(pid1.size()>0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyPlanByIds(pid1));
                }
                if(pid2.size()>0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyNewPlanByIds(pid2));
                }
               // listvp.addAll(this.iViewSupplyplanService.getViewSupplyNewPlanByIds(pid2));
                double jr = listvp.stream().mapToDouble(p -> Convert.toDouble(p.getFpjr())).sum();
                scmBFpplan.setFpjr(new BigDecimal(jr));
                for (ViewSupplyplan vp2 : listvp
                ) {
                    if (werks.equals("")) {
                        werks = vp2.getWerks();
                        lgort = vp2.getLgort();
                        zq= vp2.getMaterCode();
                    } else {
                        if (!(werks.equals(vp2.getWerks()) && lgort.equals(vp2.getLgort()) && zq.substring(0,7).equals(vp2.getMaterCode().substring(0,7)))) {
                            throw new FebsException(vp2.getId().toString() + ":" + vp2.getWerkst() + "-" + vp2.getLgortName() + "不一致或入账月份不一致");
                        }
                    }
                }
            }
            this.iScmBFpplanService.updateScmBFpplan(scmBFpplan);
        } catch (Exception e) {
            // message = "修改失败";
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")

    public void deleteScmBFpplans(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id: arr_ids
                 ) {
                ScmBFpplan  dataScmBFpplan= this.iScmBFpplanService.getById(id);
                if (StringUtils.isNotEmpty(dataScmBFpplan.getFphm())) {
                    throw new FebsException("已经绑定了发票号，请解除发票号后编辑");
                }
                this.iScmBFpplanService.updateDeletePlan(String.valueOf(dataScmBFpplan.getId()),dataScmBFpplan.getId());
            }

        } catch (Exception e) {
            throw new FebsException(e.getMessage());
        }
    }

    @PostMapping("excel")

    public void export(QueryRequest request, ScmBFpplan scmBFpplan, HttpServletResponse response) throws FebsException {
        try {
            List<ScmBFpplan> scmBFpplans = this.iScmBFpplanService.findScmBFpplans(request, scmBFpplan).getRecords();
            ExcelKit.$Export(ScmBFpplan.class, response).downXlsx(scmBFpplans, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmBFpplan detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmBFpplan scmBFpplan = this.iScmBFpplanService.getById(id);
        return scmBFpplan;
    }
}