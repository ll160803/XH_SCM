package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.BarCodeUtil;
import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.entity.*;
import cc.mrbird.febs.scm.service.*;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.zxing.WriterException;
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
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    public IViewSupplyplanNewService iViewSupplyplanNewService;
    @Autowired
    public IScmBPurcharseorderService iScmBPurcharseorderService;
    @Autowired
    public IScmBSendorderService iScmBSendorderService;
    @Autowired
    public IScmBGysfpService iScmBGysfpService;

    @Autowired
    public IScmBSupplyplanDService iScmBSupplyplanDService;

    @Autowired
    public FebsProperties febsProperties;


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
        if (entity.getWerks().equals("2000") & (entity.getLgort().equals("1001") || entity.getLgort().equals("1008"))) {
            return true;
        }
        if (entity.getWerks().equals("2200") & (entity.getLgort().equals("1002") || entity.getLgort().equals("1005"))) {
            return true;
        }
        if (entity.getWerks().equals("2300") && (entity.getLgort().equals("1001") || entity.getLgort().equals("1005")|| entity.getLgort().equals("2003") ||entity.getLgort().equals("2012") )) {
            return true;
        }
        if (entity.getWerks().equals("2100")) {
            return true;
        }
        return this.iScmBSupplyplanService.IsExistFphm(id, fphm, gysAccount);
    }

    private String getSendDepartName(String sendDepartName) {
        if (sendDepartName.contains("????????????")) {
            return sendDepartName;
        }
        if (sendDepartName.contains("????????????")) {
            return sendDepartName;
        }
        if (sendDepartName.contains("?????????(??????)")) {
            return sendDepartName;
        }
        if (sendDepartName.contains("????????????")) {
            return sendDepartName;
        }
        if (sendDepartName.contains("?????????(?????????)")) {
            return sendDepartName;
        }
        return "";
    }

    @Log("??????/??????")
    @PostMapping
    @RequiresPermissions("scmBSupplyplan:add")
    public void addScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan) throws FebsException {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, 6);
            Date m = c.getTime();
            if (scmBSupplyplan.getVfdat().compareTo(m) < 0 ) {//?????????????????????
                if(StringUtils.isEmpty(scmBSupplyplan.getIsHp())) {
                    throw new FebsException("????????????????????????6?????????");
                }
            }
            User currentUser = FebsUtil.getCurrentUser();
            if(StringUtils.isNotEmpty(scmBSupplyplan.getIsHp()) &&scmBSupplyplan.getIsHp().equals("1")) {
                if (!this.iScmBGysfpService.IsExist(scmBSupplyplan.getFphm(), currentUser.getUsername(), "", DateUtil.format(scmBSupplyplan.getFprq(),"yyyy"))) {
                    throw new FebsException("?????????????????????????????????????????????");
                }
            }
           if(StringUtils.isEmpty(scmBSupplyplan.getFphm())) {
               scmBSupplyplan.setFprq(null);
           }

            BigDecimal bd = scmBSupplyplan.getgMenge().divide(scmBSupplyplan.getPkgAmount(), 2);
            scmBSupplyplan.setPkgNumber(bd.setScale(0, BigDecimal.ROUND_UP));

            scmBSupplyplan.setCreateUserId(currentUser.getUserId());
            scmBSupplyplan.setGysaccount(currentUser.getUsername());

            scmBSupplyplan.setGysname(currentUser.getRealname());

//            Boolean flag = IsExistFphm(scmBSupplyplan.getBaseId(), "", scmBSupplyplan.getFphm(), currentUser.getUsername());
//            if (!flag) {
//                throw new FebsException("????????????????????????????????????????????????????????????????????????");
//            }
            this.iScmBSupplyplanService.createScmBSupplyplan(scmBSupplyplan);
            //???????????????????????????  viki 2020-08-19
            this.iScmBSupplyplanDService.HandlePackage(scmBSupplyplan);

            List<ViewSupplyplan> list = new ArrayList<>();
            list.add(this.iViewSupplyplanService.getById(scmBSupplyplan.getId()));
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "C");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                ScmBSupplyplan deletesupplan = new ScmBSupplyplan();
                deletesupplan.setId(scmBSupplyplan.getId());
                deletesupplan.setIsDeletemark(0);
                this.iScmBSupplyplanService.updateSupplyplanOnly(deletesupplan);//???????????? ????????????  ?????????
                throw new FebsException("SAP???????????????");
            }
            this.iScmBSupplyplanService.updateWerkAndLgort(list.get(0));
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("??????????????????")
    @PutMapping("done")
    public void updateDoneScmBSupplyplan(@Valid String ids, String doneMenge) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            LambdaQueryWrapper<ScmBSupplyplanD> queryWrapper = new LambdaQueryWrapper<>();
            String[] arr = ids.split(",");

            queryWrapper.eq(ScmBSupplyplanD::getIsDeletemark, 1);
            queryWrapper.in(ScmBSupplyplanD::getId, arr);
            List<ScmBSupplyplanD> scmds = this.iScmBSupplyplanDService.list(queryWrapper);
            BigDecimal doneM = new BigDecimal(0);
            for (ScmBSupplyplanD scmd : scmds
            ) {
                if (scmd.getState().equals(1)) {
                    throw new FebsException(scmd.getId() + "???????????????????????????");
                }
                doneM = doneM.add(scmd.getMenge());
            }

            if (iScmBSupplyplanService.HasSendOrder(arr[0].substring(0, 12))) {
                throw new FebsException("????????????????????????????????????????????????????????????");
            }
            List<ViewSupplyplan> list = new ArrayList<>();
            ViewSupplyplan plan = this.iViewSupplyplanService.getById(arr[0].substring(0, 12));
            if (plan.getStatus().equals(1)) {
                throw new FebsException("????????????????????????????????????????????????");
            }

            if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), plan.getWerks() + plan.getLgort()).equals(0L)) {
                throw new FebsException("??????????????? " + plan.getWerkst() + "  " + plan.getLgortName() + " ????????????");
            }

            BigDecimal newDoneMenge = plan.getDoneMenge() == null ? doneM : plan.getDoneMenge().add(doneM);
            if (newDoneMenge.compareTo(plan.getgMenge()) == 1) {
                throw new FebsException("???????????????????????????????????????");
            }
            plan.setDoneMenge(newDoneMenge);
            list.add(plan);
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                throw new FebsException("SAP???????????????,??????????????????");
            } else {
                this.iScmBSupplyplanService.updateDoneMenge(arr[0].substring(0, 12), doneM.toString());
                //??????????????? ???????????????
                for (ScmBSupplyplanD scmd2 : scmds
                ) {
                    scmd2.setState(1);
                    this.iScmBSupplyplanDService.updateScmBSupplyplanD(scmd2);
                }
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????????????????")
    @PutMapping("cancel")
    public void updateCancelDoneScmBSupplyplan(@Valid String id) throws FebsException {
        try {
            ScmBSupplyplanD scmd = this.iScmBSupplyplanDService.getById(id);
            if (scmd.getState().equals(0)) {
                throw new FebsException("???????????????????????????");
            }
            User currentUser = FebsUtil.getCurrentUser();
            List<ViewSupplyplan> list = new ArrayList<>();
            ViewSupplyplan plan = this.iViewSupplyplanService.getById(id.substring(0, 12));
            if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), plan.getWerks() + plan.getLgort()).equals(0L)) {
                throw new FebsException("??????????????? " + plan.getWerkst() + "  " + plan.getLgortName() + " ????????????");
            }
            if (plan.getStatus().equals(1)) {
                throw new FebsException("??????????????????????????????????????????????????????");
            }
            plan.setDoneMenge(plan.getDoneMenge().subtract(scmd.getMenge()));
            list.add(plan);
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error(id + "SAP???????????????");
                throw new FebsException("SAP???????????????,??????????????????");
            } else {
                this.iScmBSupplyplanService.updateCancelDoneMenge(id.substring(0, 12), scmd.getMenge().toString());
                //??????????????? ???????????????

                scmd.setState(0);
                this.iScmBSupplyplanDService.updateScmBSupplyplanD(scmd);
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????????????????????????????")
    @PutMapping("doneApp")
    public void updateDoneScmBSupplyplanApp(@Valid String id, String doneMenge) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            if (iScmBSupplyplanService.HasSendOrder(id)) {
                throw new FebsException("????????????????????????????????????????????????????????????");
            }
            List<ViewSupplyplan> list = new ArrayList<>();
            ViewSupplyplan plan = this.iViewSupplyplanService.getById(id);
            if (plan.getStatus().equals(1)) {
                throw new FebsException("????????????????????????????????????????????????");
            }

            if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), plan.getWerks() + plan.getLgort()).equals(0L)) {
                throw new FebsException("??????????????? " + plan.getWerkst() + "  " + plan.getLgortName() + " ????????????");
            }
            BigDecimal bd = new BigDecimal(doneMenge);
            BigDecimal newDoneMenge = plan.getDoneMenge() == null ? bd : plan.getDoneMenge().add(bd);
            if (newDoneMenge.compareTo(plan.getgMenge()) == 1) {
                throw new FebsException("???????????????????????????????????????");
            }
            plan.setDoneMenge(newDoneMenge);
            list.add(plan);
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error(id + "SAP???????????????");
                throw new FebsException("SAP???????????????,??????????????????");
            } else {
                this.iScmBSupplyplanService.updateDoneMenge(id, doneMenge);
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????????????????,?????????")
    @PutMapping("cancelApp")
    public void updateCancelDoneScmBSupplyplanApp(@Valid String id) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            List<ViewSupplyplan> list = new ArrayList<>();
            ViewSupplyplan plan = this.iViewSupplyplanService.getById(id);
            if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), plan.getWerks() + plan.getLgort()).equals(0L)) {
                throw new FebsException("??????????????? " + plan.getWerkst() + "  " + plan.getLgortName() + " ????????????");
            }
            if (plan.getStatus().equals(1)) {
                throw new FebsException("??????????????????????????????????????????????????????");
            }
            BigDecimal bd = new BigDecimal(0);
            plan.setDoneMenge(bd);
            list.add(plan);
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error(id + "SAP???????????????");
                throw new FebsException("SAP???????????????,??????????????????");
            } else {
                this.iScmBSupplyplanService.updateCancelDoneMengeApp(id);
            }

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("??????")
    @PutMapping
    @RequiresPermissions("scmBSupplyplan:update")
    public void updateScmBSupplyplan(@Valid ScmBSupplyplan scmBSupplyplan) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSupplyplan.setModifyUserId(currentUser.getUserId());

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, 6);
            Date m = c.getTime();
            if (scmBSupplyplan.getVfdat().compareTo(m) < 0 ) {//?????????????????????
                if(StringUtils.isEmpty(scmBSupplyplan.getIsHp())) {
                    throw new FebsException("????????????????????????6?????????");
                }
            }

            BigDecimal bd = scmBSupplyplan.getgMenge().divide(scmBSupplyplan.getPkgAmount(), 2);
            scmBSupplyplan.setPkgNumber(bd.setScale(0, BigDecimal.ROUND_UP));

//            Boolean flag = IsExistFphm(scmBSupplyplan.getBaseId(), scmBSupplyplan.getId().toString(), scmBSupplyplan.getFphm(), currentUser.getUsername());
//            if (!flag) {
//                throw new FebsException("????????????????????????????????????????????????????????????????????????");
//            }
            if(StringUtils.isNotEmpty(scmBSupplyplan.getIsHp()) &&scmBSupplyplan.getIsHp().equals("1")) {
                if (!this.iScmBGysfpService.IsExist(scmBSupplyplan.getFphm(), currentUser.getUsername(), "", DateUtil.format(scmBSupplyplan.getFprq(), "yyyy"))) {
                    throw new FebsException("?????????????????????????????????????????????");
                }
            }
            if(StringUtils.isEmpty(scmBSupplyplan.getFphm())) {
                scmBSupplyplan.setFprq(null);
            }
            if (!iScmBSupplyplanService.HasPreDone(scmBSupplyplan.getId().toString())) {
                throw new FebsException("??????????????????????????????????????????????????????");
            }
            this.iScmBSupplyplanService.updateScmBSupplyplan(scmBSupplyplan);
            //???????????????????????????  viki 2020-08-19
            this.iScmBSupplyplanDService.HandlePackage(scmBSupplyplan);

            List<ViewSupplyplan> list = new ArrayList<>();
            list.add(this.iViewSupplyplanService.getById(scmBSupplyplan.getId()));
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error(scmBSupplyplan.getId().toString() + "SAP???????????????");
                throw new FebsException(backMsg.get(0).getMESS());
            }
            this.iScmBSupplyplanService.updateWerkAndLgort(list.get(0));
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????")
    @PutMapping("cancelplan")
    public void updateCancelScmBSupplyplan(@Valid String ids) throws FebsException {
        try {
            message = "";
            User currentUser = FebsUtil.getCurrentUser();

            List<String> ids2= Arrays.asList(ids.split(","));
            List<ViewSupplyplan> list= new ArrayList<>();
            if(ids2.size()>0) {
                 list = this.iViewSupplyplanService.getViewSupplyPlanByIds(ids2);
            }
            List<ViewSupplyplan> doneList = new ArrayList<>();
            List<Long> arrids = new ArrayList<>();
            for (ViewSupplyplan en : list
            ) {
                if (!en.getStatus().equals(1)) {
                    message += en.getId().toString() + ":?????????";
                } else {
                    en.setStatus(0);
                    doneList.add(en);
                    arrids.add(en.getId());
                }

            }

            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), doneList, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error("SAP???????????????");
                throw new FebsException("SAP???????????????");
            } else {
                this.iScmBSupplyplanService.cancleSupplyPlan(arrids);//??????scm??????
            }


        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????")
    @PutMapping("over")
    public void updateOverScmBSupplyplan(@Valid String ids) throws FebsException {
        try {
            message = "";
            log.error("??????id" + ids);
            User currentUser = FebsUtil.getCurrentUser();
           // String str_ids = "'" + ids.replace(",", "','") + "'";
            List<String> id_tr= Arrays.asList(ids.split(","));
            List<ViewSupplyplan> list= new ArrayList<>();
            if(id_tr.size()>0) {
                list = this.iViewSupplyplanService.getViewSupplyPlanByIds(id_tr);
            }
            List<ViewSupplyplan> doneList = new ArrayList<>();
            List<Long> arrids = new ArrayList<>();
            for (ViewSupplyplan en : list
            ) {
                if (en.getStatus().equals(1)) {
                    message += en.getId().toString() + ":?????????";
                } else if (!en.getgMenge().equals(en.getDoneMenge())) {
                    message += en.getId().toString() + ":???????????????????????????????????????";
                } else {
                    en.setStatus(1);
                    doneList.add(en);
                    arrids.add(en.getId());
                }

            }
            if (doneList.size() > 0) {
                log.error("??????SAP??????");
                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUsername(), doneList, currentUser.getUsername(), currentUser.getRealname(), "1", "U");
                if (!backMsg.get(0).getMSTYPE().equals("S")) {
                    log.error("SAP???????????????");
                    throw new FebsException(backMsg.get(0).getMESS());
                } else {
                    this.iScmBSupplyplanService.doneSupplyPlan(arrids);//??????scm??????
                }
            } else {
                throw new FebsException(message);
            }


        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("??????????????????")
    @PutMapping("overSendOrder")
    @RequiresPermissions("scmBSupplyplan:overSendOrder")
    public void updateOverScmBSupplyplan2(@Valid String sendOrderId) throws FebsException {
        try {
            message = "";
            User currentUser = FebsUtil.getCurrentUser();

            List<ViewSupplyplan> list = this.iViewSupplyplanService.getViewSupplyPlanByOrderId(sendOrderId);
            List<ViewSupplyplan> doneList = new ArrayList<>();
            List<Long> arrids = new ArrayList<>();
            if (list.size() > 0) {
                if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), list.get(0).getWerks() + list.get(0).getLgort()).equals(0L)) {
                    message = "??????????????? " + list.get(0).getWerkst() + "  " + list.get(0).getLgortName() + " ????????????";
                } else {
                    for (ViewSupplyplan en : list
                    ) {

                        if (en.getStatus().equals(1)) {
                            message += "????????????????????????";
                            break;
                        } else if (!en.getgMenge().equals(en.getDoneMenge())) {
                            message += "????????????" + en.getId().toString() + ":???????????????????????????????????????,?????????:";
                            message += GetUndoScmSupplyPlanD(en.getId().toString());
                        } else {
                            en.setStatus(1);
                            doneList.add(en);
                            arrids.add(en.getId());
                        }

                    }
                }
            }

            if (!StringUtils.isNotBlank(message)) {
                log.error("??????SAP??????");
                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUsername(), doneList, currentUser.getUsername(), currentUser.getRealname(), "1", "X");
                String mess = "";
                for (BackFromSAP_SubPlan itSub : backMsg) {
                    if (!itSub.getMSTYPE().equals("S")) {
                        mess += (itSub.getZGYJH() + itSub.getMESS());
                    }
                }
                if (StringUtils.isNotBlank(mess)) {
                    throw new FebsException(mess);
                } else {
                    this.iScmBSupplyplanService.doneSupplyPlan(arrids);//??????scm??????
                }
            } else {
                throw new FebsException(message);
            }


        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    private String GetUndoScmSupplyPlanD(String BaseId) {
        String mess = "";
        LambdaQueryWrapper<ScmBSupplyplanD> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScmBSupplyplanD::getIsDeletemark, 1);
        queryWrapper.eq(ScmBSupplyplanD::getBaseId, BaseId);
        List<ScmBSupplyplanD> scmds = this.iScmBSupplyplanDService.list(queryWrapper);
        for (ScmBSupplyplanD scmd : scmds
        ) {
            if (scmd.getState().equals(0)) {
                mess += scmd.getId() + ",";
            }
        }
        return mess;
    }

    @Log("????????????????????????")
    @PutMapping("cancelSendOrder")
    @RequiresPermissions("scmBSupplyplan:cancelSendOrder")
    public void updateCancelScmBSendOrder(@Valid String sendOrderId) throws FebsException {
        try {
            message = "";
            User currentUser = FebsUtil.getCurrentUser();

            List<ViewSupplyplan> list = this.iViewSupplyplanService.getViewSupplyPlanByOrderId(sendOrderId);
            List<ViewSupplyplan> doneList = new ArrayList<>();
            List<Long> arrids = new ArrayList<>();
            if (list.size() > 0) {
                if (iViewSupplyplanService.findAreaCount(currentUser.getUserId().toString(), list.get(0).getWerks() + list.get(0).getLgort()).equals(0L)) {
                    message = "??????????????? " + list.get(0).getWerkst() + "  " + list.get(0).getLgortName() + " ????????????";
                } else {
                    for (ViewSupplyplan en : list
                    ) {
                        if (!en.getStatus().equals(1)) {
                            message += "???????????????????????????";
                            break;
                        }
                        else if (StringUtils.isNotEmpty(en.getMaterCode())) {
                            message += "?????????????????????????????????????????????";
                            break;
                        }
                        else {
                            en.setStatus(0);
                            doneList.add(en);
                            arrids.add(en.getId());
                        }

                    }
                }
            }

            if (!StringUtils.isNotBlank(message)) {
                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), doneList, currentUser.getUsername(), currentUser.getRealname(), "0", "X");
                String mess = "";
                for (BackFromSAP_SubPlan itSub : backMsg) {
                    if (!itSub.getMSTYPE().equals("S")) {
                        mess += (itSub.getZGYJH() + itSub.getMESS());
                    }
                }
                if (StringUtils.isNotBlank(mess)) {
                    throw new FebsException(mess);
                } else {
                    this.iScmBSupplyplanService.cancleSupplyPlan(arrids);//??????scm??????
                }

            } else {
                throw new FebsException(message);
            }


        } catch (Exception e) {
            message = e.getMessage();
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("????????????????????????")
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
            message = "????????????";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("????????????????????????")
    @DeleteMapping("deleteSendOrder2/{ids}")
    public void deleteSendOrders2(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            if (!this.iScmBSupplyplanService.canUpdateSendOrder(ids)) {
                throw new FebsException("???????????????????????????????????????");
            }
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setSendOrderCode("");
                this.iScmBSupplyplanService.updateSupplyplanOnly(scmBSupplyplan);

            }
            List<String> id_tr= Arrays.asList(ids.split(","));
            List<ViewSupplyplan> doneList= new ArrayList<>();
            if(id_tr.size()>0) {
                doneList = this.iViewSupplyplanService.getViewSupplyPlanByIds(id_tr);
            }
            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), doneList, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
            if (!backMsg.get(0).getMSTYPE().equals("S")) {
                log.error("SAP???????????????");
                throw new FebsException("SAP???????????????");
            } else {
                // this.iScmBSendorderService.updateFpjr(doneList.get(0).getSendOrderCode());//?????????????????????????????????
            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "????????????";
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }

    @Log("??????????????????")
    @DeleteMapping("deleteFpPlan/{ids}")
    public void deleteFpPlan(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            if (!this.iScmBSupplyplanService.canUpdateSendOrder(ids)) {
                throw new FebsException("???????????????????????????????????????");
            }
            String[] arr_ids = ids.split(StringPool.COMMA);
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setCode("");
                this.iScmBSupplyplanService.updateSupplyplanOnly(scmBSupplyplan);

            }
//            List<ViewSupplyplan> doneList = this.iViewSupplyplanService.getViewSupplyPlanByIds(ids);
//            RfcNOC rfc = new RfcNOC();
//            List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), doneList, currentUser.getUsername(), currentUser.getRealname(), "0", "U");
//            if (!backMsg.get(0).getMSTYPE().equals("S")) {
//                log.error("SAP???????????????");
//                throw new FebsException("SAP???????????????");
//            } else {
//                // this.iScmBSendorderService.updateFpjr(doneList.get(0).getSendOrderCode());//?????????????????????????????????
//            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "????????????";
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }



    @Log("??????")
    @DeleteMapping("/{ids}")
    public void deleteScmBSupplyplans(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            String[] arr_ids = ids.split(StringPool.COMMA);
            //if(this.iScmBSupplyplanService.)
            if (!iScmBSupplyplanService.HasPreDone(ids)) {
                throw new FebsException("??????????????????????????????????????????????????????");
            }
            for (String id :
                    arr_ids) {
                ScmBSupplyplan scmBSupplyplan = new ScmBSupplyplan();
                scmBSupplyplan.setId(Long.parseLong(id));
                scmBSupplyplan.setIsDeletemark(0);

                List<ViewSupplyplan> list = new ArrayList<>();
                list.add(this.iViewSupplyplanService.getById(Long.parseLong(id)));
                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC(currentUser.getUserId().toString(), list, currentUser.getUsername(), currentUser.getRealname(), "0", "D");
                if (!backMsg.get(0).getMSTYPE().equals("S")) {
                    log.error("??????" + scmBSupplyplan.getId().toString() + "SAP???????????????");
                    throw new FebsException("????????????,?????????SAP???????????????");
                } else {
                    this.iScmBSupplyplanService.updateScmBSupplyplan(scmBSupplyplan);
                }
            }
            //this.iScmBSupplyplanService.deleteScmBSupplyplans(arr_ids);
        } catch (Exception e) {
            message = "????????????";
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmBSupplyplan:export")
    public void export(QueryRequest request, ScmBSupplyplan scmBSupplyplan, HttpServletResponse response) throws FebsException {
        try {
            List<ScmBSupplyplan> scmBSupplyplans = this.iScmBSupplyplanService.findScmBSupplyplans(request, scmBSupplyplan).getRecords();
            ExcelKit.$Export(ScmBSupplyplan.class, response).downXlsx(scmBSupplyplans, false);
        } catch (Exception e) {
            message = "??????Excel??????";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmBSupplyplan detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmBSupplyplan scmBSupplyplan = this.iScmBSupplyplanService.getById(id);
        return scmBSupplyplan;
    }


    @PostMapping("kpprint")
    public FebsResponse Generate23(@NotBlank(message = "{required}") String id) {
        FebsResponse feb = new FebsResponse();
        QueryRequest request= new QueryRequest();
        request.setPageNum(1);
        request.setPageSize(1000);
        ViewSupplyplanNew viewSupplyplanNew= new ViewSupplyplanNew();
        viewSupplyplanNew.setCode(id);
        viewSupplyplanNew.setIsDeletemark(1);
        List<ViewSupplyplanNew> e1 = iViewSupplyplanNewService.findViewSupplyplans_byCode(request,viewSupplyplanNew).getRecords();
       double totalJr= e1.stream().mapToDouble(p->Convert.toDouble(p.getFpjr())).sum();
        StringBuilder sb = new StringBuilder();
        if (e1 != null && e1.size() > 0) {

            sb.append(String.format(GenerateHeadStr_kp(e1.get(0).getCode().toString()), e1.get(0).getGysaccount(), e1.get(0).getGysname(), e1.get(0).getWerkst() + "  " + e1.get(0).getLgortName()));
           // sb.append(String.format(GenerateTabHeadStr(), "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "??????", "??????", "??????", "??????",  "????????????", "????????????", "????????????"));
            sb.append(String.format(GenerateTabHeadStr(), "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "??????", "??????", "??????", "??????", "????????????", "????????????", "????????????", "????????????"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (ViewSupplyplanNew f2 : e1) {
               // sb.append(String.format(GenerateRowStr(), sdf.format(f2.getBedat()), f2.getId().toString(), f2.getMatnr(), f2.getTxz01(), String.format("%.2f", f2.getMenge()), String.format("%.2f", f2.getgMenge()), f2.getMseht(), String.format("%.2f", f2.getNetpr()), String.format("%.2f", (f2.getNetpr().multiply(f2.getgMenge()))), f2.getCharge(),  String.format("%.2f", f2.getFpjr()), f2.getOutCause() == null ? "" : f2.getOutCause(), f2.getOutDate() == null ? "" : sdf.format(f2.getOutDate())));
                sb.append(String.format(GenerateRowStr(), sdf.format(f2.getBedat()), f2.getId2().toString(), f2.getMatnr(), f2.getTxz01(), String.format("%.2f", f2.getMenge()), String.format("%.2f", f2.getgMenge()), f2.getMseht(), String.format("%.2f", f2.getNetpr()), String.format("%.2f", (f2.getNetpr().multiply(f2.getgMenge()))), f2.getCharge(), f2.getFphm()==null?"":f2.getFphm(), String.format("%.2f", f2.getFpjr()), f2.getOutCause() == null ? "" : f2.getOutCause(), f2.getOutDate() == null ? "" : sdf.format(f2.getOutDate())));
            }
            sb.append(String.format("<tr><td colspan=\"4\" style=\"height:30px;font-family:??????;border-top:solid 1px black;text-align:left;font-size: 12px;\" >?????????(??????)??? %1$s</td><td colspan=\"4\" style=\"height:30px;font-family:??????;border-top:solid 1px black;font-size: 12px;\" >????????????(??????)???</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:??????;font-size: 12px;\" >???????????????%3$s</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:??????;font-size: 12px;\" >????????????%2$s</td></tr>", e1.get(0).getGysname(),totalJr,DateUtil.format(new Date(),"yyyy-MM-dd")));
            sb.append("</table>");
        } else {
            sb.append("????????????????????????!");
        }
        feb.data(sb.toString());
        return feb;
    }

    @PostMapping("print")
    public FebsResponse Generate(@NotBlank(message = "{required}") String id, String bsart) {
        FebsResponse feb = new FebsResponse();
        List<ViewSupplyplan> e1 = iViewSupplyplanService.findVPlanByOrderCode(id);
        StringBuilder sb = new StringBuilder();
        if (e1 != null && e1.size() > 0) {
            double total_money= e1.stream().mapToDouble(p->Convert.toDouble(p.getFpjr())).sum();
            sb.append(String.format(GenerateHeadStr(e1.get(0).getSendOrderCode().toString()), e1.get(0).getGysaccount(), e1.get(0).getGysname(), e1.get(0).getWerkst() + "  " + e1.get(0).getLgortName()));
            // sb.append(String.format(GenerateTabHeadStr(), "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "??????", "??????", "??????", "??????",  "????????????", "????????????", "????????????"));
            sb.append(String.format(GenerateTabHeadStr(), "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "??????", "??????", "??????", "??????", "????????????", "?????????", "????????????", "??????"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (ViewSupplyplan f2 : e1) {
                // sb.append(String.format(GenerateRowStr(), sdf.format(f2.getBedat()), f2.getId().toString(), f2.getMatnr(), f2.getTxz01(), String.format("%.2f", f2.getMenge()), String.format("%.2f", f2.getgMenge()), f2.getMseht(), String.format("%.2f", f2.getNetpr()), String.format("%.2f", (f2.getNetpr().multiply(f2.getgMenge()))), f2.getCharge(),  String.format("%.2f", f2.getFpjr()), f2.getOutCause() == null ? "" : f2.getOutCause(), f2.getOutDate() == null ? "" : sdf.format(f2.getOutDate())));
                sb.append(String.format(GenerateRowStr(), sdf.format(f2.getBedat()), f2.getId().toString(), f2.getMatnr(), f2.getTxz01(), String.format("%.2f", f2.getMenge()), String.format("%.2f", f2.getgMenge()), f2.getMseht(), String.format("%.2f", f2.getNetpr()), String.format("%.2f",  f2.getFpjr()), f2.getCharge(), f2.getFphm()==null?"":f2.getFphm(), f2.getVfdat() == null ? "" : sdf.format(f2.getVfdat()), f2.getOutCause() == null ? "" : f2.getOutCause(), f2.getLinkTelephone()==null?"":f2.getLinkTelephone()));
            }
            sb.append(String.format("<tr><td colspan=\"4\" style=\"height:30px;font-family:??????;border-top:solid 1px black;text-align:left;font-size: 12px;\" >?????????(??????)??? %1$s</td><td colspan=\"4\" style=\"height:30px;font-family:??????;border-top:solid 1px black;font-size: 12px;\" >????????????(??????)???</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:??????;font-size: 12px;\" >???????????????%3$s</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:??????;font-size: 12px;\" >???????????????%2$s</td></tr>", e1.get(0).getGysname(),DateUtil.format(new Date(),"yyyy-MM-dd"),String.format("%.2f", total_money)));
            sb.append("</table>");
        } else {
            sb.append("????????????????????????!");
        }
        feb.data(sb.toString());
        return feb;
    }
    public String GenerateHeadStr_kp(String code) {
        StringBuilder sb = new StringBuilder();
        String mark = GenerateMark(code);
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        sb.append(String.format("<tr><td colspan=\"12\" style=\"height:50px;font-family:??????;text-align:center;font-size: 20px;\" >%1$s</td><td colspan=\"2\" ><img alt=\"????????????\" id=\"im_14\" src=\"%2$s\"  style=\"text-align:center; width:80px; height:80px;\"/></td></tr>", "????????????????????????????????????", mark));
        sb.append("<tr><td colspan=\"3\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >??????????????????%1$s</td>");
        sb.append("<td colspan=\"4\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >??????????????????%2$s</td>");
        sb.append("<td colspan=\"5\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >?????????%3$s</td>");
        sb.append(String.format("<td colspan=\"2\" style=\"height:40px;font-family:??????;text-align:center;font-size: 12px;\" >%1$s</td><tr>", code));
        return sb.toString();

    }
    public String GenerateHeadStr(String orderCode) {
        StringBuilder sb = new StringBuilder();
        String mark = GenerateMark(orderCode);
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        sb.append(String.format("<tr><td colspan=\"12\" style=\"height:50px;font-family:??????;text-align:center;font-size: 20px;\" >%1$s</td><td colspan=\"2\" ><img alt=\"????????????\" id=\"im_14\" src=\"%2$s\"  style=\"text-align:center; width:80px; height:80px;\"/></td></tr>", "????????????????????????????????????", mark));
        sb.append("<tr><td colspan=\"3\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >??????????????????%1$s</td>");
        sb.append("<td colspan=\"4\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >??????????????????%2$s</td>");
        sb.append("<td colspan=\"5\" style=\"height:40px;font-family:??????;text-align:left;font-size: 12px;\" >?????????%3$s</td>");
        sb.append(String.format("<td colspan=\"2\" style=\"height:40px;font-family:??????;text-align:center;font-size: 12px;\" >%1$s</td><tr>", orderCode));
        return sb.toString();

    }

    public String GenerateTabHeadStr() {
        String reStr =
                "<tr>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%1$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%2$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%3$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%5$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%6$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%7$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%8$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%9$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%10$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%11$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%12$s" +
                        "</td>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%13$s" +
                        "</td>" +
                        "<td style=\"width: 100px;border-left:solid 1px black;border-top:solid 1px black;border-right:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%14$s" +
                        "</td>" +
                        "</tr>";

        return reStr;

    }

    public String GenerateRowStr() {
        String reStr =
                "<tr>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%1$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%2$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;text-align:right;border-top:solid 1px black;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%3$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%5$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%6$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%7$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%8$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%9$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%10$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%11$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%12$s" +
                        "</td>" +

                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%13$s" +
                        "</td>" +
                        "<td style=\"width: 100px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;border-right:solid 1px black;height:30px;font-family:??????;font-size: 12px;\">" +
                        "%14$s" +
                        "</td>" +
                        "</tr>";

        return reStr;

    }

    @PostMapping("printPlan")
    public FebsResponse PrintPlan(@NotBlank(message = "{required}") String ids) {
        log.info("ids:" + ids);
        FebsResponse feb = new FebsResponse();
        LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();
        String[] arr = ids.split(",");
        List<Long> arrids = new ArrayList<>();
        for (String id : arr) {
            arrids.add(Long.parseLong(id));
        }
        queryWrapper.eq(ViewSupplyplan::getIsDeletemark, 1);
        queryWrapper.in(ViewSupplyplan::getId, arrids);
        List<ViewSupplyplan> e1 = iViewSupplyplanService.list(queryWrapper);
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < arrids.size(); i++) {
            Long idl = arrids.get(i);

            List<ViewSupplyplan> entitys = e1.stream().filter((ViewSupplyplan s) -> s.getId().equals(idl)).collect(Collectors.toList());

            ViewSupplyplan entity = entitys.get(0);
            //?????? ??????????????? viki 2020-08-19
            String str_num = entity.getPkgNumber().toString();
            Integer num = Convert.toInt(str_num);
            BigDecimal menge_d = entity.getPkgAmount();
            BigDecimal leftMenge = NumberUtil.sub(entity.getgMenge(), NumberUtil.mul(new BigDecimal(num - 1), menge_d));
            for (Integer y = 1; y <= num; y++) {
                String sub_code = entity.getId().toString() + "_" + y.toString();
                String data = GenerateMark(sub_code);
                if (i == 0 && y == 1) {
                    GenerateCode(sb, data, entity.getTxz01(), entity.getMatnr(), entity.getCharge(), String.format("%.0f", entity.getMenge()), entity.getVfdat() == null ? "" : sdf.format(entity.getVfdat()), "", entity.getGysname(), sub_code, "", entity.getMseht(), entity.getPkgAmount() == null ? "" : String.format("%.0f", entity.getPkgAmount()), y.toString() + "/" + num.toString(), String.format("%.0f", entity.getgMenge()), entity.getWerkst(), entity.getName());
                } else {
                    GenerateCode(sb, data, entity.getTxz01(), entity.getMatnr(), entity.getCharge(), String.format("%.0f", entity.getMenge()), entity.getVfdat() == null ? "" : sdf.format(entity.getVfdat()), "", entity.getGysname(), sub_code, "page-break-before: always;", entity.getMseht(), entity.getPkgAmount() == null ? "" : String.format("%.0f", entity.getPkgAmount()), y.toString() + "/" + num.toString(), String.format("%.0f", entity.getgMenge()), entity.getWerkst(), entity.getName());
                }
            }
        }

        feb.data(sb.toString());
        return feb;
    }

    private String GenerateMark(String id) {
        String filename = UUID.randomUUID().toString() + ".png";
        final String projectPath = febsProperties.getUploadPath();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);

        String preMonth = sdf.format(cal.getTime());

        String fileMonthPath = projectPath + sdf.format(new Date());
        String filePreMonthPath = projectPath + preMonth;
        File file = new File(fileMonthPath);
        File file_p = new File(filePreMonthPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file_p.exists()) {
            file_p.delete();
        }

        String filepath = fileMonthPath + "\\" + filename;
        try {
            BarCodeUtil.generateQRCodeImage(id, 64, 64, filepath);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        return febsProperties.getBaseUrl() + "/uploadFile/" + sdf.format(new Date()) + "/" + filename;

    }

    public void GenerateCode(StringBuilder sb, String data, String TXZ01, String MATNR, String CHARG, String ORDER_MENGE, String VFDAT, String perPage, String CREATENAME, String GYJH, String fenPage, String MSEHT, String PKG_AMOUNT, String PKG_NUMBER, String NUMBER, String WERKST, String NAME) {
        String replaceStr = GenerateStr();
        String addTR = "";
        //if (fenPage != "")//?????????????????????20px????????????????????????????????????20px
        //{
        //    addTR = "<tr><td colspan=\"3\" style=\"width:340px;height:20px;\"></td></tr>";
        //}
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sb.append(String.format(replaceStr, TXZ01, MATNR, data, CHARG, ORDER_MENGE, VFDAT, sdf.format(new Date()), GYJH, CREATENAME, fenPage, MSEHT, PKG_AMOUNT, PKG_NUMBER, NUMBER, addTR, WERKST.replace("??????????????????", "").replace("-", ""), NAME));
    }

    public String GenerateStr() {
        String reStr = "<div style=\"width: 340px; margin: 0; padding: 0; font-family:??????;font-size: 14px; %10$s\"><table cellpadding=\"0\" cellspacing=\"0\">%15$s" +
                "<tr>" +
                "<td colspan=\"2\" style=\"width: 240px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????????????????????????????????????????" +
                "</td>" +
                "<td  style=\"width: 100px;height:16px;font-family:??????;font-size: 12px;\">" +
                "%7$s" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"width: 70px;height:30px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                "</td>" +
                "<td colspan=\"2\" style=\"width: 270px;height:30px;font-family:??????;font-size: 12px;\">" +
                "%1$s" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"width: 70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                " </td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%2$s" +
                "</td>" +
                "<td rowspan=\"6\" style=\"width: 80px;height:80px;\">" +
                "<img alt=\"????????????\" id=\"im_1\" src=\"%3$s\"  style=\" width:80px; height:80px;\"/>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "?????????" +
                "</td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%4$s" +
                "</td>" +
                "</tr>" +

                "<tr>" +
                "<td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                "</td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%6$s" +
                "</td>" +
                " </tr>" +
                "<tr>" +
                "<td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                "</td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%12$s%11$s/???" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "?????????" +
                "</td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%13$s" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                " <td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                "</td>" +
                "<td style=\"width: 170px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%14$s%11$s(%16$s)" +
                " </td>" +
                "</tr>" +
                "<tr>" +
                " <td style=\"width:70px;height:16px;font-family:??????;font-size: 14px;\">" +
                "???????????????" +
                "</td>" +
                "<td  colspan=\"2\"  style=\"width: 270px;height:16px;font-family:??????;font-size: 14px;\">" +
                "%5$s%11$s(%17$s)" +
                "<span style=\"float:right\">%8$s</sapn>" +
                " </td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"width:70px;height:30px;font-family:??????;vertical-align:top;font-size: 14px;\">" +
                "????????????" +
                "</td>" +
                "<td colspan=\"2\" style=\"width: 270px;height:30px;vertical-align:top;font-family:??????;font-size: 12px;\">" +
                "%9$s" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</div>";
        return reStr;

    }
    @PostMapping("materCode")
    public void UpdateMaterCode(String materCode,String ids){
        if(StringUtils.isNotEmpty(ids)){
            String[] arrids= ids.split(",");
            LambdaQueryWrapper<ScmBSupplyplan> lm= new LambdaQueryWrapper<>();
            lm.in(ScmBSupplyplan::getId,arrids);
            lm.eq(ScmBSupplyplan::getIsDeletemark,1);
            lm.eq(ScmBSupplyplan::getStatus,1);

            ScmBSupplyplan plan= new ScmBSupplyplan();
            if(materCode==null){
                plan.setMaterCode("");
            }
            else {
                plan.setMaterCode(materCode);
            }
            this.iScmBSupplyplanService.update(plan,lm);
        }
    }

}