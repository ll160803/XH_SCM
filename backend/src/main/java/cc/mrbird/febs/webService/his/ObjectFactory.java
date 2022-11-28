
package cc.mrbird.febs.webService.his;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cc.mrbird.febs.webService.his package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoWebCommonServiceResponse_QNAME = new QName("http://webservice.common.ld.com/", "doWebCommonServiceResponse");
    private final static QName _IsSucResponse_QNAME = new QName("http://webservice.common.ld.com/", "isSucResponse");
    private final static QName _DoWebCommonService_QNAME = new QName("http://webservice.common.ld.com/", "doWebCommonService");
    private final static QName _DoThridServiceResponse_QNAME = new QName("http://webservice.common.ld.com/", "doThridServiceResponse");
    private final static QName _DoThridService_QNAME = new QName("http://webservice.common.ld.com/", "doThridService");
    private final static QName _IsSuc_QNAME = new QName("http://webservice.common.ld.com/", "isSuc");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cc.mrbird.febs.webService.his
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IsSucResponse }
     * 
     */
    public IsSucResponse createIsSucResponse() {
        return new IsSucResponse();
    }

    /**
     * Create an instance of {@link DoWebCommonServiceResponse }
     * 
     */
    public DoWebCommonServiceResponse createDoWebCommonServiceResponse() {
        return new DoWebCommonServiceResponse();
    }

    /**
     * Create an instance of {@link DoThridService }
     * 
     */
    public DoThridService createDoThridService() {
        return new DoThridService();
    }

    /**
     * Create an instance of {@link IsSuc }
     * 
     */
    public IsSuc createIsSuc() {
        return new IsSuc();
    }

    /**
     * Create an instance of {@link DoThridServiceResponse }
     * 
     */
    public DoThridServiceResponse createDoThridServiceResponse() {
        return new DoThridServiceResponse();
    }

    /**
     * Create an instance of {@link DoWebCommonService }
     * 
     */
    public DoWebCommonService createDoWebCommonService() {
        return new DoWebCommonService();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoWebCommonServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "doWebCommonServiceResponse")
    public JAXBElement<DoWebCommonServiceResponse> createDoWebCommonServiceResponse(DoWebCommonServiceResponse value) {
        return new JAXBElement<DoWebCommonServiceResponse>(_DoWebCommonServiceResponse_QNAME, DoWebCommonServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsSucResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "isSucResponse")
    public JAXBElement<IsSucResponse> createIsSucResponse(IsSucResponse value) {
        return new JAXBElement<IsSucResponse>(_IsSucResponse_QNAME, IsSucResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoWebCommonService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "doWebCommonService")
    public JAXBElement<DoWebCommonService> createDoWebCommonService(DoWebCommonService value) {
        return new JAXBElement<DoWebCommonService>(_DoWebCommonService_QNAME, DoWebCommonService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoThridServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "doThridServiceResponse")
    public JAXBElement<DoThridServiceResponse> createDoThridServiceResponse(DoThridServiceResponse value) {
        return new JAXBElement<DoThridServiceResponse>(_DoThridServiceResponse_QNAME, DoThridServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoThridService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "doThridService")
    public JAXBElement<DoThridService> createDoThridService(DoThridService value) {
        return new JAXBElement<DoThridService>(_DoThridService_QNAME, DoThridService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsSuc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.ld.com/", name = "isSuc")
    public JAXBElement<IsSuc> createIsSuc(IsSuc value) {
        return new JAXBElement<IsSuc>(_IsSuc_QNAME, IsSuc.class, null, value);
    }

}
