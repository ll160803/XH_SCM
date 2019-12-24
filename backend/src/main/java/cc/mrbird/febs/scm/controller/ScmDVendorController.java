package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.entity.ScmDVendorD;
import cc.mrbird.febs.scm.service.IScmDVendorDService;
import cc.mrbird.febs.scm.service.IScmDVendorService;
import cc.mrbird.febs.scm.entity.ScmDVendor;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmDVendor")

public class ScmDVendorController extends BaseController {

    private String message;
    @Autowired
    public IScmDVendorService iScmDVendorService;
    @Autowired
    public IScmDVendorDService iScmDVendorDService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询数据
     *
     * @param bootStrapTable 分页信息
     * @param scmDVendor     查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("scmDVendor:view")
    public Map<String, Object> List(QueryRequest request, ScmDVendor scmDVendor, String keyword) {
        return getDataTable(this.iScmDVendorService.findScmDVendors(request, scmDVendor,keyword));
    }

    /**
     * 跳转添加页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("scmDVendor:add")
    public void addScmDVendor(@Valid ScmDVendor scmDVendor) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmDVendor.setCreateUserId(currentUser.getUserId());
            this.iScmDVendorService.createScmDVendor(scmDVendor);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 跳转修改页面
     *
     * @param request
     * @param id      实体ID
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("scmDVendor:update")
    public void updateScmDVendor(@Valid ScmDVendor scmDVendor) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            scmDVendor.setModifyUserId(currentUser.getUserId());
            this.iScmDVendorService.updateScmDVendor(scmDVendor);
        } catch (Exception e) {
            message = "修改成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("scmDVendor:delete")
    public void deleteScmDVendors(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iScmDVendorService.deleteScmDVendors(arr_ids);
        } catch (Exception e) {
            message = "删除成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("scmDVendor:export")
    public void export(QueryRequest request, ScmDVendor scmDVendor, HttpServletResponse response) throws FebsException {
        try {
            List<ScmDVendor> scmDVendors = this.iScmDVendorService.findScmDVendors(request, scmDVendor,"").getRecords();
            ExcelKit.$Export(ScmDVendor.class, response).downXlsx(scmDVendors, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ScmDVendor detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ScmDVendor scmDVendor = this.iScmDVendorService.getById(id);
        return scmDVendor;
    }

    @PostMapping("regist")
    public void regist(ScmDVendor scmDVendor, String scmDVendorD) throws FebsException {
        try {

            List<ScmDVendorD> list = JSON.parseObject(scmDVendorD, new TypeReference<List<ScmDVendorD>>() {
            });

            this.iScmDVendorService.createScmVendor(scmDVendor, list);
        } catch (Exception e) {
            message = "注册失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("Edit")
    public void edit(ScmDVendor scmDVendor, String scmDVendorD) throws FebsException {
        try {

            List<ScmDVendorD> list = JSON.parseObject(scmDVendorD, new TypeReference<List<ScmDVendorD>>() {
            });

            this.iScmDVendorService.updateScmDVendor(scmDVendor, list);
            //region 编辑用户的姓名
           if(StringUtils.isNotBlank(scmDVendor.getCode())&&StringUtils.isNotBlank(scmDVendor.getName())) {
               this.userService.updateRealname(scmDVendor.getCode(), scmDVendor.getName());
           }
            //endregion
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("保存用户的账号")
    @PostMapping("SaveCode")
    public void saveCodeScmDVendor(@Valid String data) throws FebsException {
        try {
            List<ScmDVendor> list = JSON.parseObject(data, new TypeReference<List<ScmDVendor>>() {
            });
            for (ScmDVendor item : list) {
                if (item.getState() != null) {
                    if (item.getState() == 1) {//保存并审核
                        User user = this.userService.findByName(item.getCode());
                        if (user != null) {
                            user.setStatus("1");
                            this.userService.updateUserByName(user);
                        } else {
                            user = new User();
                            user.setUsername(item.getCode());//供应商编码
                            user.setRealname(item.getName());//供应商名称
                            user.setAvatar("default.jpg");
                            user.setSsex("1");
                            user.setPassword("0000");
                            user.setDeptId(1L);
                            user.setStatus("1");//有效
                            user.setCode(item.getId());//存储供应商的ID
                            if (item.getLb() == 0) {
                                user.setRoleId("3");//角色ID
                                user.setRoleName("药品供应商");
                            } else {
                                user.setRoleId("4");//角色ID
                                user.setRoleName("物资供应商");
                            }
                            this.userService.createUser(user);
                        }
                    }
                    if (item.getState() == 0) {//取消审核
                        User user = this.userService.findByName(item.getCode());

                        user.setStatus("0");//无效

                        this.userService.updateUser(user);
                    }
                }
                this.iScmDVendorService.updateScmDVendor(item);
            }

        } catch (Exception e) {
            message = "保存用户帐号失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /*
    获取当前供应商的信息
     */
    @GetMapping("/GetByVendorCode")
    @RequiresPermissions("VendorUpdate:view")
    public FebsResponse GetByVendorCode() {
        User currentUser = FebsUtil.getCurrentUser();
        String userCode = currentUser.getUsername();
        QueryWrapper<ScmDVendor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ScmDVendor::getCode, userCode);
        ScmDVendor scmDVendor = this.iScmDVendorService.getOne(queryWrapper);
        CustomerVendor cv = new CustomerVendor();
        if (scmDVendor != null) {
            List<ScmDVendorD> scmDVendorDs = this.iScmDVendorDService.findScmDVendorDByBaseId(scmDVendor.getId());
            Collections.sort(scmDVendorDs, Comparator.comparing(ScmDVendorD::getFileIndex));

            cv.scmDVendor = scmDVendor;
            cv.scmDVendorDS = scmDVendorDs;
        }
        return new FebsResponse().data(cv);
    }

    public class CustomerVendor {
        public ScmDVendor scmDVendor;
        public List<ScmDVendorD> scmDVendorDS;
    }
}