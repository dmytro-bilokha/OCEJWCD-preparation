package bilokhado.loggingfilter.filter;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingFilterTest {

    private static final String REQUEST_METHOD = "Method";
    private static final String REQUEST_URL = "Request_URL";
    private static final String QUERY_STRING = "Query_String";
    private static final String LOG_STRING = String.format("[%s] %s::Query String::%s",
            REQUEST_METHOD, REQUEST_URL, QUERY_STRING);
    private static final Level LOGGING_LEVEL = Level.INFO;

    @Mock
    private Appender mockAppender;
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    @Before
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
        when(request.getMethod()).thenReturn(REQUEST_METHOD);
        when(request.getRequestURL()).thenReturn(new StringBuffer(REQUEST_URL));
        when(request.getQueryString()).thenReturn(QUERY_STRING);
    }

    @After
    public void clean() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void shouldLogRequest() throws Exception {
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(LOGGING_LEVEL));
        assertThat(loggingEvent.getFormattedMessage(),
                is(LOG_STRING));
    }
}
