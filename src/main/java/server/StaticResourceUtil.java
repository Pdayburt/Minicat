package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class StaticResourceUtil {

    /**
     * 获取静态资源的绝对路径
     * @param  //path
     * @return
     */

    public static String getAbsolutePath(String path){
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath+path;
    }

    /**
     *
     * 读取静态资源文件输入流 通过输出流输出
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0 ){
            count = inputStream.available();
        }
        int resourceSize = count;
        //http请求头
        outputStream.write(HttpProtocolUtil.getHttpHeader200((long) resourceSize).getBytes());
        //读取内容输出
        long writtenSize = 0; // 已经读取的内容长度
        int byteSize = 1024;//计划每次缓冲的长度
        byte[] bytes = new byte[byteSize];
        while (writtenSize < resourceSize) {
            if (writtenSize + byteSize > resourceSize) { //说明剩余未读取大小不足一个1024byte数组
                byteSize = (int) (resourceSize - writtenSize); //剩余文件内容的真实长度
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            writtenSize += byteSize;
        }
    }
}
