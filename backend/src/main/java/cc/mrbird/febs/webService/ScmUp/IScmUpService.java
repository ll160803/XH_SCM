package cc.mrbird.febs.webService.ScmUp;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
/**
 * WebService2
 */
@WebService(name = "IScmUpService"
)
public interface IScmUpService {
    @WebMethod
    UpMess UploadFileAndCharge(String userName,String realName, String password, String matnr, String charge, String fileCName,String fileBa64Content);

    @WebMethod
    UpMess UploadFileFp(String userName,String realName, String password, String fphm, String fprq, String fpAmount,String fileName1,String fileBa64Content1,String fileName2,String fileBa64Content2);
}
