package cc.mrbird.febs.webService.SapToScm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sap_PurchasePlan {
    /// <summary>
    /// 采购凭证号
    /// </summary>
    public String EBELN ;
    /// <summary>
    /// 采购凭证项目编号
    /// </summary>
    public String EBELP ;
    /// <summary>
    /// 供应商账户
    /// </summary>
    public String LIFNR ;
    /// <summary>
    /// 库存地点描述
    /// </summary>
    public String NAME ;
    /// <summary>
    /// 物料号
    /// </summary>
    public String MATNR ;
    /// <summary>
    /// 物料描述
    /// </summary>
    public String TXZ01 ;
    /// <summary>
    /// 院区
    /// </summary>
    public String WERKS ;
    /// <summary>
    /// 库存地点
    /// </summary>
    public String LGORT ;

    /// <summary>
    /// 数量
    /// </summary>
    public BigDecimal MENGE;

    /// <summary>
    /// 计量单位
    /// </summary>
    public String MEINS ;
    /// <summary>
    /// 竞价
    /// </summary>
    public String NETPR ;

    /// <summary>
    /// 交货日期
    /// </summary>
    public String EINDT ;

    /// <summary>
    /// 采购订单日期
    /// </summary>
    public String BEDAT ;

    /// <summary>
    /// 计量单位描述
    /// </summary>
    public String MSEHT ;

    /// <summary>
    /// 工厂描述
    /// </summary>
    public String WERKST ;

    /// <summary>
    /// 采购类型
    /// </summary>
    public String BSART ;

    /// <summary>
    /// 送货科室编码
    /// </summary>
    public String KOSTL ;

    /// <summary>
    /// L是删除 
    /// </summary>
    public String LOEKZ ;

    /// <summary>
    /// 订单备注
    /// </summary>
    public String REMARK ;
    /// <summary>
    /// 联系人
    /// </summary>
    public String CONTACT ;
    /// <summary>
    /// 联系电话
    /// </summary>
    public String PHONE ;
    /// <summary>
    /// 备注
    /// </summary>
    public String COMMENTS ;
}
