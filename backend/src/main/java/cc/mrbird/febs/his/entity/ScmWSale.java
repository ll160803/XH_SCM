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
     * ��Ʊ����
     */
    private String code;
    /**
     * 1=��ҩ���壻2=��ҽѧ
     */
            @ExcelField(value ="ҵ������",writeConverterExp = "1=��ҩ����,2=��ҽѧ")
    private String ywlx;

    /**
     * 202210ҵ�������·�
     */
            @ExcelField(value ="ҵ�������·�")
    private String ny;

    /**
     * Ժ��	2000(��Ժ=2000;����=2100;��Ժ=2200;������=2300)
     */
            @ExcelField(value ="Ժ��")
    private String fyId;

    /**
     * ��Ӧ��ID
     */
            @ExcelField(value ="��Ӧ��ID")
    private String gysId;

    /**
     * ��Ӧ������
     */
            @ExcelField(value ="��Ӧ������")
    private String gysName;

    /**
     * ҩƷ����
     */
            @ExcelField(value ="ҩƷ����")
    private String ypBh;

    /**
     * ���ش���
     */
            @ExcelField(value ="���ش���")
    private String ypCd;

    /**
     * ҩƷ����
     */
            @ExcelField(value ="ҩƷ����")
    private String ypMc;

    /**
     * ����
     */
            @ExcelField(value ="����")
    private Double sl;

    /**
     * ��λ
     */
            @ExcelField(value ="��λ")
    private String dw;

    /**
     * �������
     */
            @ExcelField(value ="�������")
    private Double jhJe;

    /**
     * ����������
     */
            @ExcelField(value ="����������")
    private String cjr;

    /**
     * �����˹���
     */
            @ExcelField(value ="�����˹���")
    private String cjrid;

    /**
     * ��ע
     */
            @ExcelField(value ="��ע")
    private String remark;

    /**
     * ״̬
     */
    @TableField("state")
    private Integer state;

    /**
     * �Ƿ�ɾ��
     */
    @TableField("IS_DELETEMARK")
    private Integer isDeletemark;

    /**
     * ����ʱ��
     */
    @TableField("CREATE_TIME")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * �޸�ʱ��
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * ������
     */
    @TableField("CREATE_USER_ID")
    private Long createUserId;

    /**
     * �޸���
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