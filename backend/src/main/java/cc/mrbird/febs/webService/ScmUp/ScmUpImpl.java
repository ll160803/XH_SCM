package cc.mrbird.febs.webService.ScmUp;

import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileToBase64Util;
import cc.mrbird.febs.common.utils.FtpUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.scm.RFC.BackFromSAP_SubPlan;
import cc.mrbird.febs.scm.RFC.RfcNOC;
import cc.mrbird.febs.scm.dao.ScmBGysMaterPicMapper;
import cc.mrbird.febs.scm.dao.ScmDMaterMapper;
import cc.mrbird.febs.scm.entity.*;
import cc.mrbird.febs.scm.service.*;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Service
@WebService(name = "upf", targetNamespace = "urn:ScmUp.webService.febs.mrbird.cc",
        endpointInterface = "cc.mrbird.febs.webService.ScmUp.IScmUpService"// 接口地址
)
public class ScmUpImpl implements IScmUpService {

    private final String userNyAccount="10000100";

    @Autowired
    private UserManager userManager;

    @Autowired
    private IComFileService iComFileService;

    @Autowired
    private ScmBGysMaterPicMapper scmBGysMaterPicMapper;

    @Autowired
    private ScmDMaterMapper scmDMaterMapper;

    @Autowired
    private FebsProperties febsProperties;

    @Autowired
    private IScmBGysfpService iScmBGysfpService;

    @Autowired
    public IViewSupplyplanNewService iViewSupplyplanNewService;

    @Autowired
    public IScmBFpplanService iScmBFpplanService;

    @Autowired
    public IViewSupplyplanService iViewSupplyplanService;

    public UpMess UploadFileAndCharge(String userName, String realName, String password, String matnr, String charge, String fileCname, String fileBa64Content, String fileName1, String fileBa64Content1) {
        UpMess Msg = new UpMess();

        List<String> arrUserAccount = new ArrayList<>();
        arrUserAccount.add(userNyAccount);
        if (!arrUserAccount.contains(userName)) {// 不被允许的用户
            Msg.setIsSuccess(false);
            Msg.setMess("不明原因错误");
            return Msg;
        }
        if (!StringUtils.isNotBlank(userName) || !StringUtils.isNotBlank(password) || !StringUtils.isNotBlank(matnr)
                || !StringUtils.isNotBlank(charge) || !StringUtils.isNotBlank(fileBa64Content) || !StringUtils.isNotBlank(fileCname)) {
            Msg.setIsSuccess(false);
            Msg.setMess("参数有误");
            return Msg;
        }

        try {
            String accountCode = userName.trim().replace("'", "");
            String username = StringUtils.lowerCase(accountCode);
            password = MD5Util.encrypt(username, password);

            User user = this.userManager.getUser(username);
            if (user == null) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }

            if (!StringUtils.equals(user.getPassword(), password)) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }
            if (User.STATUS_LOCK.equals(user.getStatus())) {
                Msg.setIsSuccess(false);
                Msg.setMess("账号已被锁定,请联系管理员！");
                return Msg;
            }

            String f_ID = SaveFile(Msg, fileBa64Content1, fileName1); //厂家发票
            if (!StringUtils.isNotBlank(f_ID)) {
                return Msg;
            }

            byte[] filebs = new BASE64Decoder().decodeBuffer(fileBa64Content);
            InputStream inputStream = null;
            inputStream = new ByteArrayInputStream(filebs);

            String fileName = UUID.randomUUID() + ".pdf"; // 新文件名

            try {
                log.info("接口开始上传ftp");
                FtpUtil ftpUtil = new FtpUtil();
                ftpUtil.uploadFile("SPL/PRD/", fileName, inputStream);
                log.info("接口上传pdf结束");
            } catch (Exception e) {
                Msg.setIsSuccess(false);
                log.error("转化PDF并存储失败:" + e.getMessage());
                Msg.setMess("转化PDF失败");
                return Msg;
            }
            log.info("33333333333");
            String Id = UUID.randomUUID().toString();
            ComFile cf = new ComFile();
            cf.setId(Id);
            cf.setCreateTime(new Date());
            cf.setClientName(fileCname);//客户端的名称
            cf.setServerName(fileName);
            iComFileService.createComFile(cf);

            LambdaQueryWrapper<ScmBGysMaterPic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScmBGysMaterPic::getMatnr, matnr);

            queryWrapper.eq(ScmBGysMaterPic::getGysaccount, userName);
            queryWrapper.eq(ScmBGysMaterPic::getCharge, charge);
            queryWrapper.eq(ScmBGysMaterPic::getIsDeletemark, 1);
            int count = this.scmBGysMaterPicMapper.selectCount(queryWrapper);
            if (count > 0) {
                Msg.setIsSuccess(false);
                log.error("存在相同的药品和批次");
                Msg.setMess("存在相同的药品和批次");
                return Msg;
            }
            LambdaQueryWrapper<ScmDMater> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(ScmDMater::getGysaccount, userName);
            queryWrapper2.eq(ScmDMater::getMatnr, matnr);
            List<ScmDMater> scmDMaters = this.scmDMaterMapper.selectList(queryWrapper2);
            ScmDMater scmDMater = new ScmDMater();
            if (scmDMaters.size() > 0) {
                scmDMater = scmDMaters.get(0);
            } else {
                Msg.setIsSuccess(false);
                Msg.setMess("不存在的药品编码");
                return Msg;
            }

            ScmBGysMaterPic scmBGysMaterPic = new ScmBGysMaterPic();
            scmBGysMaterPic.setCharge(charge);
            scmBGysMaterPic.setGysaccount(userName);
            scmBGysMaterPic.setName(realName);
            scmBGysMaterPic.setFileId(cf.getId());
            scmBGysMaterPic.setId(UUID.randomUUID().toString());
            scmBGysMaterPic.setState(0);
            scmBGysMaterPic.setMaterId(scmDMater.getId());
            scmBGysMaterPic.setMatnr(scmDMater.getMatnr());
            scmBGysMaterPic.setMseht(scmDMater.getMseht());
            scmBGysMaterPic.setProduceArea(scmDMater.getProduceArea());
            scmBGysMaterPic.setSpellCode(scmDMater.getSpellCode());
            scmBGysMaterPic.setSpec(scmDMater.getSpec());
            scmBGysMaterPic.setTxz01(scmDMater.getTxz01());
            scmBGysMaterPic.setCreateTime(new Date());
            scmBGysMaterPic.setMtart(f_ID);// 存入厂商发票附件文件ID

            Boolean flag = RfcNOC.SendUploadInfo_RFC(userName, matnr, charge, fileName, "I");
            if (flag) {
                log.info("666666666666666666");
                this.scmBGysMaterPicMapper.insert(scmBGysMaterPic);
            } else {
                log.info("操作失败，SAP端接收验收报告失败");
                Msg.setIsSuccess(false);
                Msg.setMess("SAP端接收验收报告失败");
                return Msg;
            }
            // this.scmBGysMaterPicMapper.insert(scmBGysMaterPic);

            Msg.setIsSuccess(true);
            Msg.setMess("");
            return Msg;
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            log.info(ex.getMessage());
            Msg.setMess("SCM接口出错");
            return Msg;

        }

    }

    public UpMess UploadFileFp(String userName, String realName, String password, String fphm, String fprq, String fpAmount, String fileName1, String fileBa64Content1, String fileName2, String fileBa64Content2) {
        UpMess Msg = new UpMess();

        List<String> arrUserAccount = new ArrayList<>();
        arrUserAccount.add(userNyAccount);
        if (!arrUserAccount.contains(userName)) {// 不被允许的用户
            Msg.setIsSuccess(false);
            Msg.setMess("不明原因错误");
            return Msg;
        }
        if (!StringUtils.isNotBlank(userName) || !StringUtils.isNotBlank(password) || !StringUtils.isNotBlank(fphm)
                || !StringUtils.isNotBlank(fprq) || !StringUtils.isNotBlank(fpAmount) || !StringUtils.isNotBlank(fileBa64Content1)
                || !StringUtils.isNotBlank(fileBa64Content2)) {
            Msg.setIsSuccess(false);
            Msg.setMess("参数有误");
            return Msg;
        }
        BigDecimal amount = new BigDecimal(0);
        try {
            amount = new BigDecimal(fpAmount);
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            Msg.setMess("发票金额参数有误");
            return Msg;
        }
        Date startDate;
        try {
            startDate = DateUtil.parse(fprq + " 00:00:00");
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            Msg.setMess("发票日期参数有误，应为日期格式");
            return Msg;
        }
        if (this.iScmBGysfpService.IsExist(fphm, userName, "", fprq.substring(0, 4))) {
            Msg.setIsSuccess(false);
            Msg.setMess("已经存在的的发票号码");
            return Msg;
        }
        try {
            String accountCode = userName.trim().replace("'", "");
            String username = StringUtils.lowerCase(accountCode);
            password = MD5Util.encrypt(username, password);

            User user = this.userManager.getUser(username);
            if (user == null) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }

            if (!StringUtils.equals(user.getPassword(), password)) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }
            if (User.STATUS_LOCK.equals(user.getStatus())) {
                Msg.setIsSuccess(false);
                Msg.setMess("账号已被锁定,请联系管理员！");
                return Msg;
            }


            String f_ID = SaveFile(Msg, fileBa64Content1, fileName1); //厂家发票
            if (!StringUtils.isNotBlank(f_ID)) {
                return Msg;
            }
            String f_ID2 = SaveFile(Msg, fileBa64Content2, fileName2); //供应商发票
            if (!StringUtils.isNotBlank(f_ID2)) {
                return Msg;
            }

            ScmBGysfp scmBGysfp = new ScmBGysfp();
            //scmBGysfp.setCreateUserId(userName);
            scmBGysfp.setFpAmount(amount);
            scmBGysfp.setFpHm(fphm);
            scmBGysfp.setFprq(startDate);
            scmBGysfp.setGysaccount(userName);
            scmBGysfp.setGysName(realName);
            scmBGysfp.setCreateTime(new Date());
            scmBGysfp.setState(0);
            scmBGysfp.setFileId(f_ID);//厂家
            scmBGysfp.setMaterId(f_ID2); //供应商的
            scmBGysfp.setIsDeletemark(1);
            this.iScmBGysfpService.createScmBGysfp(scmBGysfp);
            Msg.setIsSuccess(true);
            Msg.setMess("");
            return Msg;
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            log.info(ex.getMessage());
            Msg.setMess("SCM接口出错");
            return Msg;

        }
    }

    public String SaveFile(UpMess msg, String fileBa64Content1, String fileName2) {
        String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名
        String filePath = febsProperties.getUploadPath(); // 上传后的路径
        String fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            FileToBase64Util.decoderBase64File(fileBa64Content1, filePath + fileName);
        } catch (Exception ex) {
            msg.setIsSuccess(false);
            msg.setMess("转换文件失败");
            return "";
        }
        String Id = UUID.randomUUID().toString();
        ComFile cf = new ComFile();
        cf.setId(Id);
        cf.setCreateTime(new Date());
        cf.setClientName(fileName2);//客户端的名称
        cf.setServerName(fileName);
        iComFileService.createComFile(cf);
        return Id;


    }


    private String AddData(String supplyPlanIds,String username,String realname,UpMess msg){
        try {
            ScmBFpplan scmBFpplan = new ScmBFpplan();
            scmBFpplan.setGysaccount(username);
            scmBFpplan.setGysname(realname);
            scmBFpplan.supplyPlanIds = supplyPlanIds;

            String werks = "";
            String lgort = "";
            String zq = ""; //是否同一个账期
            if (StringUtils.isNotBlank(supplyPlanIds)) {


                String[] pIds = supplyPlanIds.split(",");
                List<String> pid1 = new ArrayList<>();
                List<String> pid2 = new ArrayList<>();
                for (String iid : pIds
                ) {
                    if (iid.contains("*")) {
                        pid2.add(iid.replace("*", ""));
                    } else {
                        pid1.add(iid);
                    }
                }


                List<ViewSupplyplan> listvp = new ArrayList<>();
                if (pid1.size() > 0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyPlanByIds(pid1));
                }
                if (pid2.size() > 0) {
                    listvp.addAll(this.iViewSupplyplanService.getViewSupplyNewPlanByIds(pid2));
                }
                double jr = listvp.stream().mapToDouble(p -> Convert.toDouble(p.getFpjr())).sum();

                for (ViewSupplyplan vp2 : listvp
                ) {
                    if (werks.equals("")) {
                        werks = vp2.getWerks();
                        lgort = vp2.getLgort();
                        zq = vp2.getMaterCode();
                    } else {
                        if (!(werks.equals(vp2.getWerks()) && (!werks.equals("2000") || lgort.equals(vp2.getLgort())) && zq.substring(0, 7).equals(vp2.getMaterCode().substring(0, 7)))) {
                            // throw new FebsException(vp2.getId().toString() + ":" + vp2.getWerkst() + "-" + vp2.getLgortName() + "不一致或入账月份不一致");
                            msg.setIsSuccess(false);
                            msg.setMess(vp2.getId().toString() + ":" + vp2.getWerkst() + "-" + vp2.getLgortName() + "不一致或入账月份不一致");
                            return "";
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
            return scmBFpplan.getId().toString();
        }
        catch (Exception ex){
            msg.setIsSuccess(false);
            msg.setMess("创建开票清单失败:"+ex.getMessage());
        }
      return  "";
    }
    public UpMess UploadFileFpList(String userName, String realName, String password, String fphm, String fprq, String fpAmount,
                                   String fileName2, String fileBa64Content2,  String supplyPlanIds) {
        UpMess Msg = new UpMess();

        String fpBm=""; //开票清单
        List<String> arrUserAccount = new ArrayList<>();
        arrUserAccount.add(userNyAccount);
        if (!arrUserAccount.contains(userName)) {// 不被允许的用户
            Msg.setIsSuccess(false);
            Msg.setMess("不明原因错误");
            return Msg;
        }
        if (!StringUtils.isNotBlank(userName) || !StringUtils.isNotBlank(password) || !StringUtils.isNotBlank(fphm)
                || !StringUtils.isNotBlank(fprq) || !StringUtils.isNotBlank(fpAmount) || !StringUtils.isNotBlank(supplyPlanIds)
                || !StringUtils.isNotBlank(fileBa64Content2)) {
            Msg.setIsSuccess(false);
            Msg.setMess("参数有误");
            return Msg;
        }
        BigDecimal amount = new BigDecimal(0);
        try {
            amount = new BigDecimal(fpAmount);
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            Msg.setMess("发票金额参数有误");
            return Msg;
        }
        Date startDate;
        try {
            startDate = DateUtil.parse(fprq + " 00:00:00");
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            Msg.setMess("发票日期参数有误，应为日期格式");
            return Msg;
        }

        if (this.iScmBGysfpService.IsExist(fphm, userName, "", fprq.substring(0, 4))) {
            Msg.setIsSuccess(false);
            Msg.setMess("已经存在的的发票号码");
            return Msg;
        }
        try {
            String accountCode = userName.trim().replace("'", "");
            String username = StringUtils.lowerCase(accountCode);
            password = MD5Util.encrypt(username, password);

            User user = this.userManager.getUser(username);
            if (user == null) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }

            if (!StringUtils.equals(user.getPassword(), password)) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }
            if (User.STATUS_LOCK.equals(user.getStatus())) {
                Msg.setIsSuccess(false);
                Msg.setMess("账号已被锁定,请联系管理员！");
                return Msg;
            }


             fpBm= AddData(supplyPlanIds,username,realName,Msg);
            if(StringUtils.isEmpty(fpBm)){
                return Msg;
            }



            String gysFpId = UUID.randomUUID().toString();

            String f_ID2 = SaveFile(Msg, fileBa64Content2, fileName2); //供应商发票
            if (!StringUtils.isNotBlank(f_ID2)) {
                return Msg;
            }
            ScmBGysfp scmBGysfp = new ScmBGysfp();
            scmBGysfp.setId(gysFpId);
            //scmBGysfp.setCreateUserId(userName);
            scmBGysfp.setFpAmount(amount);
            scmBGysfp.setFpHm(fphm);
            scmBGysfp.setFpBm(fpBm);
            scmBGysfp.setFprq(startDate);
            scmBGysfp.setGysaccount(userName);
            scmBGysfp.setGysName(realName);
            scmBGysfp.setCreateTime(new Date());
            scmBGysfp.setState(0);
            // scmBGysfp.setFileId(f_ID);//厂家
            scmBGysfp.setMaterId(f_ID2); //供应商的
            scmBGysfp.setIsDeletemark(1);

            if (StringUtils.isNotEmpty(fpBm)) {

                LambdaQueryWrapper<ScmBFpplan> queryWrapper45 = new LambdaQueryWrapper<>();
                queryWrapper45.eq(ScmBFpplan::getIsDeletemark, 1);//1是未删 0是已删

                if (StringUtils.isNotEmpty(fphm)) {
                    queryWrapper45.and(wrap -> wrap.eq(ScmBFpplan::getFphm, "").or().eq(ScmBFpplan::getFphm, fphm)
                            .or().isNull(ScmBFpplan::getFphm));
                }

                queryWrapper45.eq(ScmBFpplan::getGysaccount, userName);
                queryWrapper45.eq(ScmBFpplan::getFpjr, fpAmount);
                if (this.iScmBFpplanService.count(queryWrapper45) <= 0) {
                    Msg.setIsSuccess(false);
                    Msg.setMess("不存在的开票清单，请检查开票清单号和发票金额是否对应");
                    return Msg;
                }


                ScmBFpplan scmBFpplan = new ScmBFpplan();
                scmBFpplan.setId(Long.parseLong(fpBm));
                scmBFpplan.setFphm(scmBGysfp.getFpHm());
                scmBFpplan.setFprq(scmBGysfp.getFprq());

                List<ViewSupplyplan> list = new ArrayList<>();
                list.addAll(this.iViewSupplyplanService.findVPlanByCode(scmBGysfp.getFpBm()));
                LambdaQueryWrapper<ViewSupplyplanNew> queryWrapper = new LambdaQueryWrapper<>();

                queryWrapper.eq(ViewSupplyplanNew::getIsDeletemark, 1);
                queryWrapper.eq(ViewSupplyplanNew::getCode, scmBGysfp.getFpBm());
                List<ViewSupplyplanNew> listNew = this.iViewSupplyplanNewService.list(queryWrapper);
                listNew.forEach(p -> {
                    ViewSupplyplan plan = new ViewSupplyplan();
                    BeanUtil.copyProperties(p, plan, CopyOptions.create().setIgnoreNullValue(true));
                    list.add(plan);
                });

                list.parallelStream().forEach(item -> {
                    item.setFphm(scmBGysfp.getFpHm());
                    item.setFprq(scmBGysfp.getFprq());
                });
                RfcNOC rfc = new RfcNOC();
                List<BackFromSAP_SubPlan> backMsg = rfc.SendSupplyPlan_RFC("37", list, userNyAccount, "南京医药湖北有限公司", "2", "U");//这里是写死的
                if (!backMsg.get(0).getMSTYPE().equals("S")) {
                    Msg.setIsSuccess(false);
                    Msg.setMess("新增发票数据,SAP端接收失败");
                    return Msg;
                }

                this.iScmBFpplanService.updateScmBFpplan(scmBFpplan); //2022-06-13
                this.iScmBFpplanService.updateFpData(scmBGysfp.getFpBm(), DateUtil.format(scmBGysfp.getFprq(), "yyyy-MM-dd"), scmBGysfp.getFpHm());
            }


            this.iScmBGysfpService.createScmBGysfpJieKou(scmBGysfp);
            Msg.setIsSuccess(true);
            Msg.setMess("");
            return Msg;
        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            log.info(ex.getMessage());
            Msg.setMess("SCM接口出错");
            return Msg;

        }
        finally {
            if(StringUtils.isNotEmpty(fpBm) && !Msg.getIsSuccess()){ //如果已经创建开票清单  则删除
                this.iScmBFpplanService.updateDeletePlan(fpBm,Long.parseLong(fpBm));
            }
        }
    }

    public String hello() {
        return "haha";
    }

    public UpMess DownLoadPlan(@WebParam(name = "userName") String userName, @WebParam(name = "realName") String realName, @WebParam(name = "password") String password) {
        UpMess Msg = new UpMess();

        List<String> arrUserAccount = new ArrayList<>();
        arrUserAccount.add(userNyAccount);
        if (!arrUserAccount.contains(userName)) {// 不被允许的用户
            Msg.setIsSuccess(false);
            Msg.setMess("不明原因错误");
            return Msg;
        }
        if (!StringUtils.isNotBlank(userName) || !StringUtils.isNotBlank(password)) {
            Msg.setIsSuccess(false);
            Msg.setMess("参数有误");
            return Msg;
        }

        try {
            String accountCode = userName.trim().replace("'", "");
            String username = StringUtils.lowerCase(accountCode);
            password = MD5Util.encrypt(username, password);

            User user = this.userManager.getUser(username);
            if (user == null) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }

            if (!StringUtils.equals(user.getPassword(), password)) {
                Msg.setIsSuccess(false);
                Msg.setMess("您的用户名或密码有误");
                return Msg;
            }
            if (User.STATUS_LOCK.equals(user.getStatus())) {
                Msg.setIsSuccess(false);
                Msg.setMess("账号已被锁定,请联系管理员！");
                return Msg;
            }
            ViewSupplyplan scmBSupplyplan= new ViewSupplyplan();
            scmBSupplyplan.setIsDeletemark(1);
            scmBSupplyplan.setGysaccount(username);
            scmBSupplyplan.setStatus(1);

            QueryRequest request =new QueryRequest();
            request.setPageNum(1);
            request.setPageSize(10000);
            request.setSortField("id");

            List<ViewSupplyplan> records=this.iViewSupplyplanService.findViewSupplyplans_byMaterCode(request, scmBSupplyplan).getRecords();
            Msg.setIsSuccess(true);
            Msg.setMess("");
            Msg.setPlans(records);
            return Msg;


        } catch (Exception ex) {
            Msg.setIsSuccess(false);
            Msg.setMess(ex.getMessage());
        }
        return Msg;
    }
}
