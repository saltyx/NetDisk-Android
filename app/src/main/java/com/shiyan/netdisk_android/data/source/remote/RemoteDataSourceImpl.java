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
import android.util.Log;

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
    
    final String TAG = getClass().getName();
    static RemoteDataSourceImpl INSTANCE;
    final NetHelper netHelper ;

    private RemoteDataSourceImpl() {
        netHelper = NetHelper.getInstance() ;
    }

    public static RemoteDataSourceImpl getInstance() {
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

    /**
     * Get files by folder from the server
     * @param id folder's id
     * @param callback when data is ready, callback the json string
     */
    @Override
    public void getFilesByFolder(int id, final GetData callback) {
        netHelper.getFilesByFolder(id, new CallBack() {
            @Override
            public void success(@NonNull String data) {
                try {
                    Log.i(TAG, data);
                    JSONObject obj = new JSONObject(data);
                    String info = obj.getString("info");
                    callback.onLoaded(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable(e.toString());
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

}
