package server;

import java.io.IOException;
import java.util.Arrays;

public class LagouServlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response) throws IOException, InterruptedException {

        Thread.sleep(5000);
        String content = "<h1>LagouServlet get</h1>";
        response.output(HttpProtocolUtil.getHttpHeader200((long) content.getBytes().length)+ content);
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        String content = "<h1>LagouServlet post</h1>";
        response.output(HttpProtocolUtil.getHttpHeader200((long) content.getBytes().length)+ content);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
