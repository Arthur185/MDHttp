package com.muheda.mdhttp.http.interceptor;

import android.util.Log;

import com.muheda.mdhttp.http.HttpCodec;
import com.muheda.mdhttp.http.Request;
import com.muheda.mdhttp.http.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangfei
 * @date 2019/10/10.
 * 头拦截器 确保参数不少
 */
public class HeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {

        Log.e("interceptor", "Http头拦截器开始运行");

        Request request = interceptorChain.call.getRequest();
        Map<String, String> headers = request.getHeaders();
        if (!headers.containsKey(HttpCodec.HEAD_HOST)) {
            headers.put(HttpCodec.HEAD_HOST, request.getHttpUrl().getHost());
        }
        if (!headers.containsKey(HttpCodec.HEAD_CONNECTION)) {
            headers.put(HttpCodec.HEAD_CONNECTION, HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        }

        if (null != request.getRequestBody()) {
            String contentType = request.getRequestBody().getContentType();
            if (null != contentType) {
                headers.put(HttpCodec.HEAD_CONTENT_TYPE, contentType);
            }

            long contentLength = request.getRequestBody().getContentLength();

            if (-1 != contentLength) {
                headers.put(HttpCodec.HEAD_CONTENT_LENGTH, Long.toString(contentLength));
            }
        }
        return interceptorChain.proceed();
    }
}
