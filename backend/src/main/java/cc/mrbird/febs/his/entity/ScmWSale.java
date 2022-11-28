package cc.mrbird.febs.his.entity;

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
 * 
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */

@Excel("scm_w_sale")
@Data
public class ScmWSale implements Serializable{

private static final long serialVersionUID=1L;

                                private String id;

    /**
     * 开票编码
     */
    private String code;
    /**
     * 1=草药代煎；2=核医学
     */
            @ExcelField(value ="业务类型",writeConverterExp = "1=草药代煎,2=核医学")
    private String ywlx;

    /**
     * 202210业务所属月份
     */
            @ExcelField(value ="业务所属月份")
    private String ny;

    /**
     * 院区	2000(本院=2000;肿瘤=2100;西院=2200;金银湖=2300)
     */
            @ExcelField(value ="院区")
    private String fyId;

    /**
     * 供应商ID
     */
            @ExcelField(value ="供应商ID")
    private String gysId;

    /**
     * 供应商名称
     */
            @ExcelField(value ="供应商名称")
    private String gysName;

    /**
     * 药品编码
     */
            @ExcelField(value ="药品编码")
    private String ypBh;

    /**
     * 产地代码
     */
            @ExcelField(value ="产地代码")
    private String ypCd;

    /**
     * 药品名称
     */
            @ExcelField(value ="药品名称")
    private String ypMc;

    /**
     * 数量
     */
            @ExcelField(value ="数量")
    private Double sl;

    /**
     * 单位
     */
            @ExcelField(value ="单位")
    private String dw;

    /**
     * 进货金额
     */
            @ExcelField(value ="进货金额")
    private Double jhJe;

    /**
     * 创建人姓名
     */
            @ExcelField(value ="创建人姓名")
    private String cjr;

    /**
     * 创建人工号
     */
            @ExcelField(value ="创建人工号")
    private String cjrid;

    /**
     * 备注
     */
            @ExcelField(value ="备注")
    private String remark;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
    private Integer isDeletemark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
    private Long modifyUserId;



    public static final String ID ="id" ;

    public static final String YWLX ="ywlx" ;

    public static final String NY ="ny" ;

    public static final String FY_ID ="fy_id" ;

    public static final String GYS_ID ="gys_id" ;

    public static final String GYS_NAME ="gys_name" ;

    public static final String YP_BH ="yp_bh" ;

    public static final String YP_CD ="yp_cd" ;

    public static final String YP_MC ="yp_mc" ;

    public static final String SL ="sl" ;

    public static final String DW ="dw" ;

    public static final String JH_JE ="jh_je" ;

    public static final String CJR ="cjr" ;

    public static final String CJRID ="cjrid" ;

    public static final String REMARK ="remark" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }