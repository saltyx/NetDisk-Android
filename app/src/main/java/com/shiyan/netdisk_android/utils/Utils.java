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

import com.shiyan.netdisk_android.SecuDiskApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class Utils {
    private static int FILE_NAME_MAX_LEN = 15;
    private static String[] IMAGE_SUFFIX = {"jpg","png","jpeg","bmp"};
    /**
     * get current tile
     * @return time
     */
    public static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String calculateFileSize(long size) {
        double kb =size/1024;
        double mb = kb/1024;
        double gb = mb/1024;
        if (mb < 1) return String.valueOf(kb).concat("KB");
        if (gb < 1) return String.valueOf(mb).concat("MB");
        return String.valueOf(gb).concat("GB");

    }

    public static boolean isImage(String path) {
        String[] params = path.split("\\.");
        if (params.length == 1) return false;
        for (String str:IMAGE_SUFFIX) {
            if (params[params.length-1].contentEquals(str)) return true;
        }
        return false;
    }

    public static String getFileType(String filename) {
        String[] params = filename.split("\\.");
        if (params.length == 1) return "file";
        return params[params.length-1];
    }

    public static String getProperLengthFileName(String filename) {
        if (filename.length() <= FILE_NAME_MAX_LEN ) return filename;
        return filename.substring(0, FILE_NAME_MAX_LEN).concat("...");

    }

    public static String buildBaseFileUrl(String param, int... id) {
        return String.format(Locale.US, "http://%s:%s/v1/file/".concat(param), SecuDiskApplication.IP, SecuDiskApplication.Port, id[0]);
    }
}
