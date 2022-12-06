package cc.mrbird.febs.common.generator;

import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;

public class TestMain {
    public static void main(String[] args) {
      String month=  DateUtil.format(DateUtil.offsetDay(DateUtil.date(),-1),"yyyyMM");

        BigDecimal a= new BigDecimal("345.667").setScale(2,BigDecimal.ROUND_HALF_UP);
      System.out.println(a.toString());
    }
}
