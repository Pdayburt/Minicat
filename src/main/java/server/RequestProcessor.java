package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread{
    private Socket socket;
    private Map<String,HttpServlet> serverletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> serverletMap) {
        this.socket = socket;
        this.serverletMap = serverletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            //静态资源处理
            if (!serverletMap.containsKey(request.getUrl())) {
                response.outputHtml(request.getUrl());
            }else {
                //其他的是动态资源
                HttpServlet httpServlet = serverletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
            socket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
