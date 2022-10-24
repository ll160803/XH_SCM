package cc.mrbird.febs.webService.ScmUp;

import cc.mrbird.febs.scm.entity.ViewSupplyplan;
import lombok.Data;

import java.util.List;

@Data
public class UpMess {
    /**
     * 返回消息
     */
    private String mess;
    /**
     * 是否成功
     */
    private Boolean isSuccess;

    /**
     * 返回供应计划
     */
    private List<ViewSupplyplan> plans ;
}
