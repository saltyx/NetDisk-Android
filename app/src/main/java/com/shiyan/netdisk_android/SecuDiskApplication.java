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

package com.shiyan.netdisk_android;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import java.util.Stack;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class SecuDiskApplication extends Application {

    public static String Token;
    public static String IP;
    public static String Port;
    public static int CurrentFolder;
    private static Stack<Integer> FolderTrack;
    @Override
    public void onCreate() {
        super.onCreate();
        CurrentFolder = 1;//root
        FolderTrack = new Stack<>();
        FolderTrack.push(CurrentFolder);
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        LeakCanary.install(this);
    }

    public static int findPreFolder() {
        if (FolderTrack.isEmpty()) {
            CurrentFolder = 1;
            return 1;//root
        } else {
            FolderTrack.pop();CurrentFolder = FolderTrack.peek();
            return CurrentFolder ;
        }
    }

    public static void goToNext(int id) {
        FolderTrack.push(id);
        CurrentFolder = id;
    }
}
