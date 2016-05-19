package bilokhado.showconfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ShowConfigServletTest {

    private static final String SERVLET_CONTEXT_PATH = "Context_Path";
    private static final String SERVLET_CONTEXT_NAME = "Context_Name";
    private static final String SERVER_INFO = "Server_Info";
    private static final int EFFECTIVE_MAJOR = 42;
    private static final int EFFECTIVE_MINOR = 24;
    private static final int VERSION_MAJOR = 21;
    private static final int VERSION_MINOR = 12;
    private static final String CONTEXT_INIT_PARAM1 = "Context_init_param_1";
    private static final String CONTEXT_INIT_PARAM2 = "Context_init_param_2";
    private static final String CONTEXT_ATTRIBUTE1 = "Context_attribute_1";
    private static final String CONTEXT_ATTRIBUTE2 = "Context_attribute_2";

    private static final String SERVLET_NAME = "Servlet_name";
    private static final String SERVLET_INIT_PARAM1 = "Servlet_init_param_1";
    private static final String SERVLET_INIT_PARAM2 = "Servlet_init_param_2";

    private ShowConfigServlet servlet;
    private ServletConfig config;
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @Before
    public void init() throws Exception {
        initializeServletConfig();
        initializeServlet();
        initializeRequestResponse();
    }

    @After
    public void cleanup() {
        printWriter.close();
    }

    @Test
    public void shouldShowFromConfig() throws Exception {
        servlet.doGet(request, response);
        String servletOutput = stringWriter.toString();
        verify(response).getWriter();
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVLET_NAME)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVLET_INIT_PARAM1)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVLET_INIT_PARAM2)));
    }

    @Test
    public void shouldShowFromContext() throws Exception {
        servlet.doGet(request, response);
        String servletOutput = stringWriter.toString();
        verify(response).getWriter();
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVER_INFO)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVLET_CONTEXT_NAME)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(SERVLET_CONTEXT_PATH)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(CONTEXT_INIT_PARAM1)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(CONTEXT_INIT_PARAM2)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(CONTEXT_ATTRIBUTE1)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(CONTEXT_ATTRIBUTE2)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(VERSION_MAJOR + "." + VERSION_MINOR)));
        assertThat(servletOutput, containsString(wrapStringInHtmlColumn(EFFECTIVE_MAJOR + "." + EFFECTIVE_MINOR)));
    }

    private void initializeServletConfig() throws Exception {
        context = mock(ServletContext.class);
        when(context.getContextPath()).thenReturn(SERVLET_CONTEXT_PATH);
        when(context.getServletContextName()).thenReturn(SERVLET_CONTEXT_NAME);
        when(context.getServerInfo()).thenReturn(SERVER_INFO);
        when(context.getEffectiveMajorVersion()).thenReturn(EFFECTIVE_MAJOR);
        when(context.getEffectiveMinorVersion()).thenReturn(EFFECTIVE_MINOR);
        when(context.getMajorVersion()).thenReturn(VERSION_MAJOR);
        when(context.getMinorVersion()).thenReturn(VERSION_MINOR);
        Enumeration paramsEnumeration = new StringTokenizer(CONTEXT_INIT_PARAM1 + " " + CONTEXT_INIT_PARAM2);
        when(context.getInitParameterNames()).thenReturn(paramsEnumeration);
        when(context.getInitParameter(CONTEXT_INIT_PARAM1)).thenReturn(CONTEXT_INIT_PARAM1);
        when(context.getInitParameter(CONTEXT_INIT_PARAM2)).thenReturn(CONTEXT_INIT_PARAM2);
        paramsEnumeration = new StringTokenizer(CONTEXT_ATTRIBUTE1 + " " + CONTEXT_ATTRIBUTE2);
        when(context.getAttributeNames()).thenReturn(paramsEnumeration);
        when(context.getAttribute(CONTEXT_ATTRIBUTE1)).thenReturn(CONTEXT_ATTRIBUTE1);
        when(context.getAttribute(CONTEXT_ATTRIBUTE2)).thenReturn(CONTEXT_ATTRIBUTE2);
        config = mock(ServletConfig.class);
        when(config.getServletContext()).thenReturn(context);
        when(config.getServletName()).thenReturn(SERVLET_NAME);
        paramsEnumeration = new StringTokenizer(SERVLET_INIT_PARAM1 + " " + SERVLET_INIT_PARAM2);
        when(config.getInitParameterNames()).thenReturn(paramsEnumeration);
        when(config.getInitParameter(SERVLET_INIT_PARAM1)).thenReturn(SERVLET_INIT_PARAM1);
        when(config.getInitParameter(SERVLET_INIT_PARAM2)).thenReturn(SERVLET_INIT_PARAM2);
    }

    private void initializeServlet() throws Exception {
        servlet = new ShowConfigServlet();
        servlet.init(config);
    }

    private void initializeRequestResponse() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter, true);
        when(response.getWriter()).thenReturn(printWriter);
    }

    private String wrapStringInHtmlColumn(String columnText) {
        return "<td>" + columnText + "</td>";
    }

}
