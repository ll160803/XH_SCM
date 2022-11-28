package cc.mrbird.febs.his.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * HIS开票清单
 * </p>
 *
 * @author viki
 * @since 2022-10-24
 */

@Excel("scm_w_fpplan")
@Data
@Accessors(chain = true)
public class ScmWFpplan implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
                    @TableId(value = "ID" , type = IdType.AUTO)
                    @ExcelField(value ="主键")
    private Long id;

    /**
     * 编码
     */
    @TableField("CODE")
            @ExcelField(value ="编码")
    private String code;

    /**
     * 名字
     */
    @TableField("NAME")
            @ExcelField(value ="名字")
    private String name;

    /**
     * 发票号码
     */
    @TableField("FPHM")
            @ExcelField(value ="发票号码")
    private String fphm;

    /**
     * 金额
     */
    @TableField("FPJR")
            @ExcelField(value ="金额")
    private BigDecimal fpjr;

    /**
     * 金额
     */
    @TableField("FPJR_REAL")
    @ExcelField(value ="实际金额")
    private BigDecimal fpjrReal;


    /**
     * 发票日期
     */
    @TableField("FPRQ")
            @ExcelField(value ="发票日期")
    private Date fprq;
    private transient String fprqFrom;
    private transient String fprqTo;

    /**
     * 供应商账号
     */
    @TableField("GYSACCOUNT")
            @ExcelField(value ="供应商账号")
    private String gysaccount;

    /**
     * 供应商名称
     */
    @TableField("GYSNAME")
            @ExcelField(value ="供应商名称")
    private String gysname;

    /**
     * 备注
     */
    @TableField("COMMENTS")
            @ExcelField(value ="备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("Status")
            @ExcelField(value ="状态")
    private Integer Status;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
            @ExcelField(value ="是否删除")
    private Integer isDeletemark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
            @ExcelField(value ="创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
            @ExcelField(value ="修改人")
    private Long modifyUserId;

    /**
     * 院区Id
     */
            @ExcelField(value ="院区Id")
    private String werks;

    /**
     * 院区
     */
            @ExcelField(value ="院区")
    private String werkst;

    /**
     * 库房id
     */
            @ExcelField(value ="库房id")
    private String lgort;

    /**
     * 库房名称
     */
    @TableField("lgortName")
            @ExcelField(value ="库房名称")
    private String lgortName;



    public static final String ID ="ID" ;

    public static final String CODE ="CODE" ;

    public static final String NAME ="NAME" ;

    public static final String FPHM ="FPHM" ;

    public static final String FPJR ="FPJR" ;

    public static final String FPRQ ="FPRQ" ;

    public static final String GYSACCOUNT ="GYSACCOUNT" ;

    public static final String GYSNAME ="GYSNAME" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATUS ="Status" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

    public static final String WERKS ="werks" ;

    public static final String WERKST ="werkst" ;

    public static final String LGORT ="lgort" ;

    public static final String LGORTNAME ="lgortName" ;

        }