package cc.mrbird.febs.webService.ScmToGys;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PlanDetail {


    /// <summary>
    /// 采购凭证号
    /// </summary>
    private String EBELN ;
    /// <summary>
    /// 采购凭证项目编号
    /// </summary>
    private String GYJH ;
    /// <summary>
    /// 供应商账户
    /// </summary>
    private String GYSACCOUNT ;
    /// <summary>
    /// 供应商表述
    /// </summary>
    private String GYSNAME ;
    /// <summary>
    /// 物料号
    /// </summary>
    private String MATNR ;
    /// <summary>
    /// 物料描述
    /// </summary>
    private String TXZ01 ;
    /// <summary>
    /// 院区
    /// </summary>
    private String WERKS ;
    /// <summary>
    /// 库存地点
    /// </summary>
    private String LGORT ;

    /// <summary>
    /// 计划数量
    /// </summary>
    private BigDecimal MENGE ;

    /// <summary>
    /// 计量单位
    /// </summary>
    private String MEINS ;
    /// <summary>
    /// 竞价
    /// </summary>
    private String NETPR ;

    /// <summary>
    /// 批次
    /// </summary>
    private String CHARG ;

    /// <summary>
    /// 生产日期
    /// </summary>
    private Date HSDAT ;

    /// <summary>
    /// 计量单位描述
    /// </summary>
    private String MSEHT ;

    /// <summary>
    /// 工厂描述
    /// </summary>
    private String WERKST ;

    /// <summary>
    /// 供应计划号
    /// </summary>
    private String CODE ;
    /// <summary>
    /// 有效日期
    /// </summary>
    private Date VFDAT ;

    /// <summary>
    /// 发票号码
    /// </summary>
    private String FPHM ;

    /// <summary>
    /// 发票金额
    /// </summary>
    private BigDecimal FPJR ;
    /// <summary>
    /// 发票日期
    /// </summary>
    private Date FPRQ ;

    /// <summary>
    /// 备注
    /// </summary>
    private String COMMENTS ;
    /// <summary>
    /// 发票编码
    /// </summary>
    private String FPBM ;


    /// <summary>
    ///  交货日期       
    /// </summary>
    private Date EEIND ;

    /// <summary>
    /// 凭证日期
    /// </summary>
    private Date BEDAT ;

    /// <summary>
    /// 包装数量
    /// </summary>
    private BigDecimal PKG_AMOUNT ;

    /// <summary>
    /// 箱数
    /// </summary>
    private BigDecimal PKG_NUMBER ;

    /// <summary>
    /// 采购订单数量
    /// </summary>
    private BigDecimal ORDER_MENGE ;
    /// <summary>
    /// 缺货原因
    /// </summary>
    private String OUT_CAUSE ;
    /// <summary>
    /// 下次补货日期
    /// </summary>
    private Date OUT_DATE ;
    /// <summary>
    /// 标志 
    /// </summary>
    private String FLAG ;

    private String ID ;
    /**
     * 备注
     */
    private String NOTE;
}
