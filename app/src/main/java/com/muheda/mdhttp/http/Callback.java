package com.muheda.mdhttp.http;

import java.io.IOException;

/**
 * @author wangfei
 * @date 2019/10/10.
 */
public interface Callback {

    void onFailure(Call call, IOException throwable);

    void onResponse(Call call, Response response);
}
