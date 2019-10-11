package com.muheda.mdhttp.http;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public class Request {
    private Map<String,String> headers;//http包请求头
    private String method;//请求方法，post 或者 get方法
    private HttpUrl httpUrl;//http的url信息
    private RequestBody requestBody;//如果是post请求，还会有requestBody存参数信息

    public Request(Builder builder){
        this.headers = builder.headers;
        this.method = builder.method;
        this.httpUrl = builder.httpUrl;
        this.requestBody = builder.requestBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public String getMethod() {
        return method;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public static final class Builder{

        Map<String,String> headers = new HashMap<>();
        String method;
        HttpUrl httpUrl;
        RequestBody requestBody;

        public Builder addHeader(String key,String value){
            headers.put(key,value);
            return this;
        }

        public Builder removeHeader(String key){
            headers.remove(key);
            return this;
        }

        public Builder post(RequestBody requestBody){
            this.requestBody = requestBody;
            this.method = "POST";
            return this;
        }

        public Builder get(){
            this.method = "GET";
            return this;
        }

        public Builder setHttpUrl(String url){
            try {
                this.httpUrl = new HttpUrl(url);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Http Url Format Error!",e);
            }
        }

        public Request build(){

            if(null == httpUrl){
                throw new IllegalStateException("url is null!");
            }
            if(null == method){
                method = "GET";
            }

            return new Request(this);
        }
    }
}
