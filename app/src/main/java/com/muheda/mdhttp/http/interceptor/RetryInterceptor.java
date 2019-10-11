package com.muheda.mdhttp.http.interceptor;

import android.util.Log;

import com.muheda.mdhttp.http.Call;
import com.muheda.mdhttp.http.Response;

import java.io.IOException;

/**
 * @author wangfei
 * @date 2019/10/10.
 * 重连连接器
 */
public class RetryInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {
        Log.e("interceptor", "重试拦截器已运行");
        Call call = interceptorChain.call;
        IOException ioException = null;

        for (int i = 0; i < call.getHttpClient().getRetryTimes(); i++) {
            if (call.isCanceled()) {
                throw new IOException("this task is canceled");
            }
            try {
                return interceptorChain.proceed();
            } catch (IOException e) {
                ioException = e;
            }
        }
        throw ioException;
    }
}
