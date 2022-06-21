package cc.mrbird.febs.common.converter;

import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IfConverter implements WriteConverter {
    @Override
    public String convert(Object value) throws ExcelKitWriteConverterException {
        try {
            if (value == null)
                return "";
            else {
                if(value.equals("1")) return  "否";
                return  "是";
            }
        } catch (Exception e) {
            log.error("是否转换异常", e);
            return "";
        }
    }
}
