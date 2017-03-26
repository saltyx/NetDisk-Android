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

package com.shiyan.netdisk_android.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class UserFile implements Parcelable {

    public static final String KEY_ID = "id";
    public static final String KEY_FILENAME = "filename";
    public static final String KEY_FILESIZE = "filesize";
    public static final String KEY_IS_FOLDER = "isFolder";
    public static final String KEY_FROM_FOLDER = "fromFolder";
    public static final String KEY_IS_ENCRYPTED = "isEncrypted";
    public static final String KEY_IS_SHARED = "isShared";
    public static final String KEY_DOWNLOAD_LINK = "downloadLink";
    public static final String KEY_DOWNLOAD_TIME = "downloadTime";
    public static final String KEY_CREATED_AT = "createAt";
    public static final String KEY_UPDATED_AT = "updateAt";
    public static final String KEY_SHA256 = "sha256";
    public static final String KEY_IV = "iv";

    private int id;
    private String fileName;
    private int fileSize;
    private boolean isFolder;
    private int fromFolder;
    private boolean isShared;
    private boolean isEncrypted;
    private String downloadLink;
    private int downloadTimes;
    private String createAt;
    private String updateAt;
    private String sha256;
    private String iv;

    public UserFile() {

    }

    public UserFile(Parcel in) {
        Bundle bundle = in.readBundle();
        id = bundle.getInt(KEY_ID);
        fileName = bundle.getString(KEY_FILENAME);
        fileSize = bundle.getInt(KEY_FILESIZE);
        isFolder = bundle.getBoolean(KEY_IS_FOLDER);
        fromFolder = bundle.getInt(KEY_FROM_FOLDER);
        isShared = bundle.getBoolean(KEY_IS_SHARED);
        isEncrypted = bundle.getBoolean(KEY_IS_ENCRYPTED);
        downloadLink = bundle.getString(KEY_DOWNLOAD_LINK);
        downloadTimes = bundle.getInt(KEY_DOWNLOAD_TIME);
        createAt = bundle.getString(KEY_CREATED_AT);
        updateAt = bundle.getString(KEY_UPDATED_AT);
        sha256 = bundle.getString(KEY_SHA256);
        iv = bundle.getString(KEY_IV);
    }

    public static final Parcelable.Creator<UserFile> CREATOR = new Parcelable.Creator<UserFile>(){
        @Override
        public UserFile createFromParcel(Parcel source) {
            return new UserFile(source);
        }

        @Override
        public UserFile[] newArray(int size) {
            return new UserFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        bundle.putString(KEY_FILENAME, fileName);
        bundle.putInt(KEY_FILESIZE, fileSize);
        bundle.putBoolean(KEY_IS_FOLDER, isFolder);
        bundle.putInt(KEY_FROM_FOLDER, fromFolder);
        bundle.putBoolean(KEY_IS_ENCRYPTED, isEncrypted);
        bundle.putBoolean(KEY_IS_SHARED, isShared);
        bundle.putString(KEY_DOWNLOAD_LINK, downloadLink);
        bundle.putInt(KEY_DOWNLOAD_TIME, downloadTimes);
        bundle.putString(KEY_CREATED_AT, createAt);
        bundle.putString(KEY_UPDATED_AT, updateAt);
        bundle.putString(KEY_SHA256, sha256);
        bundle.putString(KEY_IV, iv);
        dest.writeBundle(bundle);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public int getFromFolder() {
        return fromFolder;
    }

    public void setFromFolder(int fromFolder) {
        this.fromFolder = fromFolder;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public int getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(int downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
