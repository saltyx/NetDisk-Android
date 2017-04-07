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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v7.app.NotificationCompat;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.Utils;

import java.io.File;

public class DownloadService extends Service {

    public static final String KEY = "KEY";
    public static final int NOTIFICATION_ID = 0x0001;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private int mDownloadID = -1;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context, UserFile file) {
        Intent starter = new Intent(context, DownloadService.class);
        starter.putExtra(KEY, file);
        context.startService(starter);
    }

    @Override public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this);
        mNotificationBuilder.setContentText("Pending");
        mNotificationBuilder.setSmallIcon(R.drawable.ic_archive_black_24dp);
        Intent intentPause = new Intent(this,DownloadService.class);
        PendingIntent pendingIntentPause = PendingIntent.getService(this, (int)System.currentTimeMillis(),
                intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder.addAction(R.drawable.ic_archive_black_24dp,"pause",pendingIntentPause);

    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra(KEY)){
            FileDownloader.getImpl().pause(mDownloadID);
            mNotificationManager.cancel(NOTIFICATION_ID);
            return START_NOT_STICKY;
        }
        UserFile file = intent.getParcelableExtra(KEY);
        if (file == null) {
            FileDownloader.getImpl().pause(mDownloadID);
            mNotificationManager.cancel(NOTIFICATION_ID);
            return START_NOT_STICKY;
        }

        mDownloadID = FileDownloader.getImpl().create(Utils.buildBaseFileUrl("%d",file.getId()))
                .addHeader("Authorization", String.format("Token token=%s", SecuDiskApplication.Token))
                .setPath(Environment.getExternalStorageDirectory().toString().concat(File.separator)
                        , true)
                .setListener(new FileDownloadListener() {
                    @Override protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        mNotificationBuilder.setContentTitle("Pending").setProgress(totalBytes, soFarBytes,false);
                        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());

                    }

                    @Override protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        mNotificationBuilder.setContentTitle("Downloading").setProgress(totalBytes, soFarBytes,false);
                        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());

                    }

                    @Override protected void completed(BaseDownloadTask task) {
                        mDownloadID = -1;
                        mNotificationBuilder.setContentTitle("Completed")
                                .setContentText(task.getFilename().concat(" downloaded")).setProgress(100,100,false);
                        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
                    }

                    @Override protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override protected void error(BaseDownloadTask task, Throwable e) {
                        e.printStackTrace();
                    }

                    @Override protected void warn(BaseDownloadTask task) {

                    }

                    @Override protected void started(BaseDownloadTask task) {
                        super.started(task);

                    }
                }).start();
        return START_STICKY;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (mDownloadID != -1) {
            FileDownloader.getImpl().pause(mDownloadID);
        }
    }
}
