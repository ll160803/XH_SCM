package cc.mrbird.febs.scm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * <p>
 * 资质文件记录表
 * </p>
 *
 * @author viki
 * @since 2019-11-21
 */
@Data
@Excel("批次表")
public class ScmBGysMaterPic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("ID")
    private String id;
    /**
     * 编码
     */
    @TableField("CODE")

    private String code;
    /**
     * 姓名
     */
    @TableField("NAME")

    private String name;
    /**
     * 供应商账号
     */
    @TableField("GYSACCOUNT")

    private String gysaccount;
    /**
     * 药品编码ID
     */
    @TableField("MATER_ID")
    private String materId;

    /**
     * 药品编码
     */
    @TableField("MATNR")
    @ExcelField(value = "药品编码")
    private String matnr;
    /**
     * 批次号
     */
    @TableField("CHARGE")
    @ExcelField(value = "批次号")
    private String charge;
    /**
     * 备注
     */
    @TableField("COMMENTS")
    private String comments;
    /**
     * 状态
     */
    @TableField("STATE")
    private Integer state;
    /**
     * 附件ID
     */
    @TableField("FILE_ID")
    private String fileId;

    /**
     * 院区
     */
    @TableField("werks")
    private String werks;

    /**
     * 库房
     */
    @TableField("lgort")
    private String lgort;

    /**
     * 院区
     */
    @TableField("werkt")
    private String werkt;

    /**
     * 库房
     */
    @TableField("lgortName")
    private String lgortName;
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

    /**
     * 单位描述
     */
    @TableField("MSEHT")
    private String mseht;
    /**
     * 物料描述
     */
    @TableField("TXZ01")
    @ExcelField(value = "药品名称")
    private String txz01;
    /**
     * 生产厂家
     */
    @TableField("PRODUCE_AREA")
    private String produceArea;
    /**
     * 规格
     */
    @TableField("SPEC")
    private String spec;

    @TableField("AUDITCAUSE")
    private  String auditCause;

    private  transient  String userid;


    /**
     * 拼音码
     */
    @TableField("SPELL_CODE")
    private String spellCode;

    public static final String ID = "ID";

    public static final String CODE = "CODE";

    public static final String NAME = "NAME";

    public static final String GYSACCOUNT = "GYSACCOUNT";

    public static final String MATER_ID = "MATER_ID";

    public static final String CHARGE = "CHARGE";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String FILE_ID = "FILE_ID";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    @Override
    public String toString() {
        return "ScmBGysMaterPic{" +
                ", id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", gysaccount=" + gysaccount +
                ", materId=" + materId +
                ", charge=" + charge +
                ", comments=" + comments +
                ", state=" + state +
                ", fileId=" + fileId +
                ", isDeletemark=" + isDeletemark +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", createUserId=" + createUserId +
                ", modifyUserId=" + modifyUserId +
                "}";
    }
}