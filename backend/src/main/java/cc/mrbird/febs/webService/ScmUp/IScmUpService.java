package cc.mrbird.febs.webService.ScmUp;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
/**
 * WebService2
 */
@WebService(name = "IScmUpService"
)
public interface IScmUpService {
    /**
     *
     * @param userName 供应商账号
     * @param realName 供应商名称
     * @param password 密码
     * @param matnr  药品编号
     * @param charge 批次号
     * @param fileCName 验收报告名称
     * @param fileBa64Content 验收报告附件Base64
     * @param fileName1 厂家文件名称
     * @param fileBa64Content1 厂家附件BASE64编码
     * @return
     */
    @WebMethod
    UpMess UploadFileAndCharge(@WebParam(name="userName")String userName, @WebParam(name="realName")String realName, @WebParam(name="password")String password, @WebParam(name="matnr")String matnr, @WebParam(name="charge")String charge, @WebParam(name="fileCName")String fileCName, @WebParam(name="fileBa64Content")String fileBa64Content, @WebParam(name="fileName1")String fileName1, @WebParam(name="fileBa64Content1")String fileBa64Content1);

    /**
     *
     * @param userName 供应商账号
     * @param realName 供应商名称
     * @param password 密码
     * @param fphm 发票号码
     * @param fprq 发票日期
     * @param fpAmount 发票金额
     * @param fileName1  厂家文件名称
     * @param fileBa64Content1  厂家附件的Base64编码
     * @param fileName2 供应商发票名称
     * @param fileBa64Content2 供应商发票附件Base64编码
     * @return
     */
    @WebMethod
    UpMess UploadFileFp(@WebParam(name="userName")String userName,@WebParam(name="realName")String realName, @WebParam(name="password")String password, @WebParam(name="fphm")String fphm, @WebParam(name="fprq")String fprq, @WebParam(name="fpAmount")String fpAmount,@WebParam(name="fileName1")String fileName1,@WebParam(name="fileBa64Content1")String fileBa64Content1,@WebParam(name="fileName2")String fileName2,@WebParam(name="fileBa64Content2")String fileBa64Content2);


    /**@WebParam(name="userName")
     *
     * @param userName 供应商账号
     * @param realName 供应商名称
     * @param password 密码
     * @param fphm 发票号码
     * @param fprq 发票日期
     * @param fpAmount 发票金额
     * @param fileName2 发票名称
     * @param fileBa64Content2 发票附件的Base64编码
     * @param fpBm 开票清单号
     * @return
     */
    @WebMethod
    UpMess UploadFileFpList(@WebParam(name="userName")String userName,@WebParam(name="realName")String realName, @WebParam(name="password")String password, @WebParam(name="fphm")String fphm, @WebParam(name="fprq")String fprq, @WebParam(name="fpAmount")String fpAmount,@WebParam(name="fileName2")String fileName2,@WebParam(name="fileBa64Content2")String fileBa64Content2,@WebParam(name="fpBm")String fpBm);

    @WebMethod
    String hello();
}
