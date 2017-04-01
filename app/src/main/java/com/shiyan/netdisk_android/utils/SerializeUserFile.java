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

package com.shiyan.netdisk_android.utils;

import android.util.Log;

import com.shiyan.netdisk_android.model.UserFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class SerializeUserFile {

    public static List<UserFile> serialize(String data) throws JSONException {
        List<UserFile> list = new ArrayList<>();
        JSONArray array = new JSONArray(data);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            int id = obj.getInt("id");
            String fileName = obj.getString("file_name");
            int fileSize = obj.get("file_size") == null ? 0 : obj.getInt("file_size");
            boolean isFolder = obj.getBoolean("is_folder");
            int fromFolder = obj.getInt("from_folder");
            boolean isShared = obj.getBoolean("is_shared");
            boolean isEncrypted = obj.getBoolean("is_encrypted");
            String downloadLink = obj.getString("download_link");
            int downloadTimes = obj.getInt("download_times");
            String createAt = obj.getString("created_at");
            String updateAt = obj.getString("updated_at");
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

    public static List<UserFile> serializeFolder(String data) throws JSONException {
        List<UserFile> list = serialize(data);
        List<UserFile> result = new ArrayList<>();
        for (UserFile file:list) {
            if (file.isFolder()) {
                result.add(file);
            }
        }
        list.clear();
        return result;
    }

    public static List<UserFile> serializeFolder(List<UserFile> list) {
        List<UserFile> result = new ArrayList<>();
        for (UserFile file:list) {
            if (file.isFolder()) {
                result.add(file);
            }
        }
        return result;
    }

    public static List<UserFile> serializeFile(String data) throws JSONException {
        List<UserFile> list = serialize(data);
        List<UserFile> result = new ArrayList<>();
        for (UserFile file: list) {
            if (!file.isFolder()) {
                result.add(file);
            }
        }
        list.clear();
        return result;
    }

    public static List<UserFile> serializeFile(List<UserFile> list) {
        List<UserFile> result = new ArrayList<>();
        for (UserFile file:list) {
            if (!file.isFolder()) {
                result.add(file);
            }
        }
        return result;
    }
}
