package bilokhado.loggingfilter.servlet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class HelloServletIT {

    private static final String SERVLET_URI = "/HelloWorld";

    private static ServletTester tester;

    /**
     * Starts and sets up the Jetty container
     */
    @BeforeClass
    public static void initServletContainer() throws Exception {
        tester = new ServletTester();
        tester.setContextPath("/");
        tester.addServlet(HelloServlet.class, SERVLET_URI);
        tester.start();
    }

    /**
     * Stops the Jetty container.
     */
    @AfterClass
    public static void cleanupServletContainer() throws Exception {
        tester.stop();
    }

    @Test
    public void testGetResponsesHtml() throws Exception {
        HttpTester req = new HttpTester();
        req.setMethod("GET");
        req.setURI(SERVLET_URI);
        req.setVersion("HTTP/1.0");
        HttpTester resp = new HttpTester();
        resp.parse(tester.getResponses(req.generate()));
        assertEquals(200, resp.getStatus());
        String servletOutput = resp.getContent();
        assertThat(servletOutput, containsString("Hello World"));
        assertThat(servletOutput, containsString("Servlet"));
    }

    @Test
    public void shouldFail() {
        assertEquals("aaa", "");
    }

}
