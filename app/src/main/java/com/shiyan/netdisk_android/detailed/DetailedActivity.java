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

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.data.DataRepoImpl;
import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.dialog.WithOneInputDialogFragment;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.ImageLoader;
import com.shiyan.netdisk_android.utils.Inject;
import com.shiyan.netdisk_android.utils.Utils;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailedActivity extends AppCompatActivity {

    public static final String KEY_USER = "USER_KEY";
    public final String TAG = getClass().getName();

    private DataRepoImpl mDB;

    @BindView(R.id.name) TextView mFileName;
    @BindView(R.id.delete) TextView mDeleteFile;
    @BindView(R.id.lock) TextView mLock;
    @BindView(R.id.lock_switch) SwitchCompat mSwitch;
    @BindView(R.id.rename) TextView mRename;
    @BindView(R.id.share) TextView mShare;
    @BindView(R.id.file_image) ImageView mImage;

    @OnClick(R.id.delete) void onDelete() {
        if (mFile == null) return;
        mDB.deleteFiles(mFile, new DataSource.ResultCallBack() {
            @Override public void onSuccess(@Nullable String success) {

            }

            @Override public void onError(@Nullable String error) {

            }
        });
    }

    @OnClick(R.id.lock) void onLock() {
        if (mFile == null) return;
        String title = "Encrypt ".concat(mFile.getFileName());
        final boolean backup = mFile.isEncrypted();

        mFile.setEncrypted(true);
        if (mFile.isEncrypted()) {
            title = "Decrypt ".concat(mFile.getFileName());
            mFile.setEncrypted(false);
        }
        buildDialog(title, new WithOneInputDialogFragment.CallBack() {
            @Override public void onOkClick(String text) {
                mDB.decryptFile(mFile, text, new DataSource.ResultCallBack() {
                    @Override public void onSuccess(@Nullable String success) {

                    }

                    @Override public void onError(@Nullable String error) {
                        mFile.setEncrypted(backup);
                    }
                });
            }

            @Override public void onCancelClick() {

            }
        });

    }

    @OnClick(R.id.rename) void onRename() {
        if (mFile == null) return;
        final String backup = mFile.getFileName();
        buildDialog("Rename ".concat(mFile.getFileName()), new WithOneInputDialogFragment.CallBack() {
            @Override public void onOkClick(String text) {
                mFile.setFileName(text);
                mDB.updateFile(mFile, new DataSource.ResultCallBack() {
                    @Override public void onSuccess(@Nullable String success) {

                    }

                    @Override public void onError(@Nullable String error) {
                        mFile.setFileName(backup);
                    }
                });
            }

            @Override public void onCancelClick() {

            }
        });
    }

    @OnClick(R.id.share) void onShare() {
        if (mFile == null) return;
        final boolean backup = mFile.isShared();
        String title = "Share ".concat(mFile.getFileName());
        mFile.setShared(true);
        if (mFile.isShared()) {
            mFile.setShared(false);
            title = "Private ".concat(mFile.getFileName());
        }
        buildDialog(title, new WithOneInputDialogFragment.CallBack() {
            @Override public void onOkClick(String text) {
                mDB.shareFile(mFile, new DataSource.ResultCallBack() {
                    @Override public void onSuccess(@Nullable String success) {

                    }

                    @Override public void onError(@Nullable String error) {
                        mFile.setShared(backup);
                    }
                });
            }

            @Override public void onCancelClick() {

            }
        });
    }

    private UserFile mFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        mFile = bundle.getParcelable(KEY_USER);
        if (mFile == null) return;
        mFileName.setText(mFile.getFileName());
        mSwitch.setChecked(mFile.isEncrypted());
        mDB = Inject.provideDataRepo(getApplication());
        if (!Utils.isImage(mFile.getFileName())) return;
        try {
            mImage.setImageBitmap(ImageLoader.getInstance().getImage(mFile.getId()));
        } catch (JSONException e) {
            e.printStackTrace();
            mImage.setImageResource(R.drawable.ic_note_black_24dp);
        } catch (IOException ee) {
            mImage.setImageResource(R.drawable.ic_note_black_24dp);
        }
    }

    private void buildDialog(String title, WithOneInputDialogFragment.CallBack callBack) {
        final WithOneInputDialogFragment dialogFragment = WithOneInputDialogFragment.newInstance(mFile)
                .setTitle(title)
                .setCallBack(callBack);
        dialogFragment.show(getFragmentManager(), TAG);
    }
}
