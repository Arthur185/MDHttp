package com.muheda.mdhttp.http.interceptor;

import android.util.Log;

import com.muheda.mdhttp.http.HttpClient;
import com.muheda.mdhttp.http.HttpConnection;
import com.muheda.mdhttp.http.HttpUrl;
import com.muheda.mdhttp.http.Request;
import com.muheda.mdhttp.http.Response;

import java.io.IOException;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public class ConnectionInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {
        Log.e("interceptor", "连接拦截器开始运行");
        Request request = interceptorChain.call.getRequest();
        HttpClient httpClient = interceptorChain.call.getHttpClient();
        HttpUrl httpUrl = request.getHttpUrl();

        HttpConnection httpConnection = httpClient.getConnectionPool().getHttpConnection(httpUrl.getHost(),httpUrl.getPort());
        if(null == httpConnection){
            httpConnection = new HttpConnection();
        }else{
            Log.e("interceptor", "从连接池中获得连接");
        }
        httpConnection.setRequest(request);

        try {
            Response response = interceptorChain.proceed(httpConnection);
            if (response.isKeepAlive()){
                httpClient.getConnectionPool().putHttpConnection(httpConnection);
            }else{
                httpConnection.close();
            }
            return response;
        }catch (IOException e){
            httpConnection.close();
            throw e;
        }
    }
}
