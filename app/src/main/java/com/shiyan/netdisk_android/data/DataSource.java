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

package com.shiyan.netdisk_android.data;

import android.support.annotation.Nullable;

import com.shiyan.netdisk_android.model.UserFile;

import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public interface DataSource {

    String JSON_EXCEPTION = "JSON_EXCEPTION";

    interface GetData<T> {
        void onLoaded(T data);
        void onDataNotAvailable(@Nullable String msg);
    }

    interface ResultCallBack {
        void onSuccess(@Nullable String success);
        void onError(@Nullable String error);
    }

    void createFolder(UserFile file, ResultCallBack callBack);
    void deleteFolder(UserFile file, ResultCallBack callBack);
    void updateFolder(UserFile file, ResultCallBack callBack);
    void encryptFolder(UserFile file,String passPhrase, ResultCallBack callBack);
    void decryptFolder(UserFile file,String passPhrase, ResultCallBack callBack);
    void getFolder(int id, GetData callback);
    void getFilesByFolder(int id, GetData callback);

    void createFile(UserFile file, ResultCallBack callBack);
    void encryptFile(UserFile file,String passPhrase, ResultCallBack callBack);
    void decryptFile(UserFile file,String passPhrase, ResultCallBack callBack);
    void copyFile(UserFile file, ResultCallBack callBack);//相当于新建file
    void deleteFiles(UserFile file, ResultCallBack callBack);
    void moveFile(UserFile file, ResultCallBack callBack);//相当于更新file
    void updateFile(UserFile file, ResultCallBack callBack);
    void shareFile(UserFile file, ResultCallBack callBack);
    void cancelShare(UserFile file, ResultCallBack callBack);

}
