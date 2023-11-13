package server;

public class HttpProtocolUtil {

    public static  String getHttpHeader200(Long contentLength){
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Content-Length: "+contentLength+"\n" +
                "\r\n";
    }


    public static  String getHttpHeader404(){
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 not found \n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Content-Length: "+str404.getBytes().length+"\n" +
                "\r\n";
    }
}
