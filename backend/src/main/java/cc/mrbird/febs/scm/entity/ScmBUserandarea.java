package cc.mrbird.febs.scm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * 用户表和院区表配置表
 * </p>
 *
 * @author viki
 * @since 2019-11-13
 */

@Excel("scm_b_userandarea")
public class ScmBUserandarea implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
                    @TableId("ID")
                    @ExcelField(value ="主键")
private String id;
    /**
     * 用户ID
     */
    @TableField("UserID")
            @ExcelField(value ="用户ID")
private Long UserID;
    /**
     * 院区ID
     */
    @TableField("AreaID")
            @ExcelField(value ="院区ID")
private String AreaID;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
private LocalDateTime modifyTime;
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


    public String getId(){
            return id;
            }

        public void setId(String id) {
            this.id = id;
            }

    public Long getUserID(){
            return UserID;
            }

        public void setUserID(Long UserID) {
            this.UserID = UserID;
            }

    public String getAreaID(){
            return AreaID;
            }

        public void setAreaID(String AreaID) {
            this.AreaID = AreaID;
            }

    public LocalDateTime getCreateTime(){
            return createTime;
            }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
            }

    public LocalDateTime getModifyTime(){
            return modifyTime;
            }

        public void setModifyTime(LocalDateTime modifyTime) {
            this.modifyTime = modifyTime;
            }

    public Long getCreateUserId(){
            return createUserId;
            }

        public void setCreateUserId(Long createUserId) {
            this.createUserId = createUserId;
            }

    public Long getModifyUserId(){
            return modifyUserId;
            }

        public void setModifyUserId(Long modifyUserId) {
            this.modifyUserId = modifyUserId;
            }

    public static final String ID ="ID" ;

    public static final String USERID ="UserID" ;

    public static final String AREAID ="AreaID" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

@Override
public String toString() {
        return "ScmBUserandarea{" +
                ", id=" + id +
                ", UserID=" + UserID +
                ", AreaID=" + AreaID +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", createUserId=" + createUserId +
                ", modifyUserId=" + modifyUserId +
        "}";
        }
        }