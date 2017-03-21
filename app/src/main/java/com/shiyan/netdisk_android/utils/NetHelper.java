/*
 * MIT License
 *
 * Copyright (c) 2017 石岩
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shiyan.netdisk_android.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class NetHelper {

    public static class LoginModel {
        String ip;
        String port;
        String username;
        String password;

        public LoginModel(String ip, String port, String username, String password) {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.password = password;
        }
    }

    private static NetHelper INSTANCE;
    private OkHttpClient client;

    private NetHelper() {
        client = new OkHttpClient();
    }

    public static NetHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (NetHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void newCall(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    public void login(LoginModel loginModel , final CallBack callback) throws JSONException, IllegalArgumentException {
        JSONObject object = new JSONObject();
        object.put("name", loginModel.username);
        object.put("password", loginModel.password);
        JSONObject data = new JSONObject();
        data.put("user", object);

        Request request = new Request.Builder()
                .url(String.format("http://%s:%s/v1/login", loginModel.ip, loginModel.port))
                .post(RequestBody.create(MediaType.parse("application/json"), data.toString()))
                .build();
        newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.error("IO_ERROR");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    callback.success(result);
                } else {
                    callback.error("RESPONSE_FAIL");
                }
            }
        });
    }
}
