package com.muheda.mdhttp.http;

import com.muheda.mdhttp.http.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public class HttpClient {
    //设置调度器
    private Dispather dispather;

    private List<Interceptor> interceptors;

    private int retryTimes;

    private ConnectionPool connectionPool;

    /**
     * 构造方法
     */
    public HttpClient(Builder builder) {
        this.dispather = builder.dispather;
        this.interceptors = builder.interceptors;
        this.retryTimes = builder.retryTimes;
        this.connectionPool = builder.connectionPool;
    }

    /**
     * 生成一个网络请求Call对象实例
     *
     * @param request
     * @return
     */
    public Call newCall(Request request) {
        return new Call(this, request);
    }

    /**
     * 建造对象
     */
    public static final class Builder {

        Dispather dispather;
        List<Interceptor> interceptors = new ArrayList<>();
        int retryTimes;
        ConnectionPool connectionPool;

        public Builder addInterceptors(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public Builder setDispather(Dispather dispather) {
            this.dispather = dispather;
            return this;
        }

        public Builder setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder setConnectionPool(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }


        public HttpClient build() {

            if (null == dispather) {
                dispather = new Dispather();
            }

            if (null == connectionPool) {
                connectionPool = new ConnectionPool();
            }
            return new HttpClient(this);
        }

    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Dispather getDispather() {
        return dispather;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
