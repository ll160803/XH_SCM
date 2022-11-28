package cc.mrbird.febs.his.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.utils.YuanQuUtil;
import cc.mrbird.febs.his.entity.ScmWChange;
import cc.mrbird.febs.his.service.IScmWChangeService;
import cc.mrbird.febs.his.service.IScmWFpplanService;
import cc.mrbird.febs.his.entity.ScmWFpplan;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2022-10-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmWFpplan")

public class ScmWFpplanController extends BaseController {

    private String message;
    @Autowired
    public IScmWFpplanService iScmWFpplanService;

    @Autowired
    public IScmWChangeService iScmWChangeService;

    /**
     * 分页查询数据
     *
     * @param request    分页信息
     * @param scmWFpplan 查询条件
     * @return
     */
    @GetMapping
    public Map<String, Object> List(QueryRequest request, ScmWFpplan scmWFpplan) {
        User currentUser = FebsUtil.getCurrentUser();
        scmWFpplan.setGysname(currentUser.getRealname());
        return getDataTable(this.iScmWFpplanService.findScmWFpplans(request, scmWFpplan));
    }

    /**
     * 添加
     *
     * @param scmWFpplan
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("scmWFpplan:add")
    public void addScmWFpplan(@Valid ScmWFpplan scmWFpplan) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmWFpplan.setCreateUserId(currentUser.getUserId());
            this.iScmWFpplanService.createScmWFpplan(scmWFpplan);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("新增开票数据")
    @PostMapping("planAdd")
    public void addFpPlan(@Valid ScmWFpplan scmWFpplan, String supplyPlanIds) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmWFpplan.setCreateUserId(currentUser.getUserId());
            scmWFpplan.setGysaccount(currentUser.getUsername());
            scmWFpplan.setGysname(currentUser.getRealname());

            LambdaQueryWrapper<ScmWChange> queryWrapper = new LambdaQueryWrapper<>();

            List<ScmWChange> listvp = new ArrayList<>();
            String werks = "";
            String zq = ""; //是否同一个账期
            if (StringUtils.isNotBlank(supplyPlanIds)) {
                String[] pIds = supplyPlanIds.split(",");


                queryWrapper.in(ScmWChange::getId, pIds);
                queryWrapper.eq(ScmWChange::getState, 0);
                queryWrapper.eq(ScmWChange::getIsDeletemark, 1);


                listvp = this.iScmWChangeService.list(queryWrapper);
                double jr = listvp.stream().mapToDouble(p -> Convert.toDouble(p.getJhJe())).sum();

                for (ScmWChange vp2 : listvp
                ) {
                    if (werks.equals("")) {
                        werks = vp2.getFyId();
                    } else {
                        if (!werks.equals(vp2.getFyId())) {
                            throw new FebsException(vp2.getId().toString() + ":院区不一致");
                        }
                    }
                }
                scmWFpplan.setWerks(listvp.get(0).getFyId());
                // scmWFpplan.setLgortName(listvp.get(0).getKw());
                scmWFpplan.setFpjr(new BigDecimal(jr));
            }
            this.iScmWFpplanService.createScmWFpplan(scmWFpplan);


            RfcNOC rfc = new RfcNOC();
            List<BackFromSAP_SubPlan> backList = rfc.SendFp_RFC(currentUser.getUsername(), listvp, scmWFpplan.getFphm(), Double.parseDouble(scmWFpplan.getFpjrReal().toString()), scmWFpplan.getId().toString(), DateUtil.format(scmWFpplan.getFprq(), "yyyy-MM-dd"));
            if (backList.get(0).getMSTYPE().equals("S")) {
                if (StringUtils.isNotBlank(supplyPlanIds)) {

                    ScmWChange chang = new ScmWChange();
                    chang.setState(1);
                    chang.setCode(scmWFpplan.getId().toString());
                    this.iScmWChangeService.update(chang, queryWrapper);
                }
            } else { //RFC 出错  删除已经创建的数据
                this.iScmWFpplanService.deleteScmWFpplans(new String[]{scmWFpplan.getId().toString()});
            }
        } catch (Exception e) {

            //log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }

    @PostMapping("kpprint")
    public FebsResponse Generate23(@NotBlank(message = "{required}") String id) {
        FebsResponse feb = new FebsResponse();
        QueryRequest request = new QueryRequest();
        request.setPageNum(1);
        request.setPageSize(1000);

        LambdaQueryWrapper<ScmWChange> changeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        changeLambdaQueryWrapper.eq(ScmWChange::getState, 1);
        changeLambdaQueryWrapper.eq(ScmWChange::getCode, id);
        changeLambdaQueryWrapper.eq(ScmWChange::getIsDeletemark, 1);
        List<ScmWChange> e1 = iScmWChangeService.list(changeLambdaQueryWrapper);

        ScmWFpplan fpplan = this.iScmWFpplanService.getById(Long.parseLong(id));

        String totalJr = String.format("%.2f", fpplan.getFpjrReal());
        StringBuilder sb = new StringBuilder();
        if (e1 != null && e1.size() > 0) {

            sb.append(String.format(GenerateHeadStr_kp(e1.get(0).getCode().toString()), e1.get(0).getGysId(), e1.get(0).getGysName(), YuanQuUtil.getMcById(e1.get(0).getFyId())));
            // sb.append(String.format(GenerateTabHeadStr(), "订单日期", "供应计划", "药品编码", "药品名称", "计划数量", "送货数量", "单位", "单价", "金额", "批次",  "供应金额", "缺货原因", "补送日期"));
            sb.append(String.format(GenerateTabHeadStr(), "唯一值", "调价日期", "业务所属月份", "院区", "库位", "药品类别", "药品编码", "药品名称", "产地代码", "数量", "单位", "原进货价", "新进货价", "调价金额"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (ScmWChange f2 : e1) {
                // sb.append(String.format(GenerateRowStr(), sdf.format(f2.getBedat()), f2.getId().toString(), f2.getMatnr(), f2.getTxz01(), String.format("%.2f", f2.getMenge()), String.format("%.2f", f2.getgMenge()), f2.getMseht(), String.format("%.2f", f2.getNetpr()), String.format("%.2f", (f2.getNetpr().multiply(f2.getgMenge()))), f2.getCharge(),  String.format("%.2f", f2.getFpjr()), f2.getOutCause() == null ? "" : f2.getOutCause(), f2.getOutDate() == null ? "" : sdf.format(f2.getOutDate())));
                sb.append(String.format(GenerateRowStr(), f2.getId(), f2.getTjRq(), f2.getNy(), YuanQuUtil.getMcById(f2.getFyId()), f2.getKw(), f2.getYpLb(), f2.getYpBh(), f2.getYpMc(), f2.getYpCd(), String.format("%.2f", f2.getSl()), f2.getDw(), String.format("%.2f", f2.getJhOld()), String.format("%.2f", f2.getJhNew()), String.format("%.2f", f2.getJhJe())));
            }
            sb.append(String.format("<tr><td colspan=\"4\" style=\"height:30px;font-family:宋体;border-top:solid 1px black;text-align:left;font-size: 12px;\" >供应商(盖章)： %1$s</td><td colspan=\"4\" style=\"height:30px;font-family:宋体;border-top:solid 1px black;font-size: 12px;\" >采购中心(签字)：</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:宋体;font-size: 12px;\" >打印日期：%3$s</td><td colspan=\"3\" style=\"height:30px;border-top:solid 1px black;font-family:宋体;font-size: 12px;\" >总金额：%2$s</td></tr>", e1.get(0).getGysName(), totalJr, DateUtil.format(new Date(), "yyyy-MM-dd")));
            sb.append("</table>");
        } else {
            sb.append("尚未添加调价数据!");
        }
        feb.data(sb.toString());
        return feb;
    }

    public String GenerateHeadStr_kp(String code) {
        StringBuilder sb = new StringBuilder();
        //  String mark = GenerateMark(code);
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        sb.append(String.format("<tr><td colspan=\"12\" style=\"height:50px;font-family:宋体;text-align:center;font-size: 20px;\" >%1$s</td><td colspan=\"2\" ></td></tr>", "武汉协和医院调价开票清单"));
        sb.append("<tr><td colspan=\"3\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >供应商编码：%1$s</td>");
        sb.append("<td colspan=\"4\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >供应商名称：%2$s</td>");
        sb.append("<td colspan=\"5\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >库房：%3$s</td>");
        sb.append(String.format("<td colspan=\"2\" style=\"height:40px;font-family:宋体;text-align:center;font-size: 12px;\" >%1$s</td><tr>", code));
        return sb.toString();

    }

    public String GenerateTabHeadStr() {
        String reStr =
                "<tr>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%1$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%2$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%3$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%5$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%6$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%7$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%8$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%9$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%10$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%11$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%12$s" +
                        "</td>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%13$s" +
                        "</td>" +
                        "<td style=\"width: 100px;border-left:solid 1px black;border-top:solid 1px black;border-right:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%14$s" +
                        "</td>" +
                        "</tr>";

        return reStr;

    }

    public String GenerateRowStr() {
        String reStr =
                "<tr>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%1$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%2$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;text-align:right;border-top:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%3$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%5$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%6$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%7$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%8$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%9$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%10$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:left;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%11$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:right;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%12$s" +
                        "</td>" +

                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%13$s" +
                        "</td>" +
                        "<td style=\"width: 100px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;border-right:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%14$s" +
                        "</td>" +
                        "</tr>";

        return reStr;

    }

    /**
     * 修改
     *
     * @param scmWFpplan
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("scmWFpplan:update")
    public void updateScmWFpplan(@Valid ScmWFpplan scmWFpplan) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmWFpplan.setModifyUserId(currentUser.getUserId());
            this.iScmWFpplanService.updateScmWFpplan(scmWFpplan);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    public void deleteScmWFpplans(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            User currentUser = FebsUtil.getCurrentUser();

            LambdaQueryWrapper<ScmWFpplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ScmWFpplan::getId, arr_ids);
            queryWrapper.eq(ScmWFpplan::getIsDeletemark, 1);
            List<ScmWFpplan> scmWFpplanList = this.iScmWFpplanService.list(queryWrapper);

            for (ScmWFpplan plan : scmWFpplanList
            ) {
                if (plan.getStatus().equals("1")) {
                    throw new Exception("已经入账的数据，不能删除");
                }
                LambdaQueryWrapper<ScmWChange> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(ScmWChange::getCode, plan.getId().toString());
                queryWrapper1.eq(ScmWChange::getState, 1);
                List<ScmWChange> changList = this.iScmWChangeService.list(queryWrapper1);

                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backList = rfc.SendFp_RFC(currentUser.getUsername(), changList, "", 0D, "", "");
                if (backList.get(0).getMSTYPE().equals("S")) {

                    ScmWChange change = new ScmWChange();
                    change.setCode("");
                    change.setState(0);
                    this.iScmWChangeService.update(change, queryWrapper1);

                    this.iScmWFpplanService.deleteScmWFpplans(arr_ids);
                }
                else{
                    throw new FebsException("SAP端调用失败");
                }

            }
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(e.getMessage());
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmWFpplan:export")
    public void export(QueryRequest request, ScmWFpplan scmWFpplan, HttpServletResponse response) throws FebsException {
        try {
            List<ScmWFpplan> scmWFpplans = this.iScmWFpplanService.findScmWFpplans(request, scmWFpplan).getRecords();
            ExcelKit.$Export(ScmWFpplan.class, response).downXlsx(scmWFpplans, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmWFpplan detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmWFpplan scmWFpplan = this.iScmWFpplanService.getById(id);
        return scmWFpplan;
    }
}