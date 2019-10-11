package com.muheda.mdhttp.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public class MDHttpHelper {
    private volatile static MDHttpHelper mdHttpHelper;
    private volatile static HttpClient client;
    private Gson mGson;

    private MDHttpHelper() {
        client = new HttpClient.Builder()
                .setRetryTimes(3)
                .build();
        mGson = new Gson();
    }

    public static MDHttpHelper getInstance() {
        if (mdHttpHelper == null) {
            synchronized (MDHttpHelper.class) {
                if (mdHttpHelper == null) {
                    mdHttpHelper = new MDHttpHelper();
                }
            }
        }
        return mdHttpHelper;
    }

    /**
     * 封装一个request方法，不管post或者get方法中都会用到
     */
    public void request(final Request request, final BaseCallback callback) {

        //在请求之前所做的事，比如弹出对话框等
        callback.onRequestBefore();

        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(final ObservableEmitter<Response> emitter) throws Exception {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException throwable) {
                        emitter.onError(throwable);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) {
                        emitter.onNext(response);
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response value) {
                        Log.e("ttttttttttt", "doOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        if (value.isSuccessful()) {
                            //返回成功回调
                            String resString = value.getBody();

                            if (callback.mType == String.class) {
                                //如果我们需要返回String类型
                                callback.onSuccess(value, resString);
                            } else {
                                //如果返回的是其他类型，则利用Gson去解析
                                try {
                                    Object o = mGson.fromJson(resString, callback.mType);
                                    callback.onSuccess(value, resString);
                                } catch (JsonParseException e) {
                                    e.printStackTrace();
                                    callback.onError(value, value.getCode(), e);
                                }
                            }

                        } else {
                            //返回错误
                            callback.onError(value, value.getCode(), null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(request, (Exception) e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 对外公开的get方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, BaseCallback callback) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        request(request, callback);
    }

    /**
     * 对外公开的post方法
     *
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url, Map<String, String> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        request(request, callback);
    }

    /**
     * 构建请求对象
     *
     * @param url
     * @param params
     * @param type
     * @return
     */
    private Request buildRequest(String url, Map<String, String> params, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.setHttpUrl(url);
        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            builder.post(buildRequestBody(params));
        }
        return builder.build();
    }

    /**
     * 通过Map的键值对构建请求对象的body
     *
     * @param params
     * @return
     */
    private RequestBody buildRequestBody(Map<String, String> params) {
        RequestBody body = new RequestBody();
        if (params != null) {
            for (Map.Entry<String, String> entity : params.entrySet()) {
                body.add(entity.getKey(), entity.getValue());
            }
        }
        return body;
    }

    /**
     * 枚举用于指明是哪一种提交方式
     */
    enum HttpMethodType {
        GET,
        POST
    }


}
