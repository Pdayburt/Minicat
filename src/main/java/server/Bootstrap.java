package server;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Minicat主类
 */
public class Bootstrap {


    /**
     * 定义docket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Minicat 启动需要的初始化的操作
     */
    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>Minicat start on port:" + port);

  /*      while (true){
            //没有请求就阻塞
            Socket socket = serverSocket.accept();
            //接受到请求
            OutputStream outputStream = socket.getOutputStream();
            String data =  "Hello Minicat";
            String responseText = HttpProtocolUtil.getHttpHeader200((long) "Hello Minicat".getBytes().length)+data;
            System.out.println("responseText===>\n"+responseText);
            outputStream.write(responseText.getBytes());
            socket.close();
        }*/

        /*while (true) {

            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            */
            /**
             * 封装request和response对象
             */
            /*
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());
            socket.close();

        }*/

        /**
         * 请求动态资源
         */


       /* while (true) {
            //加载解析相关配置 web.xml
            loadServlet();
            Socket socket = serverSocket.accept();
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
        }*/

        /**
         * 多线程改造 (不使用线程池)
         */

//        while (true){
//            loadServlet();
//            Socket socket = serverSocket.accept();
//            RequestProcessor requestProcessor = new RequestProcessor(socket, serverletMap);
//            requestProcessor.start();
//
//        }

        /**
         * 多线程改造 （使用线程池）
         */
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler =new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
        System.out.println("---------->使用多线程进行改造");
        while (true){
            loadServlet();
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, serverletMap);
            threadPoolExecutor.execute(requestProcessor);
        }



    }

    private Map<String, HttpServlet> serverletMap = new HashMap<>();
    /**
     * 加载解析web.xml 初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Node> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Node node = selectNodes.get(i);
               // <servlet-name>lagou</servlet-name>
                Node servletNameNode = node.selectSingleNode("servlet-name");
                String servletName = servletNameNode.getStringValue();
                //<servlet-class>server.LagouServlet</servlet-class>
                Node servletClassNode = node.selectSingleNode("servlet-class");
                String servletClass = servletClassNode.getStringValue();

                //根据servlet-name的值找到url-pattern的值
                /**
                 * <servlet-mapping>
                 *         <servlet-name>lagou</servlet-name>
                 *         <url-pattern>/lagou</url-pattern>
                 *     </servlet-mapping>
                 */
                Node servletMapping = rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                Class<?> aClass = Class.forName(servletClass);
                HttpServlet httpServlet = (HttpServlet) aClass.newInstance();
                serverletMap.put(urlPattern, httpServlet);

            }

        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Minicat 启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
