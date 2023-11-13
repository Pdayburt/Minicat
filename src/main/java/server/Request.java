package server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Request {

    private String method;//请求方式
    private String url;
    private InputStream inputStream;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public Request() {
    }


    public Request(InputStream inputStream) throws IOException {

        this.inputStream = inputStream;
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String inputStr = new String(bytes);
        log.info("前端传入的request------>\n" + inputStr);
        String firstLine = inputStr.split("\n")[0];
        String[] firstLineStrings = firstLine.split(" ");
        this.method = firstLineStrings[0];
        this.url = firstLineStrings[1];
        log.info("method------>:"+method);
        log.info("url------>:"+url);

    }



}
