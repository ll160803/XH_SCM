package  cc.mrbird.febs.webService.SapToScm;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * WebService2
 */
@WebService(name = "ISAPtoSCMService"
)
public interface  ISAPtoSCMService {
    @WebMethod
    Boolean GetPucharseFromSap(List<Sap_PurchasePlan> purcharseList, String Flag);
    @WebMethod
    String HelloWorld();
    @WebMethod
    Boolean GetMaterFromSap(List<SAP_MATER> materList, String Flag);
    @WebMethod
    Boolean ChangeStausFromSap(List<String> codeList, String status);
    @WebMethod
    Boolean GetHrpMaterFromSap(List<HRP_MATER> materList, String Flag);

    @WebMethod
    Boolean ChangeMaterCodeFromSap(List<String> codeList, String time);

    @WebMethod
    Boolean ChangeFpState(String fphm,String lifnr,int state);

    @WebMethod
    Boolean ChangeSupplanTime(String id,String time);

    @WebMethod
    Boolean GetPlanFormSap(String planid,String fpjr,String changeDate);

    @WebMethod
    Boolean ChangeTJandCYState(String gysAccount,String fphm,String type,int state);
}