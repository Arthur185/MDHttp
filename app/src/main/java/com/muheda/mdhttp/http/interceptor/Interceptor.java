package com.muheda.mdhttp.http.interceptor;

import com.muheda.mdhttp.http.Response;

import java.io.IOException;

/**
 * @author wangfei
 * @date 2019/10/10.
 * 拦截器统一接口
 */
public interface Interceptor {
    Response intercept(InterceptorChain interceptorChain) throws IOException;
}
