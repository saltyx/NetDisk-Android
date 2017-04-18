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

package com.shiyan.netdisk_android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import java.util.LinkedList;
import java.util.Queue;

public class UploadService extends Service {

    public static final String KEY = "KEY";

    private boolean isRunning = false;

    private Queue<String> mUploadQueue;
    private Thread mUploadThread;

    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context,String path) {
        Intent starter = new Intent(context, UploadService.class);
        starter.putExtra(KEY, path);
        context.startService(starter);
    }

    @Override public void onCreate() {
        super.onCreate();
        mUploadQueue = new LinkedList<>();
        mUploadThread = new Thread(new Runnable() {
            @Override public void run() {
                upload();
                isRunning = false;
            }
        });
        mUploadThread.interrupt();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra(KEY)) return START_NOT_STICKY;
        String path = intent.getStringExtra(KEY);
        if (isRunning) {
            mUploadQueue.add(path);
        }
        if (mUploadThread.isInterrupted() || !mUploadThread.isAlive()) {
            mUploadThread.start();
        }
        return START_STICKY;
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    private void upload() {

    }
}
