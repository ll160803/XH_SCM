package cc.mrbird.febs.webService.ScmUp;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;

@Configuration
public class UpfConfig {
    @Autowired
    private Bus bus;

    @Autowired
    private IScmUpService iScmUpService;

    @Bean(name = "endUpf")
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, iScmUpService);
        endpoint.publish("/upf");
        return endpoint;
    }
}
