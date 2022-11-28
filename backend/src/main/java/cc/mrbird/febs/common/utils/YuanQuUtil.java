package cc.mrbird.febs.common.utils;

public class YuanQuUtil {
    public  static  String getMcById(String yqId){
        switch (yqId){
            case "2000":
                return "本院";
            case "2100":
                return "肿瘤";
            case "2200":
                return "西院";
            case "2300":
                return "金银湖";
        }
        return  "";
    }
}
