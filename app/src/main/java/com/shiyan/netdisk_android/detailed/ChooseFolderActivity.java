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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.adapter.ChooseFolderAdapter;
import com.shiyan.netdisk_android.data.DataRepoImpl;
import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.Inject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseFolderActivity extends AppCompatActivity {

    final String TAG = getClass().getName();
    public static final String KEY_EXTRAS = "KEY_EXTRAS";
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.folders) RecyclerView mFolders;

    public static final int CHANGE_DATA = 0x0002;

    private Stack<Integer> stack;
    private DataRepoImpl mDB;
    private int mCurrentId;
    private ChooseFolderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        ButterKnife.bind(this);
        mToolbar.inflateMenu(R.menu.choose_folder_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        mDB = Inject.provideDataRepo(getApplication());
        stack = new Stack<>();
        stack.push(1);
        mCurrentId = 1;
        mAdapter = new ChooseFolderAdapter(new ArrayList<UserFile>(), new ChooseFolderAdapter.CallBack() {
            @Override public void onItemClick(UserFile file) {
                Message msg = Message.obtain();
                msg.arg1 = CHANGE_DATA;
                msg.arg2 = file.getId();
                mHandler.sendMessage(msg);
            }
        });
        mFolders.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFolders.setLayoutManager(linearLayoutManager);
        mDB.getFilesByFolder(mCurrentId, new DataSource.LoadData() {
            @Override public void onLoaded(ArrayList data) {
                Message msg = Message.obtain();
                msg.arg1 = CHANGE_DATA;
                msg.arg2 = -1;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_EXTRAS, data);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            @Override public void onDataNotAvailable(@Nullable String msg) {

            }
        });

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_folder_menu, menu);

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checked:
                Intent sendBack = new Intent();
                sendBack.putExtra(KEY_EXTRAS, mCurrentId);
                setResult(RESULT_OK, sendBack);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFolder(int id) {
        mDB.getFilesByFolder(id, new DataSource.LoadData() {
            @Override public void onLoaded(ArrayList data) {
                Message msg = Message.obtain();
                msg.arg1 = CHANGE_DATA;
                msg.arg2 = -1;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_EXTRAS, data);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            @Override public void onDataNotAvailable(@Nullable String msg) {

            }
        });

    }

    public void updateAdapter(ArrayList<UserFile> data) {
        mAdapter.changeData(data);
    }

    @Override public void onBackPressed() {
        if (mCurrentId == 1 || stack.empty()) {
            super.onBackPressed();
        } else {
            stack.pop();
            mCurrentId = stack.peek();
            changeFolder(mCurrentId);
        }
    }

    @Override public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return super.onNavigateUp();
    }

    private static class MHandler extends Handler {
        private final WeakReference<ChooseFolderActivity> reference;

        MHandler(ChooseFolderActivity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override public void handleMessage(Message msg) {
            ChooseFolderActivity activity = reference.get();
            if (activity == null) return;
            switch (msg.arg1) {
                case CHANGE_DATA:
                    if (msg.arg2 == -1) {
                        ArrayList<UserFile> files = msg.getData().getParcelableArrayList(KEY_EXTRAS);
                        activity.updateAdapter(files);
                    } else {
                        activity.mCurrentId = msg.arg2;
                        activity.stack.push(msg.arg2);
                        activity.changeFolder(msg.arg2);
                    }
                    break;
            }
        }
    }

    private final Handler mHandler = new MHandler(this);

}
