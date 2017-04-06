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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.data.DataRepoImpl;
import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.dialog.DetailInfoDialogFragment;
import com.shiyan.netdisk_android.dialog.WithOneInputDialogFragment;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.ImageLoader;
import com.shiyan.netdisk_android.utils.Inject;
import com.shiyan.netdisk_android.utils.Utils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailedActivity extends AppCompatActivity {

    public static final String KEY_USER = "USER_KEY";
    public final String TAG = getClass().getName();

    private DataRepoImpl mDB;

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

                            }

                            @Override public void onError(@Nullable String error) {

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

    }

    @OnClick(R.id.save) void onSaveClick() {

    }

    @OnClick(R.id.delete) void onDeleteClick() {

    }

    @OnClick(R.id.more) void onMoreClick() {
        final DetailInfoDialogFragment dialogFragment = DetailInfoDialogFragment.newInstance(mFile);
        dialogFragment.setCallBack(new DetailInfoDialogFragment.OnMoreCallBack() {
            @Override public void onDeletedClick(UserFile file) {

            }

            @Override public void onRenameClick(UserFile file) {

            }

            @Override public void onEncryptOrDecryptClick(UserFile file) {

            }

            @Override public void onShareClick(UserFile file) {

            }
        }).show(getFragmentManager(), TAG);
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

        mDB = Inject.provideDataRepo(getApplication());
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

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
