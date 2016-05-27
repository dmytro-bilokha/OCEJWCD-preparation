package bilokhado.monitoredsession.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlet.InstrumentedFilter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class MetricRegistersInitializer implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, new HealthCheckRegistry());
        MetricRegistry metricRegistry = new MetricRegistry();
        context.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
        context.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    //Do nothing
    }
}
