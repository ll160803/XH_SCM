package cc.mrbird.febs.scm.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

@Data
@Excel("采购")
public class PlanOrder {
    /**
     * 订单号
     */
    @ExcelField(value ="订单号")
    private String ebeln;
    /**
     * 项目号
     */
    @ExcelField(value ="项目号")
    private String ebelp;


    @ExcelField(value ="是否集采")
    private String sendDeaprtContact;
}
