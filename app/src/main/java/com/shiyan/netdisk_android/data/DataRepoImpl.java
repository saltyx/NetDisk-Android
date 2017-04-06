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

import com.shiyan.netdisk_android.data.source.local.LocalDataSourceImpl;
import com.shiyan.netdisk_android.data.source.remote.RemoteDataSourceImpl;
import com.shiyan.netdisk_android.model.UserFile;

/**
 * the current class is primarily intended to
 * handle data logic
 *
 * @author shiyan
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class DataRepoImpl implements DataSource {

    static volatile DataRepoImpl sInstance;

    private final LocalDataSourceImpl localDataSource;
    private final RemoteDataSourceImpl remoteDataSource;

    private DataRepoImpl(final LocalDataSourceImpl localDataSource,final RemoteDataSourceImpl remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static DataRepoImpl getInstance(final LocalDataSourceImpl localDataSource,final RemoteDataSourceImpl remoteDataSource) {
        if (sInstance == null) {
            synchronized (DataRepoImpl.class) {
                if (sInstance == null) {
                    sInstance = new DataRepoImpl(localDataSource, remoteDataSource);
                }
            }
        }
        return sInstance;
    }

    /**
     *
     * @param file the file
     * @param callBack callback when the file created
     */
    @Override public void createFolder(UserFile file, ResultCallBack callBack) {
        remoteDataSource.createFolder(file, callBack);
    }

    @Override public void deleteFolder(UserFile file, ResultCallBack callBack) {
        remoteDataSource.deleteFolder(file, callBack);
    }

    /**
     * Update the folder's name
     * @param file folder's id
     * @param callBack callback when the server responses
     */
    @Override public void updateFolder(UserFile file, ResultCallBack callBack) {
        remoteDataSource.updateFolder(file, callBack);
    }

    /**
     * Encrypt all the files and folders in the folder
     * @param file folder's id
     * @param passPhrase password
     * @param callBack callback
     */
    @Override public void encryptFolder(UserFile file, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.encryptFolder(file, passPhrase, callBack);
    }

    @Override public void decryptFolder(UserFile file, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.decryptFolder(file, passPhrase, callBack);
    }

    /**
     * Get the folder's information
     * @param id folder's id
     * @param callback when the data is ready, callback
     */
    @Override public void getFolder(int id, GetData callback) {
        remoteDataSource.getFolder(id, callback);
    }

    /**
     * Get all the folders and files in the folder
     * @param id folder's id
     * @param callback when the data is loaded, callback the json string
     */
    @SuppressWarnings("unchecked")
    @Override public void getFilesByFolder(int id, LoadData callback) {
        remoteDataSource.getFilesByFolder(id, callback);
    }

    /**
     * Encrypt the file in your server
     * @param file the file's id
     * @param passPhrase password
     * @param callBack when the encryption is done, callback
     */
    @Override public void encryptFile(UserFile file, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.encryptFile(file, passPhrase, callBack);
    }

    /**
     * Decrypted the file in your server
     * @param file the file's id
     * @param passPhrase password
     * @param callBack when the decryption is done, callback
     */
    @Override public void decryptFile(UserFile file, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.decryptFile(file, passPhrase, callBack);
    }

    @Override public void copyFile(UserFile file, ResultCallBack callBack) {

    }

    @Override public void deleteFiles(UserFile file, ResultCallBack callBack) {

    }

    @Override public void moveFile(UserFile file, ResultCallBack callBack) {
        remoteDataSource.moveFile(file, callBack);
    }

    @Override public void updateFile(UserFile file, ResultCallBack callBack) {
        remoteDataSource.updateFile(file, callBack);
    }

    @Override public void shareFile(UserFile file, ResultCallBack callBack) {
        remoteDataSource.shareFile(file, callBack);
    }

    @Override public void cancelShare(UserFile file, ResultCallBack callBack) {
        remoteDataSource.cancelShare(file, callBack);
    }

    @Override public void createFile(UserFile file, ResultCallBack callBack) {
        remoteDataSource.createFile(file, callBack);
    }
}
