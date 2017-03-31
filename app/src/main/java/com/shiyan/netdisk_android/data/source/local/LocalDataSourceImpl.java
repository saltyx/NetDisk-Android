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

package com.shiyan.netdisk_android.data.source.local;

import android.app.Application;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.model.UserFile;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class LocalDataSourceImpl implements DataSource {

    static volatile LocalDataSourceImpl sInstance;
    private DBHelper mDBHelper;

    private LocalDataSourceImpl(Application application) {
        mDBHelper = new DBHelper(application);
    }

    public static LocalDataSourceImpl getInstance(Application application) {
        if (sInstance == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (sInstance == null) {
                    sInstance = new LocalDataSourceImpl(application);
                }
            }
        }
        return sInstance;
    }

    @Override public void createFolder(UserFile file, ResultCallBack callBack) {

    }

    @Override public void deleteFolder(UserFile file, ResultCallBack callBack) {

    }

    @Override public void updateFolder(UserFile file, ResultCallBack callBack) {

    }

    @Override public void encryptFolder(UserFile file,String passPhrase, ResultCallBack callBack) {

    }

    @Override public void decryptFolder(UserFile file,String passPhrase, ResultCallBack callBack) {

    }

    @Override public void getFolder(int id, GetData callback) {

    }

    @Override public void getFilesByFolder(int id, GetData callback) {

    }

    @Override public void createFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void encryptFile(UserFile file,String passPhrase ,ResultCallBack callBack) {

    }

    @Override public void decryptFile(UserFile file, String passPhrase, ResultCallBack callBack) {

    }

    @Override public void copyFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void deleteFiles(UserFile file, ResultCallBack callBack) {

    }

    @Override public void moveFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void updateFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void shareFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void cancelShare(UserFile file, ResultCallBack callBack) {

    }


}
