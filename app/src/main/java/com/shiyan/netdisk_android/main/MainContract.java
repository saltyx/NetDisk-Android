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

import android.graphics.Bitmap;

import com.shiyan.netdisk_android.BasePresenter;
import com.shiyan.netdisk_android.BaseView;
import com.shiyan.netdisk_android.model.UserFile;
import com.vincent.filepicker.filter.entity.BaseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public interface MainContract {

    int FEED_BACK_TOAST_SHORT = 1;
    int FEED_BACK_TOAST_LONG = 2;
    int FEED_BACK_SNACKBAR_SHORT = 3;
    int FEED_BACK_SNACKBAR_LONG = 4;
    int FEED_BACK_SNACKBAR_INDEFINITE = 5;

    interface View extends BaseView <Presenter> {
        void showFiles(ArrayList<UserFile> filesJson);
        void showFolders(ArrayList<UserFile> folders);
        void showByGrid();
        void showByList();
        void toggle();
        void userFeedBack(String msg, int type);
        void remove(UserFile file);
        void rename(UserFile file);
        void encrypt(UserFile file);
        void decrypt(UserFile file);
        void refresh(boolean refresh);
        void add(UserFile file);
        void addFolder(UserFile file);
        void setTitle(String title);
        void showOrHideRecentFile(boolean showOrHide);
        String getTitle();
    }

    interface SearchView extends BaseView <Presenter> {
        void show(ArrayList<UserFile> data);
        void share(UserFile file);
        void userFeedBack(String msg);
    }

    interface Presenter extends BasePresenter {
        void set(int folderId);
        void setRoot();
        void change();
        void delete(UserFile file);
        void rename(UserFile file);
        void shareOrCancel(UserFile file);
        void encryptOrDecrypt(UserFile file, String passPhrase);
        void uploadCommonFile(List<BaseFile> file);
        void createFolder(UserFile file);
        void goToNextFolder(UserFile file);
        int backToPrevious();
        void query(String queryText);
    }
}
