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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "NetDisk.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String BOOL_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_FILES =
            "CREATE TABLE " + DataPersistenceContract.FilesEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_FILE_SIZE + INT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_SHARED + BOOL_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_FOLDER + BOOL_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_IS_ENCRYPTED + BOOL_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_FROM_FOLDER + INT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_DOWNLOAD_LINK + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_DOWNLOAD_TIMES + INT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_CREATE_AT + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.FilesEntry.COLUMN_NAME_UPDATE_AT + TEXT_TYPE +
                    " )";

    public DBHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
