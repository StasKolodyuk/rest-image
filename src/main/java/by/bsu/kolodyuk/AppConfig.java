package by.bsu.kolodyuk;

import by.bsu.kolodyuk.web.service.UserService;
import by.bsu.kolodyuk.web.filter.SupportCORSFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.validation.BeanValidationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import java.util.Arrays;

import static java.util.Arrays.asList;

@Configuration
@ComponentScan(basePackages = "by.bsu.kolodyuk")
public class AppConfig
{

    @Bean(destroyMethod = "destroy")
    public Server server()
    {
        JAXRSServerFactoryBean factory = serverFactoryBean();
        factory.setBus(cxf());
        return factory.create();
    }

    @Bean
    public JAXRSServerFactoryBean serverFactoryBean()
    {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(new Application(),
                JAXRSServerFactoryBean.class);
        factory.setServiceBeans(Arrays.asList(userService(), swaggerResourceJSON()));
        factory.setProviders(Arrays.asList(jsonProvider(), supportCORSFilter(), resourceWriter(), apiWriter()));
        factory.setFeatures(asList(beanValidationFeature()));
        return factory;
    }

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf()
    {
        SpringBus bus = new SpringBus();
        bus.getInInterceptors().add(new LoggingInInterceptor());
        bus.getOutInterceptors().add(new LoggingOutInterceptor());
        return bus;
    }

    @Bean
    public JacksonJsonProvider jsonProvider()
    {
        return new JacksonJsonProvider(objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new JaxbAnnotationModule().setPriority(JaxbAnnotationModule.Priority.SECONDARY));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public UserService userService()
    {
        return new UserService();
    }

    @Bean
    public SupportCORSFilter supportCORSFilter()
    {
        return new SupportCORSFilter();
    }

    @Bean
    public ApiListingResourceJSON swaggerResourceJSON()
    {
        return new ApiListingResourceJSON();
    }

    @Bean
    public ResourceListingProvider resourceWriter()
    {
        return new ResourceListingProvider();
    }

    @Bean
    public ApiDeclarationProvider apiWriter()
    {
        return new ApiDeclarationProvider();
    }

    @Bean
    public BeanValidationFeature beanValidationFeature()
    {
        return new BeanValidationFeature();
    }

    @Bean
    public BeanConfig swaggerConfig()
    {
        final BeanConfig config = new BeanConfig();
        config.setResourcePackage("by.bsu.kolodyuk");
        config.setBasePath("/");
        config.setScan(true);
        return config;
    }
}
