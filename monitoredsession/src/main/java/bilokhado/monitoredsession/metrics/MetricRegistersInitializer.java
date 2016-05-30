package bilokhado.monitoredsession.metrics;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlet.InstrumentedFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.management.ManagementFactory;

public class MetricRegistersInitializer implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, new HealthCheckRegistry());
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.registerAll(new JvmAttributeGaugeSet());
        metricRegistry.registerAll(new MemoryUsageGaugeSet());
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        metricRegistry.registerAll(new ThreadStatesGaugeSet());
        metricRegistry.registerAll(new ClassLoadingGaugeSet());
        metricRegistry.registerAll(new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        context.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
        context.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    //Do nothing
    }
}
