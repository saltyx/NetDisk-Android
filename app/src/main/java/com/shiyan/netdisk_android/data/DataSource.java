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

import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public interface DataSource {

    interface LoadData<T> {
        void onLoaded(List<T> data);
        void onDataNotAvailable(@Nullable String msg);
    }

    interface GetData<T> {
        void onLoaded(T data);
        void onDataNotAvailable(@Nullable String msg);
    }

    interface ResultCallBack {
        void onSuccess(@Nullable String success);
        void onError(@Nullable String error);
    }

    void createFolder(String folderName, int fromFolder, ResultCallBack callBack);
    void deleteFolder(int id, ResultCallBack callBack);
    void updateFolder(int id, String newName, ResultCallBack callBack);
    void encryptFolder(int id, String passPhrase, ResultCallBack callBack);
    void decryptFolder(int id, String passPhrase, ResultCallBack callBack);
    void getFolder(int id, GetData callback);
    void getFilesByFolder(int id, GetData callback);

    void encryptFile(int id, String passPhrase, ResultCallBack callBack);
    void decryptFile(int id, String passPhrase, ResultCallBack callBack);
    void copyFile(int id, int dstFolder, ResultCallBack callBack);
    void deleteFiles(int id, ResultCallBack callBack);
    void moveFile(int id, int dstFolder, ResultCallBack callBack);
    void updateFile(int id, String newName, ResultCallBack callBack);
    void shareFile(int id, ResultCallBack callBack);
    void cancelShare(int id, ResultCallBack callBack);

}
