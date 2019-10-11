package com.muheda.mdhttp.http;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author wangfei
 * @date 2019/10/10.
 * 创建与服务器连接的socket
 */
public class HttpConnection {

    Socket socket;

    long lastUseTime;

    private Request request;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void setRequest(Request request) {
        this.request = request;
    }

    public void updateLastUseTime(){
        lastUseTime = System.currentTimeMillis();
    }
    public boolean isSameAddress(String host, int port){
        if(null == socket){
            return false;
        }

        return TextUtils.equals(request.getHttpUrl().getHost(),host) && request.getHttpUrl().port == port;
    }

    /**
     * 创建socket连接
     * @throws IOException
     */
    private void createSocket() throws IOException{

        if(null == socket || socket.isClosed()){

            HttpUrl httpUrl = request.getHttpUrl();
            if(httpUrl.protocol.equalsIgnoreCase(HttpCodec.PROTOCOL_HTTPS)){
                //如果是https，就需要使用jdk默认的SSLSocketFactory来创建socket
                socket = SSLSocketFactory.getDefault().createSocket();
            }else{
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(httpUrl.getHost(),httpUrl.getPort()));
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
    }

    /**
     * 关闭socket的连接
     */
    public void close(){
        if(null != socket){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public InputStream call(HttpCodec httpCodec) throws IOException {
        //创建socket
        createSocket();
        //发送请求
        httpCodec.writeRequest(outputStream,request);
        //返回服务器响应 (InputStream)
        return inputStream;
    }
}
