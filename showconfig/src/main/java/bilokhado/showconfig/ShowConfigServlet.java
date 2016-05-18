package bilokhado.showconfig;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ShowConfigServlet extends HttpServlet {

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
            "<title>Show Config Servlet</title>" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h2>Servlet config:</h2>";
    private static final String PAGE_TAIL = "</body></html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> configData = new HashMap<>();
        PrintWriter pw = resp.getWriter();
        pw.println(PAGE_HEAD);
        configData.put("getServletName()", getServletName());
        Enumeration<String> initParameterNames = getInitParameterNames();
        putConfigEnumerationToMap("getInitParameter", this::getInitParameter, initParameterNames, configData);
        printConfigHtmlTable("Data from GenericServlet methods", configData, pw);
        pw.println(PAGE_TAIL);
    }

    private void printConfigHtmlTable(String caption, Map<String, String> configData, PrintWriter outputWriter) {
        outputWriter.println("<table><caption>" + StringEscapeUtils.escapeHtml4(caption) + "</caption><tbody>");
        for(Map.Entry<String, String> configElement : configData.entrySet()) {
           outputWriter.println(String.format("<tr><td>%s</td><td>%s</td></tr>",
                   StringEscapeUtils.escapeHtml4(configElement.getKey()),
                   StringEscapeUtils.escapeHtml4(configElement.getValue())));
        }
        outputWriter.println("</tbody></table>");
    }

    private void putConfigEnumerationToMap(String methodName, UnaryOperator<String> operator,
                                           Enumeration<String> configEnumeration, Map<String, String> configData) {
        while(configEnumeration.hasMoreElements()) {
            String configElement = configEnumeration.nextElement();
            configData.put(methodName + "(\"" + configElement + "\")", operator.apply(configElement));
        }
    }

}
