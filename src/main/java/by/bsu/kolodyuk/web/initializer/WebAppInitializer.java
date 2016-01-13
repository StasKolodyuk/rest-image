package by.bsu.kolodyuk.web.initializer;

import by.bsu.kolodyuk.AppConfig;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer extends AbstractContextLoaderInitializer
{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
        super.onStartup(servletContext);
        ServletRegistration.Dynamic registration = servletContext.addServlet("CXFServlet", new CXFServlet());
        registration.addMapping("/*");
        registration.setLoadOnStartup(1);
        registration.setInitParameter("static-resources-list", "/swagger/(\\S)+");
    }

    @Override
    protected WebApplicationContext createRootApplicationContext()
    {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        return context;
    }
}
