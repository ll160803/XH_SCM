package cc.mrbird.febs.scm.entity;

import java.time.LocalDate;
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
 * @since 2022-04-07
 */

@Excel("scm_d_control")
@Data
@Accessors(chain = true)
public class ScmDControl implements Serializable{

private static final long serialVersionUID=1L;

                                private Integer id;

    /**
     * 结束时间
     */
            @ExcelField(value ="结束时间")
    private Date endTime;
    private transient String endTimeFrom;
    private transient String endTimeTo;



    public static final String ID ="id" ;

    public static final String END_TIME ="end_time" ;

        }