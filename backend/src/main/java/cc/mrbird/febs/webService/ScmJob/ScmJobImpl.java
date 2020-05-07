package cc.mrbird.febs.webService.ScmJob;

import cc.mrbird.febs.scm.entity.*;
import cc.mrbird.febs.scm.service.*;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.List;

@Slf4j
@Component
@Service
@WebService(name = "sap", targetNamespace = "urn:ScmJob.webService.febs.mrbird.cc",
        endpointInterface = "cc.mrbird.febs.webService.ScmJob.IScmJobService"// 接口地址
)
public class ScmJobImpl implements  IScmJobService{

    @Autowired
    private IVMsgFilevalidinfoService iVMsgFilevalidinfoService;
    @Autowired
    private IVMsgOrderinfoService iVMsgOrderinfoService;

    @Autowired
    private IVMsgOrderinfomodifyService iVMsgOrderinfomodifyService;
    @Autowired
    private IVMsgMaterinfovalidService iVMsgMaterinfovalidService;
    @Autowired
    private IVMsgQuerypriceinfoService iVMsgQuerypriceinfoService;

    @Autowired
    private IVMsgReportinfoService iVMsgReportinfoService;

    @Autowired
    private UserService userService;

    /*
    获取验收文件有效期的提醒
     */
    @Override
    public  List<VMsgFilevalidinfo> getFileValidMsg()
    {
        List<VMsgFilevalidinfo> listMsg=this.iVMsgFilevalidinfoService.GetMsgFileValid();
        return  listMsg;
    }
    /*
    获取订单消息的提醒
     */
    @Override
   public  List<VMsgOrderinfo> getOrderMsg()
   {
       List<VMsgOrderinfo> listMsg=this.iVMsgOrderinfoService.GetMsgFileValid();
       return  listMsg;
   }

    /*
    获取订单改变的记录
     */
    @Override
    public List<VMsgOrderinfomodify> getOrderModifyMsg(){
        List<VMsgOrderinfomodify> listMsg=this.iVMsgOrderinfomodifyService.GetMsgFileValid();
        return  listMsg;
    }

    /*
    获取供应计划中物料的有效期提醒
     */
    @Override
   public List<VMsgMaterinfovalid> getMaterValidMsg(){
       List<VMsgMaterinfovalid> listMsg=this.iVMsgMaterinfovalidService.GetMsgFileValid();
       return  listMsg;
   }

    /*
       获取需要询价的记录
         */
    @Override
    public  List<VMsgQuerypriceinfo> getQueryPriceMsg(){
        List<VMsgQuerypriceinfo> listMsg=this.iVMsgQuerypriceinfoService.GetMsgFileValid();
        return  listMsg;
    }

    /*
    获取报告记录
     */
    @Override
   public List<VMsgReportinfo> getReportMsg(){
       List<VMsgReportinfo> listMsg=this.iVMsgReportinfoService.GetMsgFileValid();
       return  listMsg;
   }

   @Override
    public List<User> getUserWithoutOpenid(){
       List<User> users=this.userService.findUserWithoutOpenid();
       return  users;
   }
   @Override
    public  void setUserOpenid(List<User> users)
   {
       try {
           for (User user : users
           ) {
               this.userService.updateOpenid2(user.getUsername(),user.getCode());
           }
       }
       catch (Exception ex){

       }
   }
}
