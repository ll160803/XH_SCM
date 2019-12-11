package cc.mrbird.febs.webService.SapToScm;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CxfConfig {
    @Autowired
    private Bus bus;

    @Autowired
    TestImpl saPtoSCM;

//    @Bean
//    public ServletRegistrationBean<CXFServlet> dispatcherServlet(){
//        ServletRegistrationBean<CXFServlet> registrationBean =new ServletRegistrationBean<CXFServlet>(new CXFServlet(),"/ws/*");
//        return registrationBean;
//    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus()
    {
        return  new SpringBus();
    }

    @Bean
    public ITestService userService()
    {
        return  new TestImpl();
    }
    /** JAX-WS **/
    @Bean
    public Endpoint endpointSap() {
        EndpointImpl endpoint=new EndpointImpl(springBus(), saPtoSCM);//绑定要发布的服务
       // EndpointImpl endpoint = new EndpointImpl(bus, saPtoSCM);
        endpoint.publish("/test");
        return endpoint;
    }
}
