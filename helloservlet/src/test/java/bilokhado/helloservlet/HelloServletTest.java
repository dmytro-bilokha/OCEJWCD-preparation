package bilokhado.helloservlet;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class HelloServletTest {

    @Test
    public void shouldWriteHelloWorld() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter mockStrWriter = new StringWriter();
        PrintWriter mockPrnWriter = new PrintWriter(mockStrWriter, true);
        when(resp.getWriter()).thenReturn(mockPrnWriter);
        String servletOutput;
        try {
            new HelloServlet().doGet(req, resp);
        } finally {
            servletOutput = mockStrWriter.toString();
            mockPrnWriter.close();
        }
        verify(resp).getWriter();
        assertThat(servletOutput, containsString("Hello World"));
        assertThat(servletOutput, containsString("Servlet"));
    }

}
