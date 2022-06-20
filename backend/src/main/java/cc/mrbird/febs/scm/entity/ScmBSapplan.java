package cc.mrbird.febs.scm.entity;

import java.math.BigDecimal;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * sap供应计划
 * </p>
 *
 * @author viki
 * @since 2022-06-09
 */

@Excel("scm_b_sapplan")
@Data
@Accessors(chain = true)
public class ScmBSapplan implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
                    @TableId(value = "ID" , type = IdType.AUTO)
                    @ExcelField(value ="主键")
    private Long id;

    /**
     * 供应计划号
     */
            @ExcelField(value ="供应计划号")
    private Long planId;

    /**
     * 发票金额
     */
    @TableField("FPJR")
            @ExcelField(value ="发票金额")
    private BigDecimal fpjr;

    /**
     * 发票号码
     */
    @ExcelField(value ="发票号码")
    private String fpHm;

    /**
     * 发票编码
     */

    private String fpBm;

    /**
     * 发票日期
     */
    @TableField("fprq")
    @ExcelField(value = "发票日期", writeConverter = TimeConverter.class)
    private Date fprq;

    /**
     * 调价日期
     */
            @ExcelField(value ="调价日期")
    private Date changDate;
    private transient String changDateFrom;
    private transient String changDateTo;

    /**
     * 开票清单号
     */
            @ExcelField(value ="开票清单号")
    private String code;

    /**
     * 状态
     */
            @ExcelField(value ="状态")
    private Integer state;

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



    public static final String ID ="ID" ;

    public static final String PLAN_ID ="plan_id" ;

    public static final String FPJR ="FPJR" ;

    public static final String CHANG_DATE ="chang_date" ;

    public static final String CODE ="code" ;

    public static final String STATE ="state" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }