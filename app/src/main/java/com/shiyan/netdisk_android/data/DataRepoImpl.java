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

/**
 * the current class is primarily intended to
 * handle data logic
 *
 * @author shiyan
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class DataRepoImpl implements DataSource {

    static DataRepoImpl INSTANCE;

    final LocalDataSourceImpl localDataSource;
    final RemoteDataSourceImpl remoteDataSource;

    private DataRepoImpl(final LocalDataSourceImpl localDataSource,final RemoteDataSourceImpl remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static DataRepoImpl getInstance(final LocalDataSourceImpl localDataSource,final RemoteDataSourceImpl remoteDataSource) {
        if (INSTANCE == null) {
            synchronized (DataRepoImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepoImpl(localDataSource, remoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    /**
     *
     * @param folderName folder's name
     * @param fromFolder
     * @param callBack
     */
    @Override
    public void createFolder(String folderName, int fromFolder, ResultCallBack callBack) {
        remoteDataSource.createFolder(folderName, fromFolder, callBack);
    }

    @Override
    public void deleteFolder(int id, ResultCallBack callBack) {
        remoteDataSource.deleteFolder(id, callBack);
    }

    /**
     * Update the folder's name
     * @param id folder's id
     * @param newName new filename
     * @param callBack callback when the server responses
     */
    @Override
    public void updateFolder(int id, String newName, ResultCallBack callBack) {
        remoteDataSource.updateFolder(id, newName, callBack);
    }

    /**
     * Encrypt all the files and folders in the folder
     * @param id folder's id
     * @param passPhrase password
     * @param callBack callback
     */
    @Override
    public void encryptFolder(int id, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.encryptFolder(id, passPhrase, callBack);
    }

    @Override
    public void decryptFolder(int id, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.decryptFolder(id, passPhrase, callBack);
    }

    /**
     * Get the folder's information
     * @param id folder's id
     * @param callback when the data is ready, callback
     */
    @Override
    public void getFolder(int id, GetData callback) {
        remoteDataSource.getFolder(id, callback);
    }

    /**
     * Get all the folders and files in the folder
     * @param id folder's id
     * @param callback when the data is loaded, callback the json string
     */
    @Override
    public void getFilesByFolder(int id, GetData callback) {
        remoteDataSource.getFilesByFolder(id, callback);
    }

    /**
     * Encrypt the file in your server
     * @param id the file's id
     * @param passPhrase password
     * @param callBack when the encryption is done, callback
     */
    @Override
    public void encryptFile(int id, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.encryptFile(id, passPhrase, callBack);
    }

    /**
     * Decrypted the file in your server
     * @param id the file's id
     * @param passPhrase password
     * @param callBack when the decryption is done, callback
     */
    @Override
    public void decryptFile(int id, String passPhrase, ResultCallBack callBack) {
        remoteDataSource.decryptFile(id, passPhrase, callBack);
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
        remoteDataSource.updateFile(id, newName, callBack);
    }

    @Override
    public void shareFile(int id, ResultCallBack callBack) {
        remoteDataSource.shareFile(id, callBack);
    }

    @Override
    public void cancelShare(int id, ResultCallBack callBack) {
        remoteDataSource.cancelShare(id, callBack);
    }
}
