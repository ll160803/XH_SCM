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
}