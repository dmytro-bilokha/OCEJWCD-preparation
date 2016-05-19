package bilokhado.showresreq;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.UnaryOperator;

public class ShowResponseRequestServlet extends HttpServlet {

    private static final String PAGE_HEAD = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            "table {\n" +
            "    border-collapse: collapse;\n" +
            "}\n" +
            "\n" +
            "table, td, th {\n" +
            "    border: 1px solid black;\n" +
            "}\n" +
            "</style>\n" +
            "<title>Show Response and Request Servlet</title>" +
            "</head>\n" +
            "<body>\n";
    private static final String PAGE_TAIL = "</body></html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> servletData = new TreeMap<>();
        PrintWriter pw = resp.getWriter();
        pw.println(PAGE_HEAD);
        //Retrieve data from the request
        servletData.put("getAuthType()", req.getAuthType());
        servletData.put("getContextPath()", req.getContextPath());
        servletData.put("getMethod()", req.getMethod());
        servletData.put("getPathInfo()", req.getPathInfo());
        servletData.put("getPathTranslated()", req.getPathTranslated());
        servletData.put("getQueryString()", req.getQueryString());
        servletData.put("getRequestedSessionId()", req.getRequestedSessionId());
        servletData.put("getRequestURI()", req.getRequestURI());
        servletData.put("getRequestURL()", req.getRequestURL().toString());
        servletData.put("getServletPath()", req.getServletPath());
        servletData.put("getCharacterEncoding()", req.getCharacterEncoding());
        servletData.put("getLocale()", req.getLocale().toString());
        servletData.put("getContentType()", req.getContentType());
        servletData.put("getContentLength()", Integer.toString(req.getContentLength()));
        servletData.put("getServerName()", req.getServerName());
        servletData.put("getLocalAddr()", req.getLocalAddr());
        servletData.put("getLocalName()", req.getLocalName());
        servletData.put("getLocalPort()", Integer.toString(req.getLocalPort()));
        servletData.put("getProtocol()", req.getProtocol());
        servletData.put("getRemoteAddr()", req.getRemoteAddr());
        servletData.put("getRemoteHost()", req.getRemoteHost());
        servletData.put("getRemoteUser()", req.getRemoteUser());
        servletData.put("getRemotePort()", Integer.toString(req.getRemotePort()));
        servletData.put("getScheme()", req.getScheme());
        servletData.put("isRequestedSessionIdFromCookie()", Boolean.toString(req.isRequestedSessionIdFromCookie()));
        servletData.put("isRequestedSessionIdFromURL()", Boolean.toString(req.isRequestedSessionIdFromURL()));
        servletData.put("isRequestedSessionIdValid()", Boolean.toString(req.isRequestedSessionIdValid()));
        servletData.put("isAsyncSupported()", Boolean.toString(req.isAsyncSupported()));
        servletData.put("isAsyncStarted()", Boolean.toString(req.isAsyncStarted()));
        Enumeration<String> headersEnumeration = req.getHeaderNames();
        putEnumerationToMap("getHeader", req::getHeader, headersEnumeration, servletData);
        Enumeration<String> parametersEnumeration = req.getParameterNames();
        putEnumerationToMap("getParameter", req::getParameter, parametersEnumeration, servletData);
        printHtmlTable("Data from the request", servletData, pw);
        //Retrieve data from the response
        servletData = new TreeMap<>();
        servletData.put("getLocale()", resp.getLocale().toString());
        servletData.put("getContentType()", resp.getContentType());
        servletData.put("getCharacterEncoding()", resp.getCharacterEncoding());
        servletData.put("getStatus()", Integer.toString(resp.getStatus()));
        servletData.put("getBufferSize()", Integer.toString(resp.getBufferSize()));
        Collection<String> respHeaderNames = resp.getHeaderNames();
        for (String headerName : respHeaderNames) {
            servletData.put("getHeader(\"" + headerName +"\")", resp.getHeader(headerName));
        }
        printHtmlTable("Data from the response", servletData, pw);
        pw.println(PAGE_TAIL);
    }

    private void printHtmlTable(String caption, Map<String, String> configData, PrintWriter outputWriter) {
        outputWriter.println("<table><caption>" + StringEscapeUtils.escapeHtml4(caption) + "</caption><tbody>");
        for(Map.Entry<String, String> configElement : configData.entrySet()) {
           outputWriter.println(String.format("<tr><td>%s</td><td>%s</td></tr>",
                   StringEscapeUtils.escapeHtml4(configElement.getKey()),
                   StringEscapeUtils.escapeHtml4(configElement.getValue())));
        }
        outputWriter.println("</tbody></table>");
    }

    private void putEnumerationToMap(String methodName, UnaryOperator<String> operator,
                                     Enumeration<String> configEnumeration, Map<String, String> configData) {
        while(configEnumeration.hasMoreElements()) {
            String configElement = configEnumeration.nextElement();
            configData.put(methodName + "(\"" + configElement + "\")",
                    configElement == null ? "null" : operator.apply(configElement));
        }
    }

}
