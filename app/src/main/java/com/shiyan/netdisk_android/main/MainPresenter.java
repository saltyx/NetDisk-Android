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

package com.shiyan.netdisk_android.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.shiyan.netdisk_android.data.DataRepoImpl;
import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.utils.CallBack;
import com.shiyan.netdisk_android.utils.NetHelper;
import com.shiyan.netdisk_android.utils.UserFeedBack;

import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class MainPresenter implements MainContract.Presenter {


    DataRepoImpl mDataRepo;

    MainContract.View mMainView;

    public MainPresenter(DataRepoImpl mDataRepo, MainContract.View mMainView) {
        this.mDataRepo = mDataRepo;
        this.mMainView = mMainView;

        mMainView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void set() {
        mDataRepo.getFilesByFolder(1, new DataSource.LoadData() {
            @Override
            public void onLoaded(List data) {

            }

            @Override
            public void onDataNotAvailable(@Nullable String msg) {

            }
        });
    }


}
