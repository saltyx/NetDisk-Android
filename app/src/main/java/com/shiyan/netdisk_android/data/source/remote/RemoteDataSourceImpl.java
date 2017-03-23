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

package com.shiyan.netdisk_android.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.CallBack;
import com.shiyan.netdisk_android.utils.NetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class RemoteDataSourceImpl implements DataSource {

    static RemoteDataSourceImpl INSTANCE;
    final NetHelper netHelper ;

    private RemoteDataSourceImpl() {
        netHelper = NetHelper.getInstance() ;
    }

    public RemoteDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void createFolder(String folderName, int fromFolder, ResultCallBack callBack) {

    }

    @Override
    public void deleteFolder(int id, ResultCallBack callBack) {

    }

    @Override
    public void updateFolder(int id, String newName, ResultCallBack callBack) {

    }

    @Override
    public void encryptFolder(int id, String passPhrase, ResultCallBack callBack) {

    }

    @Override
    public void decryptFolder(int id, String passPhrase, ResultCallBack callBack) {

    }

    @Override
    public void getFolder(int id, GetData callback) {

    }

    @Override
    public void getFilesByFolder(int id, final LoadData callback) {
        netHelper.getFilesByFolder(id, new CallBack() {
            @Override
            public void success(@NonNull String data) {
                List list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    String array = obj.getString("info");
                    callback.onLoaded(list = serializeData(array));
                } catch (JSONException e) {
                    callback.onDataNotAvailable(e.toString());
                } finally {
                    if (list != null) {
                        list.clear();
                    }
                }
            }

            @Override
            public void error(@Nullable String error) {
                callback.onDataNotAvailable(error);
            }
        });
    }

    @Override
    public void encryptFile(int id, String passPhrase, ResultCallBack callBack) {

    }

    @Override
    public void decryptFile(int id, String passPhrase, ResultCallBack callBack) {

    }

    @Override
    public void copyFile(int id, int dstFolder, ResultCallBack callBack) {

    }

    @Override
    public void deleteFiles(int id, ResultCallBack callBack) {

    }

    @Override
    public void moveFile(int id, int dstFolder, ResultCallBack callBack) {

    }

    @Override
    public void updateFile(int id, String newName, ResultCallBack callBack) {

    }

    @Override
    public void shareFile(int id, ResultCallBack callBack) {

    }

    @Override
    public void cancelShare(int id, ResultCallBack callBack) {

    }

    private List<UserFile> serializeData(String data) throws JSONException {
        List<UserFile> list = new ArrayList<>();
        JSONArray array = new JSONArray(data);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            int id = obj.getInt("id");
            String fileName = obj.getString("file_name");
            int fileSize = obj.getInt("file_size");
            boolean isFolder = obj.getBoolean("is_folder");
            int fromFolder = obj.getInt("from_folder");
            boolean isShared = obj.getBoolean("is_shared");
            boolean isEncrypted = obj.getBoolean("is_encrypted");
            String downloadLink = obj.getString("download_link");
            int downloadTimes = obj.getInt("download_times");
            String createAt = obj.getString("create_at");
            String updateAt = obj.getString("update_at");
            String iv = obj.getString("iv");
            String sha256 = obj.getString("sha256");

            UserFile file = new UserFile();
            file.setId(id);
            file.setFileName(fileName);
            file.setFileSize(fileSize);
            file.setFolder(isFolder);
            file.setFromFolder(fromFolder);
            file.setShared(isShared);
            file.setEncrypted(isEncrypted);
            file.setDownloadLink(downloadLink);
            file.setDownloadTimes(downloadTimes);
            file.setCreateAt(createAt);
            file.setUpdateAt(updateAt);
            file.setIv(iv);
            file.setSha256(sha256);
            list.add(file);
        }
        return list;
    }
}
