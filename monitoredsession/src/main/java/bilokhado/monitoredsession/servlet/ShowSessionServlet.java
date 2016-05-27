package bilokhado.monitoredsession.servlet;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codahale.metrics.MetricRegistry.name;

public class ShowSessionServlet extends HttpServlet {

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
            "<title>Show Session Data Servlet</title>" +
            "</head>\n" +
            "<body>\n" +
            "<table><caption>Session Data</caption><tbody>\n";
    private static final String PAGE_TAIL = "</tbody></table>\n" +
            "</body></html>\n";
    private static final String ROW_FORMAT = "<tr><td>%s</td><td>%s</td></tr>";

    private Counter getInvoked;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        Object registryObject = context.getAttribute(MetricsServlet.METRICS_REGISTRY);
        if (registryObject == null || !(registryObject instanceof MetricRegistry)) {
            throw new UnavailableException("Unable to find MetricRegistry object in attribute " +
                    MetricsServlet.METRICS_REGISTRY);
        }
        MetricRegistry metricRegistry = (MetricRegistry) registryObject;
        getInvoked = metricRegistry.counter(name(ShowSessionServlet.class, "getInvoked"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (!session.isNew() && "yes".equals(req.getParameter("invalidate"))) {
            session.invalidate();
            resp.sendRedirect(resp.encodeRedirectURL(req.getRequestURI()));
            return;
        }
        PrintWriter pw = resp.getWriter();
        pw.print(PAGE_HEAD);
        printRow(pw, "getId", session.getId());
        printRow(pw, "isNew", Boolean.toString(session.isNew()));
        printRow(pw, "getCreationTime",
            LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()),
                    ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        printRow(pw, "getLastAccessedTime",
                LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()),
                        ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        printRow(pw, "getMaxInactiveInterval", Integer.toString(session.getMaxInactiveInterval()));
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            printRow(pw, "Attribute: " + name, String.valueOf(value));
        }
        if (session.getAttribute("Counter") == null) {
            session.setAttribute("Counter", new AtomicInteger());
        } else {
            ((AtomicInteger)session.getAttribute("Counter")).incrementAndGet();
        }
        pw.print(PAGE_TAIL);
        getInvoked.inc();
    }

    private void printRow (PrintWriter printWriter, String name, String value) {
        printWriter.println(String.format(ROW_FORMAT, StringEscapeUtils.escapeHtml4(name),
                StringEscapeUtils.escapeHtml4(value)));
    }

}
