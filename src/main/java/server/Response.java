package server;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    //输出指定字符串
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }
    /**
     *
     * @param path url /1.html
     *             根据url来获取到静态资源的路径，读取并输出
     */
    public void outputHtml(String path) throws IOException {
        //静态资源的绝对路径
        String absoluteResourcePath= StaticResourceUtil.getAbsolutePath(path);
        //输出静态资源
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()){
            //读取、输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else {
            //输出404
            this.output(HttpProtocolUtil.getHttpHeader404());
        }
    }


}
