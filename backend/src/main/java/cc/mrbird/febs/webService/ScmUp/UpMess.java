package cc.mrbird.febs.webService.ScmUp;

import lombok.Data;

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
}
