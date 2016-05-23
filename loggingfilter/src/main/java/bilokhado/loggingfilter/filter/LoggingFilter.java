package bilokhado.loggingfilter.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("[{}] {}::Query String::{}", request.getMethod(), request.getRequestURL().toString(),
                request.getQueryString());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    //We have no resources to free, so do nothing
    }

}
