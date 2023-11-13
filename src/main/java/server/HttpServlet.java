package server;

import java.io.IOException;

public abstract class HttpServlet implements Servlet{


    public abstract void doGet(Request request,Response response) throws IOException, InterruptedException;
    public abstract void doPost(Request request,Response response) throws IOException;
    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equals(request.getMethod())) {
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }


}
