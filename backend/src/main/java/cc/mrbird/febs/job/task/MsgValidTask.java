package cc.mrbird.febs.job.task;

import cc.mrbird.febs.common.utils.WxMessage;
import cc.mrbird.febs.scm.entity.VMsgOrderinfo;
import cc.mrbird.febs.scm.entity.VMsgFilevalidinfo;
import cc.mrbird.febs.scm.entity.VMsgMaterinfovalid;
import cc.mrbird.febs.scm.entity.VMsgOrderinfomodify;
import cc.mrbird.febs.scm.entity.VMsgQuerypriceinfo;
import cc.mrbird.febs.scm.entity.VMsgReportinfo;
import cc.mrbird.febs.scm.service.*;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MsgValidTask {
    @Autowired
    public IVMsgFilevalidinfoService iVMsgFilevalidinfoService;
    @Autowired
    public IVMsgMaterinfovalidService iVMsgMaterinfovalidService;
    @Autowired
    public IVMsgOrderinfoService iVMsgOrderinfoService;
    @Autowired
    public IVMsgOrderinfomodifyService iVMsgOrderinfomodifyService;
    @Autowired
    public IVMsgQuerypriceinfoService iVMsgQuerypriceinfoService;
    @Autowired
    public IVMsgReportinfoService iVMsgReportinfoService;
    public void runFileValid(String openids){
        List<VMsgFilevalidinfo> listMsg=this.iVMsgFilevalidinfoService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        String[] open_ids = openids.split(StringPool.COMMA);
        for (VMsgFilevalidinfo msg:listMsg
             ) {
            if(StringUtils.isNotBlank(msg.getVxcode())) {
                String mg = "供应商" + msg.getCode() + msg.getName() + msg.getFilename() + "将于" + msg.getDays() + "天后过期，请知悉！";
                wm.send(msg.getVxcode(), "资质效期", mg, "action=zzxq&id=" + msg.getCode());//供应商部分
                for (String openid ://医院部分的信息
                        open_ids) {
                    log.info("openid："+openid+",资质效期");
                    wm.send(openid, "资质效期", mg, "action=zzxq&id=" + msg.getCode());
                }
            }
        }
    }
    public  void runOrderInfo(){
        List<VMsgOrderinfo> listMsg=this.iVMsgOrderinfoService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        for (VMsgOrderinfo msg:listMsg
        ) {
            if(StringUtils.isNotBlank(msg.getVxcode())) {
                String mg = "您收到来自" + msg.getWerkst()+ "的" + msg.getNumCount() + "个药品采购项目，请及时查看！";
                log.info("openid："+msg.getVxcode()+",订单信息");
                wm.send(msg.getVxcode(), "订单信息", mg, "action=zzxq&id=" + msg.getCode());//供应商部分
            }
        }
    }
    public  void runOrderInfoModify(){
        List<VMsgOrderinfomodify> listMsg=this.iVMsgOrderinfomodifyService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        for (VMsgOrderinfomodify msg:listMsg
        ) {
            if(StringUtils.isNotBlank(msg.getVxcode())) {
                String mg = "您收到来自" + msg.getWerkst()+ "的" + msg.getNumCount() + "个采购订单变更信息，请知悉！";
                log.info("openid："+msg.getVxcode()+",订单信息更改");
                wm.send(msg.getVxcode(), "采购订单", mg, "action=订单&id=" + msg.getCode());//供应商部分
            }
        }
    }
    public  void runMaterInfoValid(String openids){
        List<VMsgMaterinfovalid> listMsg=this.iVMsgMaterinfovalidService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        String[] open_ids = openids.split(StringPool.COMMA);
        for (VMsgMaterinfovalid msg:listMsg
        ) {
            String mg="由供应商"+msg.getLifnr()+msg.getGysName()+"提供的批次为"+msg.getCharge()+"的"+msg.getMatnr()+msg.getTxz01()+"将于"+msg.getDays()+"天后过期，请知悉！";
                for (String openid ://医院部分的信息
                        open_ids) {
                    log.info("openid："+openid+",药品效期");
                    wm.send(openid, "药品效期", mg, "action=zzxq&id=" + msg.getLifnr());
                }

        }
    }
    public  void runQueryPriceInfo(){
        List<VMsgQuerypriceinfo> listMsg=this.iVMsgQuerypriceinfoService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        for (VMsgQuerypriceinfo msg:listMsg
        ) {
            if(StringUtils.isNotBlank(msg.getVxCode())) {
                String mg = "您有一条来自于武汉协和医院的"+msg.getTxz01()+"的询价信息，请知悉！";
                log.info("openid："+msg.getVxCode()+",询价请求");
                wm.send(msg.getVxCode(), "询价请求", mg, "action=xunjia&id=" + msg.getGysaccount());//供应商部分
            }
        }
    }
    public  void runReportInfo(){
        List<VMsgReportinfo> listMsg=this.iVMsgReportinfoService.GetMsgFileValid();
        WxMessage wm=new WxMessage();
        for (VMsgReportinfo msg:listMsg
        ) {
            if(StringUtils.isNotBlank(msg.getVxCode())) {
                String mg = "您有一条来自于武汉协和医院通知信息，请知悉！";
                log.info("openid："+msg.getVxCode()+",消息通知");
                wm.send(msg.getVxCode(), "消息通知", mg, "action=zzxq&id=" + msg.getCode());//供应商部分
            }
        }
    }
}
