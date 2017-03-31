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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class SPHelper {

    public final static String IP_KEY = "IP";
    public final static String PORT_KEY = "PORT";
    public final static String TOKEN_KEY = "TOKEN_";
    public final static String IS_FIRST_KEY = "IS_FIRST";

    private final static String KEY = "SecureDisk";

    private volatile static SPHelper sInstance;
    private SharedPreferences mSP;

    private SPHelper(Application context) {
        mSP = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static SPHelper getInstance(Application context) {
        if (sInstance == null) {
            synchronized (SPHelper.class) {
                if (sInstance == null) {
                    sInstance = new SPHelper(context);
                }
            }
        }
        return sInstance;
    }

    public SharedPreferences getSP() {
        return mSP;
    }

}
