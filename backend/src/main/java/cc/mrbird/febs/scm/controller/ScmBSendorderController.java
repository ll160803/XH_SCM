package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import cc.mrbird.febs.scm.service.IScmBSendorderService;
import cc.mrbird.febs.scm.entity.ScmBSendorder;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.scm.service.IViewSupplyplanService;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author viki
 * @since 2019-11-26
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmBSendorder")

public class ScmBSendorderController extends BaseController {

    private String message;
    @Autowired
    public IScmBSendorderService iScmBSendorderService;
    @Autowired
    public IViewSupplyplanService iViewSupplyplanService;

    /**
     * 分页查询数据
     *
     * @param bootStrapTable 分页信息
     * @param scmBSendorder  查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("scmBSendorder:view")
    public Map<String, Object> List(QueryRequest request, ScmBSendorder scmBSendorder) {
        scmBSendorder.setIsDeletemark(1);
        User currentUser = FebsUtil.getCurrentUser();
        scmBSendorder.setGysaccount(currentUser.getUsername());
        log.error("art:"+scmBSendorder.getBsart());
        return getDataTable(this.iScmBSendorderService.findScmBSendorders(request, scmBSendorder));
    }

    /**
     * 跳转添加页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Log("物资新增/按钮")
    @PostMapping
    @RequiresPermissions("scmBSendorder:add")
    public void addScmBSendorder(@Valid ScmBSendorder scmBSendorder, String supplyPlanIds) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSendorder.setCreateUserId(currentUser.getUserId());
            scmBSendorder.setGysaccount(currentUser.getUsername());
            scmBSendorder.setGysname(currentUser.getRealname());
            scmBSendorder.supplyPlanIds = supplyPlanIds;
            this.iScmBSendorderService.createScmBSendorder(scmBSendorder);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @Log("药品新增/按钮")
    @PostMapping("OrderAdd")
    @RequiresPermissions("sendorder:add")
    public void addSendorder(@Valid ScmBSendorder scmBSendorder, String supplyPlanIds) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSendorder.setCreateUserId(currentUser.getUserId());
            scmBSendorder.setGysaccount(currentUser.getUsername());
            scmBSendorder.setGysname(currentUser.getRealname());
            scmBSendorder.supplyPlanIds = supplyPlanIds;
            this.iScmBSendorderService.createScmBSendorder(scmBSendorder);
        } catch (Exception e) {
            message = "新增/按钮失败";
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
    @RequiresPermissions("scmBSendorder:update")
    public void updateScmBSendorder(@Valid ScmBSendorder scmBSendorder) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmBSendorder.setModifyUserId(currentUser.getUserId());
            this.iScmBSendorderService.updateScmBSendorder(scmBSendorder);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("scmBSendorder:delete")
    public void deleteScmBSendorders(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmBSendorderService.deleteScmBSendorders(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmBSendorder:export")
    public void export(QueryRequest request, ScmBSendorder scmBSendorder, HttpServletResponse response) throws FebsException {
        try {
            List<ScmBSendorder> scmBSendorders = this.iScmBSendorderService.findScmBSendorders(request, scmBSendorder).getRecords();
            ExcelKit.$Export(ScmBSendorder.class, response).downXlsx(scmBSendorders, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmBSendorder detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmBSendorder scmBSendorder = this.iScmBSendorderService.getById(id);
        return scmBSendorder;
    }
    @PostMapping("print")
    public FebsResponse Generate(@NotBlank(message = "{required}") String id) {
        FebsResponse feb=new FebsResponse();
        LambdaQueryWrapper<ViewSupplyplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ViewSupplyplan::getSendOrderCode,id);
        List<ViewSupplyplan> entitys= iViewSupplyplanService.list(queryWrapper);
        StringBuilder sb = new StringBuilder();
        String markCode = GenerateMark(id);//生成送货清单二维码
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");

        String gysName = "";
        BigDecimal FPJR = entitys.stream().map(ViewSupplyplan::getFpjr).reduce(BigDecimal.ZERO,BigDecimal::add);
        for (int i = 0; i < entitys.size(); i++)
        {
            ViewSupplyplan entity = entitys.get(i);
            if (i == 0)
            {
                gysName = entity.getGysname();
                sb.append(String.format("<tr><td colspan=\"14\" style=\"height:50px;font-family:宋体;text-align:center;font-size: 20px;\" >%1$s</td></tr>",  "武汉协和医院总务物资送货清单"));
                sb.append(String.format("<tr><td colspan=\"4\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >供应商：%1$s</td>", entity.getGysaccount()));
                sb.append(String.format("<td colspan=\"1\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >院区：</td><td colspan=\"5\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >发票号码：%1$s</td><td colspan=\"3\" style=\"height:40px;font-family:宋体;font-size: 12px;\" >送货清单号：</td><td style=\"height:40px;font-family:宋体;font-size: 12px;\" rowspan=\"2\"> <img alt=\"显示出错\" id=\"im_1\" src=\"%4$s\"  style=\" width:80px; height:80px;\"/></td></tr>", entity.getFphm(), FPJR, id, markCode, entity.getWerkst()));
                sb.append(String.format("<tr><td colspan=\"4\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >%1$s</td>", entity.getGysname()));
                sb.append(String.format("<td colspan=\"1\" style=\"height:40px;font-family:宋体;text-align:left;font-size: 12px;\" >%5$s</td><td colspan=\"5\" style=\"height:40px;font-family:宋体;font-size: 12px;\" >发票金额：%2$.2f</td><td colspan=\"3\" style=\"height:40px;font-family:宋体;font-size: 12px;\" >%3$s</td></tr>", entity.getFphm(), FPJR, id, markCode, entity.getWerkst()));
                GenerateHeadCode(sb, "订单日期", "采购订单", "行项目", "物资编码", "物资描述", "采购数量", "送货数量", "基本单位", "单价", "金额", "送达科室", "负责人", "联系电话", "商品条码");
            }
            // var data = GenerateMark(entity.CODE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            GenerateCode(sb, sdf.format(entity.getBedat()), entity.getEbeln(), entity.getId().toString(), entity.getMatnr(), entity.getTxz01(), entity.getMenge().toString(), entity.getgMenge().toString(), entity.getMseht(), entity.getNetpr().toString(), entity.getFpjr().toString(), entity.getSendDepart(), entity.getLinkPerson(), entity.getLinkTelephone(), entity.getMatnr());
        }
        //GenerateBottomCode(sb, "", "", "", "", "", "", "", "", "", "", "", "", "", "");//最后一行空着
        sb.append(String.format("<tr><td colspan=\"5\" style=\"height:30px;font-family:宋体;border-top:solid 1px black;text-align:left;font-size: 12px;\" >供应商(盖章)：%1$s</td><td colspan=\"5\" style=\"height:30px;font-family:宋体;border-top:solid 1px black;font-size: 12px;\" >采购中心(签字)：</td><td colspan=\"4\" style=\"height:30px;border-top:solid 1px black;font-family:宋体;font-size: 12px;\" >打印日期：</td></tr>", gysName));
        sb.append("</table>");
        feb.data(sb.toString());
        return  feb;
    }
    public void GenerateCode(StringBuilder sb, String BEDAT, String EBELN, String EBELP, String MATNR, String TXZ01, String order_menge, String MENGE, String MSEHT, String PRICE, String MONEY, String SEND_DEPART_NAME, String LINK_PERSON, String LINK_TELEPHONE, String MATER_CODE)
    {
        String replaceStr = GenerateStr();
        sb.append(String.format(replaceStr, BEDAT, EBELN, EBELP, MATNR, TXZ01, order_menge, MENGE, MSEHT, PRICE, MONEY, SEND_DEPART_NAME, LINK_PERSON, LINK_TELEPHONE, MATER_CODE));
    }
    public String GenerateStr()
    {
        String reStr =
                "<tr>" +
                        "<td style=\"width: 80px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%1$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%2$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%3$s" +
                        "</td>" +
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
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
                        "<td style=\"width: 100px;border-left:solid 1px black;border-top:solid 1px black;border-right:solid 1px black;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%14$s" +
                        "</td>" +
                        "</tr>";

        return reStr;

    }

    public void GenerateHeadCode(StringBuilder sb, String BEDAT, String EBELN, String EBELP, String MATNR, String TXZ01, String order_menge, String MENGE, String MSEHT, String PRICE, String MONEY, String SEND_DEPART_NAME, String LINK_PERSON, String LINK_TELEPHONE, String MATER_CODE)
    {
        String replaceStr = GenerateHeadStr();
        sb.append(String.format(replaceStr, BEDAT, EBELN, EBELP, MATNR, TXZ01, order_menge, MENGE, MSEHT, PRICE, MONEY, SEND_DEPART_NAME, LINK_PERSON, LINK_TELEPHONE, MATER_CODE));
    }
    public String GenerateHeadStr()
    {
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
                        "<td style=\"width: 60px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
                        "%4$s" +
                        "</td>" +
                        "<td style=\"width: 240px;border-left:solid 1px black;border-top:solid 1px black;text-align:center;height:30px;font-family:宋体;font-size: 12px;\">" +
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


    public ResponseEntity<byte[]> getQRImage(String id) {
        byte[] qrcode = null;
        try {
            qrcode = GetQRCodeImage(id, 64, 64);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]> (qrcode, headers, HttpStatus.CREATED);
    }

    public  byte[] GetQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
    private  String GenerateMark(String id)
    {
        String filename = UUID.randomUUID().toString() + ".png";
        final String projectPath = "D:/Project_SCM/XH_SCM/frontend/static/uploadFile";
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);

        String preMonth = sdf.format(cal.getTime());

        String fileMonthPath = projectPath+"/" + sdf.format(new Date());
        String filePreMonthPath = projectPath +"/"+ preMonth;
        File file = new File(fileMonthPath);
        File file_p = new File(filePreMonthPath);
        if (!file.exists())
        {
            file.mkdirs();
        }
        if (file_p.exists())
        {
           file_p.delete();
        }

        String filepath = fileMonthPath + "\\" + filename;
        try {
            generateQRCodeImage(id,64,64,filepath);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        return "static/uploadFile/"+sdf.format(new Date())+"/"+filename;

    }
    private  void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}