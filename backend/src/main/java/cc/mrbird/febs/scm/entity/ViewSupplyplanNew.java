package cc.mrbird.febs.scm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */

@Excel("view_supplyplan_new")
@Data
@Accessors(chain = true)
public class ViewSupplyplanNew   implements Serializable{

private static final long serialVersionUID=1L;

            private String ebeln;

            private String ebelp;

            private String coder;

            private String sendDeaprtContact;

            private String matnr;

            private String txz01;

            private String werkst;

            private String werks;

            private String lgort;

    @TableField("lgortName")
            private String lgortName;

            private BigDecimal menge;

            private String meins;

            private String mseht;

            private BigDecimal netpr;

            private Date eindt;
    private transient String eindtFrom;
    private transient String eindtTo;

            private Date bedat;
    private transient String bedatFrom;
    private transient String bedatTo;

            private String bsart;

    @TableField("G_MENGE")
            private BigDecimal gMenge;

    public BigDecimal getgMenge(){
        return gMenge;
    }

    public void setgMenge(BigDecimal gMenge) {
        this.gMenge = gMenge;
    }

    @TableField("CHARGE")
            private String charge;

    @TableField("VFDAT")
            private Date vfdat;
    private transient String vfdatFrom;
    private transient String vfdatTo;

    @TableField("HSDAT")
            private Date hsdat;
    private transient String hsdatFrom;
    private transient String hsdatTo;

    @TableField("FPHM")
            private String fphm;

    @TableField("FPJR")
            private BigDecimal fpjr;

    @TableField("FPRQ")
            private Date fprq;
    private transient String fprqFrom;
    private transient String fprqTo;

    @TableField("BASE_ID")
            private String baseId;

    @TableField("ID")
            private Long id;


    private  transient  String id2;
    public String getId2(){
        if(this.changDate!=null){
            return  String.valueOf(id)+"*";
        }
        return  String.valueOf(id);
    }

    @TableField("STATUS")
            private Integer status;

    @TableField("DONEMENGE")
            private BigDecimal donemenge;

    @TableField("FPBM")
            private String fpbm;

    @TableField("GYSACCOUNT")
            private String gysaccount;

    @TableField("GYSNAME")
            private String gysname;

    @TableField("PKG_AMOUNT")
            private BigDecimal pkgAmount;

    @TableField("PKG_NUMBER")
            private BigDecimal pkgNumber;

    @TableField("UNINFORMED_STATE")
            private String uninformedState;

    @TableField("INFORMED_STATE")
            private String informedState;

    @TableField("BSART_D")
            private String bsartD;

    @TableField("LINK_PERSON")
            private String linkPerson;

    @TableField("SEND_DEPART")
            private String sendDepart;

    @TableField("LINK_TELEPHONE")
            private String linkTelephone;

    @TableField("SEND_ORDER_CODE")
            private String sendOrderCode;

    @TableField("CODE")
            private String code;

    @TableField("MATER_CODE")
            private String materCode;

    @TableField("OUT_CAUSE")
            private String outCause;

    @TableField("OUT_DATE")
            private Date outDate;
    private transient String outDateFrom;
    private transient String outDateTo;

    @TableField("IS_DELETEMARK")
            private Integer isDeletemark;

    @TableField("CREATE_TIME")
            private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    @TableField("MODIFY_TIME")
            private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    @TableField("CREATE_USER_ID")
            private Long createUserId;

    @TableField("MODIFY_USER_ID")
            private Long modifyUserId;

    @TableField("NAME")
            private String name;

            private Date changDate;
    private transient String changDateFrom;
    private transient String changDateTo;


    private transient String materCodeFrom;




    private transient String materCodeTo;

    public static final String EBELN ="ebeln" ;

    public static final String EBELP ="ebelp" ;

    public static final String CODER ="coder" ;

    public static final String SEND_DEAPRT_CONTACT ="send_deaprt_contact" ;

    public static final String MATNR ="matnr" ;

    public static final String TXZ01 ="txz01" ;

    public static final String WERKST ="werkst" ;

    public static final String WERKS ="werks" ;

    public static final String LGORT ="lgort" ;

    public static final String LGORTNAME ="lgortName" ;

    public static final String MENGE ="menge" ;

    public static final String MEINS ="meins" ;

    public static final String MSEHT ="mseht" ;

    public static final String NETPR ="netpr" ;

    public static final String EINDT ="eindt" ;

    public static final String BEDAT ="bedat" ;

    public static final String BSART ="bsart" ;

    public static final String G_MENGE ="G_MENGE" ;

    public static final String CHARGE ="CHARGE" ;

    public static final String VFDAT ="VFDAT" ;

    public static final String HSDAT ="HSDAT" ;

    public static final String FPHM ="FPHM" ;

    public static final String FPJR ="FPJR" ;

    public static final String FPRQ ="FPRQ" ;

    public static final String BASE_ID ="BASE_ID" ;

    public static final String ID ="ID" ;

    public static final String STATUS ="STATUS" ;

    public static final String DONEMENGE ="DONEMENGE" ;

    public static final String FPBM ="FPBM" ;

    public static final String GYSACCOUNT ="GYSACCOUNT" ;

    public static final String GYSNAME ="GYSNAME" ;

    public static final String PKG_AMOUNT ="PKG_AMOUNT" ;

    public static final String PKG_NUMBER ="PKG_NUMBER" ;

    public static final String UNINFORMED_STATE ="UNINFORMED_STATE" ;

    public static final String INFORMED_STATE ="INFORMED_STATE" ;

    public static final String BSART_D ="BSART_D" ;

    public static final String LINK_PERSON ="LINK_PERSON" ;

    public static final String SEND_DEPART ="SEND_DEPART" ;

    public static final String LINK_TELEPHONE ="LINK_TELEPHONE" ;

    public static final String SEND_ORDER_CODE ="SEND_ORDER_CODE" ;

    public static final String CODE ="CODE" ;

    public static final String MATER_CODE ="MATER_CODE" ;

    public static final String OUT_CAUSE ="OUT_CAUSE" ;

    public static final String OUT_DATE ="OUT_DATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

    public static final String NAME ="NAME" ;

    public static final String CHANG_DATE ="chang_date" ;

        }