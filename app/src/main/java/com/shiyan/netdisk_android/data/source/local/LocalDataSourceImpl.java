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
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.model.UserFile;

import java.util.ArrayList;
import java.util.List;

import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_CREATE_AT;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_DOWNLOAD_LINK;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_DOWNLOAD_TIMES;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_FILE_SIZE;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_FROM_FOLDER;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_ID;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_ENCRYPTED;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_FOLDER;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_SHARED;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_IV;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_NAME;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_SHA256;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.COLUMN_NAME_UPDATE_AT;
import static com.shiyan.netdisk_android.data.source.local.DataPersistenceContract.FilesEntry.TABLE_NAME;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class LocalDataSourceImpl implements DataSource {

    private static volatile LocalDataSourceImpl sInstance;
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
        buildCreateAction(file, callBack);
    }

    @Override public void deleteFolder(UserFile file, ResultCallBack callBack) {
        buildDeleteAction(file, callBack);
    }

    @Override public void updateFolder(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void encryptFolder(UserFile file,String passPhrase, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void decryptFolder(UserFile file,String passPhrase, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @SuppressWarnings("unchecked")
    @Override public void getFolder(int id, GetData callback) {
        ArrayList<UserFile> files = buildSelectAction(id);
        if (files == null || files.size() == 0) {
            callback.onDataNotAvailable("empty");
        } else {
            callback.onLoaded(files.get(0));
        }
    }

    @SuppressWarnings("unchecked")
    @Override public void getFilesByFolder(int id, LoadData callback) {
        ArrayList<UserFile> files = buildSelectAction(id);
        if (files == null || files.size() == 0) {
            callback.onDataNotAvailable("empty");
        } else {
            callback.onLoaded(files);
        }
    }

    @Override public void createFile(UserFile file, ResultCallBack callBack) {
        buildCreateAction(file, callBack);
    }

    @Override public void encryptFile(UserFile file,String passPhrase ,ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void decryptFile(UserFile file, String passPhrase, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    /**
     * similar with the creation
     * @param file the file
     * @param callBack callback
     */
    @Override public void copyFile(UserFile file, ResultCallBack callBack) {
        buildCreateAction(file, callBack);
    }

    @Override public void deleteFiles(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    /**
     * similar with the update
     * @param file the file
     * @param callBack callback
     */
    @Override public void moveFile(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void updateFile(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void shareFile(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    @Override public void cancelShare(UserFile file, ResultCallBack callBack) {
        buildUpdateAction(file, callBack);
    }

    private ArrayList<UserFile> buildSelectAction(int id) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String [] projection = {
                COLUMN_NAME_ID                  ,
                COLUMN_NAME_NAME                ,
                COLUMN_NAME_FILE_SIZE           ,
                COLUMN_NAME_IS_FOLDER           ,
                COLUMN_NAME_FROM_FOLDER         ,
                COLUMN_NAME_IS_SHARED           ,
                COLUMN_NAME_IS_ENCRYPTED        ,
                COLUMN_NAME_DOWNLOAD_LINK       ,
                COLUMN_NAME_DOWNLOAD_TIMES      ,
                COLUMN_NAME_CREATE_AT           ,
                COLUMN_NAME_UPDATE_AT           ,
                COLUMN_NAME_IV                  ,
                COLUMN_NAME_SHA256              ,
        };

        Cursor c = db.query(TABLE_NAME, projection,null,null,null,null,null);

        if (c== null || c.getCount() <= 0) return null;

        ArrayList<UserFile> files = new ArrayList<>();

        while (c.moveToNext()) {
            int cId = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ID));
            String cName = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_NAME));
            long cFileSize = c.getLong(c.getColumnIndexOrThrow(COLUMN_NAME_FILE_SIZE));
            boolean cIsFolder = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_IS_FOLDER)) > 0;
            int cFromFolder = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_FROM_FOLDER));
            boolean cIsShared = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_IS_SHARED)) > 0;
            boolean cIsEncrypted = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_IS_ENCRYPTED)) > 0;
            String cDownloadLink = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_DOWNLOAD_LINK));
            int cDownloadTimes = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_DOWNLOAD_TIMES));
            String cCreateAt = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_CREATE_AT));
            String cUpdateAt = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_UPDATE_AT));
            String cIV = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_IV));
            String cSha256 = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_SHA256));
            files.add(
                    new UserFile(cId, cName, cFileSize,cIsFolder,cFromFolder,
                            cIsShared, cIsEncrypted,cDownloadLink, cDownloadTimes,
                            cCreateAt, cUpdateAt, cSha256, cIV, null,null));
        }

        c.close();
        db.close();

        return files;

    }

    private void buildUpdateAction(UserFile file, ResultCallBack callBack) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String where = " id = ? ";
            String[] args = {String.valueOf(file.getId())};
            db.update(TABLE_NAME, buildContentValues(file), where, args);
            db.setTransactionSuccessful();
            callBack.onSuccess(null);
        } catch (SQLException e) {
            callBack.onError(e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void buildDeleteAction(UserFile file, ResultCallBack callBack) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String where = " id = ? ";
            String[] args = {String.valueOf(file.getId())};
            db.delete(TABLE_NAME, where, args);
            db.setTransactionSuccessful();
            callBack.onSuccess(null);
        } catch (SQLException e) {
            callBack.onError(e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void buildCreateAction(UserFile file, ResultCallBack callBack) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try{
            db.beginTransaction();
            db.insertOrThrow(TABLE_NAME, null,buildContentValues(file));
            db.setTransactionSuccessful();
            callBack.onSuccess(null);
        } catch (SQLException e) {
            callBack.onError(e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues buildContentValues(UserFile file) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, file.getId());
        values.put(COLUMN_NAME_NAME, file.getFileName());
        values.put(COLUMN_NAME_FILE_SIZE, file.getFileSize());
        values.put(COLUMN_NAME_IS_FOLDER, file.isFolder());
        values.put(COLUMN_NAME_FROM_FOLDER, file.getFromFolder());
        values.put(COLUMN_NAME_IS_SHARED, file.isShared());
        values.put(COLUMN_NAME_IS_ENCRYPTED, file.isEncrypted());
        values.put(COLUMN_NAME_DOWNLOAD_LINK, file.getDownloadLink());
        values.put(COLUMN_NAME_DOWNLOAD_TIMES, file.getDownloadTimes());
        values.put(COLUMN_NAME_CREATE_AT, file.getCreateAt());
        values.put(COLUMN_NAME_UPDATE_AT, file.getUpdateAt());
        values.put(COLUMN_NAME_IV, file.getIv());
        values.put(COLUMN_NAME_SHA256, file.getSha256());
        return values;
    }
}
