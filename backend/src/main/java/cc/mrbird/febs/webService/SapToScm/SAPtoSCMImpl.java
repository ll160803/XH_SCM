package cc.mrbird.febs.webService.SapToScm;

import cc.mrbird.febs.scm.dao.ScmBPurcharseorderMapper;
import cc.mrbird.febs.scm.dao.ScmDSenddepartMapper;
import cc.mrbird.febs.scm.entity.ScmBPurcharseorder;
import cc.mrbird.febs.scm.entity.ScmDSenddepart;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@WebService(serviceName = "SAPtoSCMService",
        endpointInterface = "cc.mrbird.febs.webService.SapToScm.ISAPtoSCMService"// 接口地址
)
public class SAPtoSCMImpl implements ISAPtoSCMService {

    @Autowired
    private ScmDSenddepartMapper scmDSenddepartMapper;
    @Autowired
    private ScmBPurcharseorderMapper scmBPurcharseorderMapper;

    @Override
    public Boolean GetPucharseFromSap(List<Sap_PurchasePlan> purcharseList, String Flag) {
        //log.info("从SAP获取订单开始");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<ScmDSenddepart> listDepart = this.scmDSenddepartMapper.selectList(null);
        if (purcharseList.size() > 0) {
            //log.info("从SAP更新订单开始");

            List<ScmBPurcharseorder> list_Update = new ArrayList<>();
            List<ScmBPurcharseorder> list_Delete = new ArrayList<>();
            List<ScmBPurcharseorder> list_purchase_C = new ArrayList<>();
            for (Sap_PurchasePlan item : purcharseList) {
                LambdaQueryWrapper<ScmBPurcharseorder> queryWrapperOrder = new LambdaQueryWrapper<>();
                queryWrapperOrder.eq(ScmBPurcharseorder::getEbelp, item.getEBELP());
                queryWrapperOrder.eq(ScmBPurcharseorder::getEbeln, item.getEBELN());
                ScmBPurcharseorder order = scmBPurcharseorderMapper.selectOne(queryWrapperOrder);
                Flag = (order == null ? "C" : "U");

                // LogHelper.Info("KOSTL:"+item.KOSTL);
                if (Flag == "U") {

                    if (StringUtils.equals(item.getKOSTL(), "L")) {
                        if (order != null) {
                            list_Delete.add(order);
                        }
                    } else {
                        if (order != null) {
                            order.setEbeln(item.getEBELN());
                            try {
                                order.setBedat(sdf.parse(item.getBEDAT()));
                            } catch (Exception ex) {
                            }
                            order.setEbelp(item.getEBELP());
                            try {
                                order.setEindt(sdf.parse(item.getEINDT()));
                            } catch (Exception ex) {
                            }
                            order.setLgort(item.getLGORT());
                            order.setLifnr(item.getLIFNR());
                            order.setMatnr(item.getMATNR());
                            order.setMenge(item.getMENGE());
                            order.setMseht(item.getMSEHT());
                            order.setName(item.getNAME());
                            order.setStatus(1);
                            order.setNetpr(new BigDecimal(item.getNETPR().replace(",", "")));
                            order.setWerks(item.getWERKS());
                            order.setWerkst(item.getWERKST());
                            order.setTxz01(item.getTXZ01());
                            order.setMeins(item.getMEINS());
                            order.setBsart(item.getBSART() == "Z004" ? "0" : "1");

                            order.setSendDeaprtContact(item.getCONTACT());
                            order.setSendDeaprtPhone(item.getPHONE());
                            order.setComments(item.getCOMMENTS());

                            if (StringUtils.equals(item.getBSART(), "Z004")) {
                                order.setSendDeaprtName(item.getREMARK());//西院的药品订单 接收科室放在订单备注里
                            } else {
                                if (StringUtils.isNotBlank(item.getKOSTL()))//采购订单含有送货科室编码
                                {
                                    List<ScmDSenddepart> departs = listDepart.stream().filter(p -> p.CODE == item.getKOSTL()).collect(Collectors.toList());
                                    if (departs != null) {
                                        order.setSendDeaprtId(departs.get(0).getCode());
                                        order.setSendDeaprtName(departs.get(0).getName());
                                    }
                                }
                            }
                            list_Update.add(order);

                        }
                    }
                } else if (Flag == "C") {
                    ScmBPurcharseorder entity = new ScmBPurcharseorder();

                    entity.setEbeln(item.getEBELN());
                    try {
                        entity.setBedat(sdf.parse(item.getBEDAT()));
                    } catch (Exception ex) {
                    }
                    entity.setEbelp(item.getEBELP());
                    try {
                        entity.setEindt(sdf.parse(item.getEINDT()));
                    } catch (Exception ex) {
                    }
                    entity.setLgort(item.getLGORT());
                    entity.setLifnr(item.getLIFNR());
                    entity.setMatnr(item.getMATNR());
                    entity.setMenge(item.getMENGE());
                    entity.setMseht(item.getMSEHT());
                    entity.setName(item.getNAME());
                    entity.setStatus(1);
                    entity.setNetpr(new BigDecimal(item.getNETPR().replace(",", "")));
                    entity.setWerks(item.getWERKS());
                    entity.setWerkst(item.getWERKST());
                    entity.setTxz01(item.getTXZ01());
                    entity.setMeins(item.getMEINS());
                    entity.setBsart(item.getBSART() == "Z004" ? "0" : "1");

                    entity.setSendDeaprtContact(item.getCONTACT());
                    entity.setSendDeaprtPhone(item.getPHONE());
                    entity.setComments(item.getCOMMENTS());
                    entity.setId(UUID.randomUUID().toString());

                    if (StringUtils.equals(item.getBSART(), "Z004")) {
                        entity.setSendDeaprtName(item.getREMARK());//西院的药品订单 接收科室放在订单备注里
                    } else {
                        if (StringUtils.isNotBlank(item.getKOSTL()))//采购订单含有送货科室编码
                        {
                            List<ScmDSenddepart> departs = listDepart.stream().filter(p -> p.CODE == item.getKOSTL()).collect(Collectors.toList());
                            if (departs != null) {
                                order.setSendDeaprtId(departs.get(0).getCode());
                                order.setSendDeaprtName(departs.get(0).getName());
                            }
                        }
                    }


                    list_purchase_C.add(entity);
                }

            }

            if (list_purchase_C.size() > 0) {
                try {
                    for (ScmBPurcharseorder itemOrder : list_purchase_C) {
                        this.scmBPurcharseorderMapper.insert(itemOrder);
                    }
                    //return msg.Succeed;
                } catch (Exception ex) {
                    //log.info("新增采购订单报错");
                }
            }
            if (list_Update.size() > 0) {
                for (ScmBPurcharseorder itemOrder : list_Update) {
                    this.scmBPurcharseorderMapper.updateScmBPurcharseorder(itemOrder);
                }
            }

            if (list_Delete.size() > 0) {
                for (ScmBPurcharseorder deleteOrder : list_Delete) {
                    this.scmBPurcharseorderMapper.deleteById(deleteOrder.getId());
                }
            }
            return true;

        }
        return false;
    }
}
