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

package com.shiyan.netdisk_android.detailed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.marvinlabs.intents.MediaIntents;
import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.data.DataRepoImpl;
import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.dialog.DetailInfoDialogFragment;
import com.shiyan.netdisk_android.dialog.WithOneInputDialogFragment;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.service.DownloadService;
import com.shiyan.netdisk_android.utils.ImageLoader;
import com.shiyan.netdisk_android.utils.Inject;
import com.shiyan.netdisk_android.utils.NetHelper;
import com.shiyan.netdisk_android.utils.SerializeServerBack;
import com.shiyan.netdisk_android.utils.Utils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailedActivity extends AppCompatActivity {

    public static final int FEED_BACK = 0x0001;
    public static final int NOTIFICATION_ID = 0xF001;
    public static final String KEY_USER = "USER_KEY";
    public static final int REQUEST_CODE = 1;
    public final String TAG = getClass().getName();

    private DataRepoImpl mDB;
    private NetHelper mNetHelper;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    private int mDownloadId = -1;
    @BindView(R.id.title) TextView mTitleView;
    @BindView(R.id.file_image) ImageView mFileImageView;
    @BindView(R.id.name) TextView mFileNameTextView;
    @BindView(R.id.type) TextView mFileTypeTextView;
    @BindView(R.id.size) TextView mFileSizeTextView;
    @BindView(R.id.create_time) TextView mFileCreateTimeTextView;
    @BindView(R.id.update_time) TextView mFileUpdateTimeTextView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @OnClick(R.id.rename) void onRenameClick() {
        final WithOneInputDialogFragment dialogFragment = WithOneInputDialogFragment.newInstance(mFile);
        dialogFragment.setTitle("Rename ".concat(Utils.getProperLengthFileName(mFile.getFileName())))
                .setCallBack(new WithOneInputDialogFragment.CallBack() {
                    @Override public void onOkClick(String text) {
                        mFile.setFileName(text);
                        mDB.updateFile(mFile, new DataSource.ResultCallBack() {
                            @Override public void onSuccess(@Nullable String success) {
                                Message msg = Message.obtain();
                                msg.arg1 = FEED_BACK;
                                Bundle bundle = new Bundle();
                                bundle.putString(KEY_USER, "success");
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                                dialogFragment.dismiss();
                            }

                            @Override public void onError(@Nullable String error) {
                                Message msg = Message.obtain();
                                msg.arg1 = FEED_BACK;
                                Bundle bundle = new Bundle();
                                bundle.putString(KEY_USER, "success");
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                                dialogFragment.dismiss();
                            }
                        });
                    }

                    @Override public void onCancelClick() {
                        dialogFragment.dismiss();
                    }
                });
        dialogFragment.show(getFragmentManager(), TAG);
    }

    @OnClick(R.id.move) void onMoveClick() {
        Intent intent = new Intent(this, ChooseFolderActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @OnClick(R.id.save) void onSaveClick() {
        DownloadService.start(this,mFile);
    }

    @OnClick(R.id.delete) void onDeleteClick() {
        mDB.deleteFiles(mFile, new DataSource.ResultCallBack() {
            @Override public void onSuccess(@Nullable String success) {
                Message msg = Message.obtain();
                msg.arg1 = FEED_BACK;
                Bundle bundle = new Bundle();
                bundle.putString(KEY_USER, "success");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                finish();
            }

            @Override public void onError(@Nullable String error) {
                Message msg = Message.obtain();
                msg.arg1 = FEED_BACK;
                Bundle bundle = new Bundle();
                bundle.putString(KEY_USER, error);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
    }

    @OnClick(R.id.more) void onMoreClick() {
        final DetailInfoDialogFragment dialogFragment = DetailInfoDialogFragment.newInstance(mFile);
        dialogFragment.setCallBack(new DetailInfoDialogFragment.OnMoreCallBack() {
            @Override public void onDeletedClick(UserFile file) {

            }

            @Override public void onRenameClick(UserFile file) {

            }

            @Override public void onEncryptOrDecryptClick(final UserFile file) {
                final WithOneInputDialogFragment dialogFragment = WithOneInputDialogFragment.newInstance(mFile);
                String title = "Password for encryption";
                if (file.isEncrypted()) {
                    title = "Password for decryption";
                }
                final DataSource.ResultCallBack callBack = new DataSource.ResultCallBack() {
                    @Override public void onSuccess(@Nullable String success) {
                        Message msg = Message.obtain();
                        msg.arg1 = FEED_BACK;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_USER, "success");
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                        dialogFragment.dismiss();
                    }

                    @Override public void onError(@Nullable String error) {
                        Message msg = Message.obtain();
                        msg.arg1 = FEED_BACK;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_USER,error);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                        dialogFragment.dismiss();
                    }
                };
                dialogFragment.setTitle(title)
                        .setCallBack(new WithOneInputDialogFragment.CallBack() {
                            @Override public void onOkClick(String text) {
                                if (file.isEncrypted()) {
                                    mDB.decryptFile(file,text,callBack);
                                } else {
                                    mDB.encryptFile(file,text,callBack);
                                }

                            }

                            @Override public void onCancelClick() {
                                dialogFragment.dismiss();
                            }
                        });
                dialogFragment.show(getFragmentManager(), TAG);
            }

            @Override public void onShareClick(final UserFile file) {
                mDB.shareFile(file, new DataSource.ResultCallBack() {
                    @Override public void onSuccess(@Nullable String success) {
                        try {
                            if (200 == SerializeServerBack.getSuccessResponseInt(success)) {
                                //share it
                                Intent shareLinkIntent = new Intent();
                                shareLinkIntent.setAction(Intent.ACTION_SEND);
                                shareLinkIntent.putExtra(Intent.EXTRA_TEXT, Utils.buildSharedFileUrl(file.getId()));
                                shareLinkIntent.setType("text/plain");
                                startActivity(Intent.createChooser(shareLinkIntent,"Share the link"));
                            } else {

                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override public void onError(@Nullable String error) {

                    }
                });
            }
        }).hideOption(DetailInfoDialogFragment.RENAME,DetailInfoDialogFragment.DELETE)
                .show(getFragmentManager(), TAG);
    }

    private UserFile mFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        setupBaseObj();
        initialUI();
    }

    public void setupBaseObj() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this);
        mNotificationBuilder.setContentText("Pending");
        mNotificationBuilder.setSmallIcon(R.drawable.ic_archive_black_24dp);

        mDB = Inject.provideDataRepo(getApplication());

    }

    public void initialUI() {
        Intent intent = getIntent();
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        mFile = bundle.getParcelable(KEY_USER);
        if (mFile == null) return;
        mTitleView.setText(mFile.getFileName());
        mFileNameTextView.setText(Utils.getProperLengthFileName(mFile.getFileName()));
        mFileTypeTextView.setText(Utils.getFileType(mFile.getFileName()));
        mFileSizeTextView.setText(Utils.calculateFileSize(mFile.getFileSize()));
        mFileCreateTimeTextView.setText(mFile.getCreateAt());
        mFileUpdateTimeTextView.setText(mFile.getUpdateAt());
        if (Utils.isImage(mFile.getFileName())) {
            try {
                mFileImageView.setImageBitmap(
                        ImageLoader.getInstance().getImage(mFile.getId()));
            } catch (Exception e) {
                mFileImageView.setImageResource(R.drawable.ic_note_black_24dp);
            }
        } else {
            mFileImageView.setImageResource(R.drawable.ic_note_black_24dp);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    int id = data.getIntExtra(ChooseFolderActivity.KEY_EXTRAS, -1);
                    if (id != -1) {
                        final int backup = mFile.getFromFolder();
                        mFile.setFromFolder(id);
                        mDB.moveFile(mFile, new DataSource.ResultCallBack() {
                            @Override public void onSuccess(@Nullable String success) {
                                Message msg = Message.obtain();
                                msg.arg1 = FEED_BACK;
                                Bundle bundle = new Bundle();
                                bundle.putString(KEY_USER, "success");
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                            }

                            @Override public void onError(@Nullable String error) {
                                Message msg = Message.obtain();
                                msg.arg1 = FEED_BACK;
                                Bundle bundle = new Bundle();
                                bundle.putString(KEY_USER, error);
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                                mFile.setFromFolder(backup);
                            }
                        });
                    }
                    break;
                case RESULT_CANCELED:
                    feedback("canceled");
                    break;
            }
        }
    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override protected void onStop() {
        super.onStop();

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mDownloadId != -1) {
            FileDownloader.getImpl().pause(mDownloadId);
        }
    }

    private void feedback(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private static class MHandler extends Handler {
        private final WeakReference<DetailedActivity> reference;

        MHandler(DetailedActivity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override public void handleMessage(Message msg) {
            DetailedActivity activity = reference.get();
            if (activity == null) return;
            switch (msg.arg1) {
                case FEED_BACK:
                    activity.feedback(msg.getData().getString(KEY_USER));
                    break;
            }
        }
    }

    private final Handler mHandler = new DetailedActivity.MHandler(this);

}
