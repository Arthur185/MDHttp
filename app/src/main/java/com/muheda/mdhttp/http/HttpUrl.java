package com.muheda.mdhttp.http;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public class HttpUrl {

    String protocol;//协议，http或者https
    String host;//服务器地址
    String file;//请求服务器文件路径
    int port;//服务器服务端口

    public HttpUrl(String url) throws MalformedURLException {
        URL localUrl = new URL(url);//url格式化
        host = localUrl.getHost();
        protocol = localUrl.getProtocol();
        file = localUrl.getFile();
        port = localUrl.getPort();
        if (port == -1) {
            //代表url中没有端口信息，就是使用默认端口，http:80,https:443
            port = localUrl.getDefaultPort();
        }

        if (TextUtils.isEmpty(file)) {
            //如果为空，默认加上"/"
            file = "/";
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getFile() {
        return file;
    }

    public int getPort() {
        return port;
    }
}
