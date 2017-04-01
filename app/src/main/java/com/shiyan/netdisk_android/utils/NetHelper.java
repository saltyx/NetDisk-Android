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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.model.UserFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    private OkHttpClient client;

    private NetHelper() {
        client = new OkHttpClient();
    }

    public static NetHelper getInstance() {

        return NetHelperHolder.sInstance;
    }

    private static class NetHelperHolder {
        private final static NetHelper sInstance = new NetHelper();
    }

    private void newCall(final Request request, final CallBack callback) {
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

        buildRequest(buildBaseFolderUrl("%d",folderId), null, Method.GET, callBack);
    }

    public void getRootFolder(final CallBack callBack) {
        getFilesByFolder(1, callBack);
    }

    public void uploadFile(File file,long fileSize,int fromFolder,CallBack callBack) {
        if (file == null) {
            callBack.error("IllegalArgument");
            return;
        }
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filesize", String.valueOf(fileSize))
                .addFormDataPart("file",file.getName(),RequestBody.create(null, file))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", String.format("Token token=%s",SecuDiskApplication.Token))
                .url(buildBaseUploadUrl("%d",fromFolder))
                .post(body).build();
        newCall(request,callBack);
    }

    /**
     * create folder in the server
     * @param fromFolder the folder's id that the new folder belong to
     * @param folderName the new folder's name
     * @param callBack callback when posted to the server
     * @throws JSONException
     */
    public void createFolder(int fromFolder, String folderName, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFolderUrl("create"), buildCreateFolderParam(fromFolder,folderName), Method.POST, callBack);
    }

    /**
     * delete the folder in the server
     * @param folderId the folder's id that will be deleted
     * @param callBack callback when the server responses
     * @throws JSONException
     */
    public void deleteFolder(int folderId, final CallBack callBack) throws JSONException {

        buildRequest(buildBaseFolderUrl("delete"),buildDeleteFolderParam(folderId), Method.DELETE, callBack);
    }

    /**
     * update the folder's name in the server
     * @param folderId the folder's id that will be updated
     * @param newName the new name for the folder
     * @param callBack callback when the server responses
     * @throws JSONException
     */
    public void updateFolder(int folderId, String newName, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFolderUrl("update"),buildUpdateFolderParam(folderId,newName), Method.PUT, callBack);
    }

    /**
     * encrypt all the files in the server's folder whose id is folderId
     * @param folderId the folder's id that will be encrypted
     * @param passPhrase the password
     * @param callBack
     * @throws JSONException
     */
    public void encryptFolder(int folderId, String passPhrase, final CallBack callBack) throws JSONException {

        buildRequest(buildBaseFolderUrl("encrypt"),buildEncryptFolderParam(folderId,passPhrase),Method.POST, callBack);
    }

    /**
     * decrypt all the files in the folder
     * @param folderId folder'id
     * @param passPhrase password
     * @param callBack
     * @throws JSONException
     */
    public void decryptFolder(int folderId, String passPhrase, final CallBack callBack) throws JSONException {

        buildRequest(buildBaseFolderUrl("decrypt"),buildEncryptFolderParam(folderId, passPhrase), Method.POST, callBack);
    }

    /**
     * copy file in the server
     * @param id file'id
     * @param dstFolderId folder'id
     * @param callBack
     * @throws JSONException
     */
    public void copyFile(int id, int dstFolderId, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("copy"), buildCopyFileParam(id, dstFolderId), Method.PUT, callBack);
    }

    public void moveFile(int id, int dstFolderId, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("move"), buildMoveFileParam(id, dstFolderId), Method.PUT, callBack);
    }

    public void deleteFile(int id, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("delete"), buildDeleteFileParam(id), Method.DELETE, callBack);
    }

    public void updateFile(int id, String newName, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("update"), buildUpdateFileParam(id, newName), Method.PUT, callBack);
    }

    public void encryptFile(int id, String passPhrase, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("encrypt"), buildEncryptFileParam(id, passPhrase), Method.POST, callBack );
    }

    public void decryptFile(int id, String passPhrase, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("decrypt"), buildDecryptFileParam(id, passPhrase), Method.POST, callBack);
    }

    public void shareFile(int id, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("shareOrCancel"),buildShareFileParam(id), Method.POST,callBack);
    }

    public void cancelSharingFile(int id, final CallBack callBack) throws JSONException {
        buildRequest(buildBaseFileUrl("shareOrCancel/cancel"), buildCancelSharingFileParam(id), Method.POST, callBack);
    }

    public Bitmap getFile(int id) throws JSONException, IOException {
        Request request = new Request.Builder()
                .url(buildBaseFileUrl("%d", id))
                .header("Authorization", String.format("Token token=%s",SecuDiskApplication.Token))
                .get().build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            InputStream inputStream = response.body().byteStream();
            return BitmapFactory.decodeStream(inputStream);
        } else {
            return null;
        }
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

    private String buildCopyFileParam(int id, int dstFolderId) throws JSONException {
        JSONObject file = new JSONObject();
        file.put("id", id);
        file.put("dst_folder_id",dstFolderId);
        JSONObject data = new JSONObject();
        data.put("file", file);

        return data.toString();
    }

    private String buildMoveFileParam(int id, int dstFolderId) throws JSONException {
        return buildCopyFileParam(id, dstFolderId);
    }

    private String buildDeleteFileParam(int id) throws JSONException {
        JSONObject file = new JSONObject();
        file.put("id", id);
        JSONObject data = new JSONObject();
        data.put("file", file);
        return data.toString();
    }

    private String buildEncryptFileParam(int id, String passPhrase) throws JSONException {
        JSONObject file = new JSONObject();
        file.put("id", id);
        file.put("pass_phrase", passPhrase);
        JSONObject data = new JSONObject();
        data.put("file", file);
        return data.toString();
    }

    private String buildDecryptFileParam(int id, String passPhrase) throws JSONException {
        return buildEncryptFileParam(id, passPhrase);
    }

    private String buildShareFileParam(int id) throws JSONException {
        return buildDeleteFileParam(id);
    }

    private String buildCancelSharingFileParam(int id) throws JSONException {
        return buildShareFileParam(id);
    }

    private String buildUpdateFileParam(int id, String newName) throws JSONException {
        JSONObject file = new JSONObject();
        file.put("id", id);
        file.put("new_name", newName);
        JSONObject data = new JSONObject();
        data.put("file", file);
        return data.toString();
    }

    private String buildCreateFolderParam(int fromFolder,String folderName) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_name", folderName);
        folder.put("from_folder", fromFolder);
        JSONObject data = new JSONObject();
        data.put("folder", folder);
        return data.toString();
    }

    private String buildDeleteFolderParam(int folderId) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        JSONObject data = new JSONObject();
        data.put("folder", folder);
        return data.toString();
    }

    private String buildUpdateFolderParam(int folderId, String newName) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        folder.put("new_name",newName);
        JSONObject data = new JSONObject();
        data.put("folder", folder);
        return data.toString();
    }

    private String buildEncryptFolderParam(int folderId, String passPhrase) throws JSONException {
        JSONObject folder = new JSONObject();
        folder.put("folder_id", folderId);
        folder.put("pass_phrase",passPhrase);
        JSONObject data = new JSONObject();
        data.put("folder", folder);
        return data.toString();
    }

    private String buildDecryptFolderParam(int folderId, String passPhrase) throws JSONException {
        return buildEncryptFolderParam(folderId, passPhrase);
    }

    private String buildBaseUploadUrl(String param, int... id) {
        return buildBaseUrl(UPLOAD_BASE_URL, param, id);
    }

    private String buildBaseFileUrl(String param, int... id) {
        return buildBaseUrl(FILE_BASE_URL, param, id);
    }

    private String buildBaseFolderUrl(String param, int... id) {
        return buildBaseUrl(FOLDER_BASE_URL, param, id);
    }

    private String buildBaseUrl(String base, String param, int... id) {
        if (id.length == 0) {
            return String.format(Locale.US, base.concat(param), SecuDiskApplication.IP, SecuDiskApplication.Port);
        }

        return String.format(Locale.US, base.concat(param), SecuDiskApplication.IP, SecuDiskApplication.Port, id[0]);
    }

    public interface GetImageCallBack {
        void onImageLoaded(Bitmap image);
        void onError(@Nullable String msg);
    }
}
