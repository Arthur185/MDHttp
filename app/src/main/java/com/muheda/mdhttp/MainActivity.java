package com.muheda.mdhttp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.muheda.mdhttp.http.BaseCallback;
import com.muheda.mdhttp.http.MDHttpHelper;
import com.muheda.mdhttp.http.Request;
import com.muheda.mdhttp.http.RequestBody;
import com.muheda.mdhttp.http.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据okhttp手写简单网络请求框架 使用习惯模仿okhttp
 * 1,创建全局HttpClient对象 (Builder模式) 可设置失败重连次数，拦截器，调度器，连接线程池
 * 2,创建Request对象(Builder模式)设置请求的一些参数
 * 3,创建Call对象,通过HttpClient来拿到
 * 4,Call类中的AsyncCall线程，是请求真正开始的地方，使用者调用enqueue的时候，只是把这个AsyncCall线程添加到调度器Dispather的线程池
 * 5,调度器中只是负责把添加进来的请求进行按序执行管理，真正执行是在AsyncCall线程中run方法 责任链拦截器（默认顺序不能更换）也在此处进行处理
 * <p>
 * ,
 */

public class MainActivity extends AppCompatActivity {

    private MDHttpHelper mHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpHelper = MDHttpHelper.getInstance();

    }

    public void get(View view) {
        mHttpHelper.get("http://www.kuaidi100.com/query?type=yuantong&postid=222222222", new BaseCallback<String>() {

            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String string) {
                Log.e("响应体", response.getBody());
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });
    }

    public void post(View view) {

        Map map = new HashMap<String, String>();
        map.put("key", "064a7778b8389441e30f91b8a60c9b23");
        map.put("city", "深圳");

        mHttpHelper.post("http://restapi.amap.com/v3/weather/weatherInfo", map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String string) {
                Log.e("响应体", response.getBody());
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });
    }
}
