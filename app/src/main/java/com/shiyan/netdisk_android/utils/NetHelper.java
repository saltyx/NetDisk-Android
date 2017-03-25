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

import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.model.UserFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    enum Method {
        POST, PUT, GET, DELETE
    }

    private final static String IO_ERROR = "IO_ERROR";
    private final static String RESPONSE_ERROR = "RESPONSE_ERROR";

    private final String FOLDER_BASE_URL = "http://%s:%s/v1/folder/";
    private final String FILE_BASE_URL = "http://%s:%s/v1/file/";
    private final String UPLOAD_BASE_URL = "http://%s:%s/v1/upload/";

    private final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

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

    public void newCall(final Request request, final CallBack callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.error(IO_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    callback.success(data);
                } else {
                    callback.error(RESPONSE_ERROR);
                }
            }
        });
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
        newCall(request, callback);
    }

    public void getFilesByFolder(int folderId, final CallBack callBack) {

        buildRequest(String.format(Locale.US,FOLDER_BASE_URL.concat("%d"), SecuDiskApplication.IP,SecuDiskApplication.Port, folderId),
                null, Method.GET, callBack);
    }

    public void getRootFolder(final CallBack callBack) {
        getFilesByFolder(1, callBack);
    }

    /**
     * create folder in the server
     * @param fromFolder the folder's id that the new folder belong to
     * @param folderName the new folder's name
     * @param callBack callback when posted to the server
     * @throws JSONException
     */
    public void createFolder(int fromFolder, String folderName, final CallBack callBack) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_name", folderName);
        folder.put("from_folder", fromFolder);
        JSONObject data = new JSONObject();
        data.put("folder", folder);

        buildRequest(String.format(Locale.US, FOLDER_BASE_URL.concat("create"), SecuDiskApplication.IP, SecuDiskApplication.Port),
                data.toString(), Method.POST, callBack);
    }

    /**
     * delete the folder in the server
     * @param folderId the folder's id that will be deleted
     * @param callBack callback when the server responses
     * @throws JSONException
     */
    public void deleteFolder(int folderId, final CallBack callBack) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        JSONObject data = new JSONObject();
        data.put("folder", folder);

        buildRequest(String.format(Locale.US, FOLDER_BASE_URL.concat("delete"), SecuDiskApplication.IP, SecuDiskApplication.Port),
                data.toString(), Method.DELETE, callBack);
    }

    /**
     * update the folder's name in the server
     * @param folderId the folder's id that will be updated
     * @param newName the new name for the folder
     * @param callBack callback when the server responses
     * @throws JSONException
     */
    public void updateFolder(int folderId, String newName, final CallBack callBack) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        folder.put("new_name",newName);
        JSONObject data = new JSONObject();
        data.put("folder", folder);

        buildRequest(String.format(Locale.US, FOLDER_BASE_URL.concat("update"), SecuDiskApplication.IP, SecuDiskApplication.Port),
                data.toString(), Method.PUT, callBack);
    }

    /**
     * encrypt all the files in the server's folder whose id is folderId
     * @param folderId the folder's id that will be encrypted
     * @param passPhrase the password
     * @param callBack
     * @throws JSONException
     */
    public void encryptFolder(int folderId, String passPhrase, final CallBack callBack) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        folder.put("pass_phrase",passPhrase);
        JSONObject data = new JSONObject();
        data.put("folder", folder);

        buildRequest(String.format(Locale.US, FOLDER_BASE_URL.concat("encrypt"), SecuDiskApplication.IP, SecuDiskApplication.Port),
                data.toString(),Method.POST, callBack);
    }

    /**
     * decrypt all the files in the folder
     * @param folderId folder'id
     * @param passPhrase password
     * @param callBack
     * @throws JSONException
     */
    public void decryptFolder(int folderId, String passPhrase, final CallBack callBack) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        folder.put("pass_phrase",passPhrase);
        JSONObject data = new JSONObject();
        data.put("folder", folder);

        buildRequest(String.format(Locale.US, FOLDER_BASE_URL.concat("decrypt"), SecuDiskApplication.IP, SecuDiskApplication.Port),
                data.toString(), Method.POST, callBack);

    }

    

    private void buildRequest(String url, String data,Method method, final CallBack callBack) {
        Request.Builder base = new Request.Builder()
                .url(url)
                .header("Authorization", String.format("Token token=%s",SecuDiskApplication.Token))
                .addHeader("Content-Type", "application/json");

        if (method == Method.POST) {
            base.post(RequestBody.create(JSON_MEDIA_TYPE, data));
        } else if (method == Method.DELETE) {
            base.delete(RequestBody.create(JSON_MEDIA_TYPE, data));
        } else if (method == Method.PUT) {
            base.put(RequestBody.create(JSON_MEDIA_TYPE, data));
        } else {
            base.get();
        }

        newCall(base.build(), callBack);
    }

}
