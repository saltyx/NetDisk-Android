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


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.adapter.SearchResultAdapter;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.SerializeUserFile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment implements MainContract.SearchView {

    final String TAG = getClass().getName();

    final static int MSG_SHOW_SEARCH_RESULT = 0x0001;
    final static String KEY_BUNDLE = "KEY_BUNDLE";

    private MainContract.Presenter mPresenter;
    private SearchResultAdapter mAdapter;
    @BindView(R.id.search_result) RecyclerView mSearchResult;

    public SearchResultFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);
        ButterKnife.bind(this, root);
        mAdapter = new SearchResultAdapter(null);
        mSearchResult.setAdapter(mAdapter);
        mSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        //mSearchResult.set
        return root;

    }

    public static SearchResultFragment newInstance() {
        return new SearchResultFragment();
    }

    @Override public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override public void show(ArrayList<UserFile> data) {
        Message msg = Message.obtain();
        msg.arg1 = MSG_SHOW_SEARCH_RESULT;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_BUNDLE, data);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Override public void userFeedBack(String msg) {
        if (msg == null || msg.contentEquals("")) return;
        //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "userFeedBack: "+msg);
    }

    public void showResult(ArrayList<UserFile> data) {
        mAdapter.changeData(SerializeUserFile.sort(data));
    }

    private static class MHandler extends Handler {
        private final WeakReference<SearchResultFragment> searchFragment;

        MHandler(SearchResultFragment fragment) {
            searchFragment = new WeakReference<>(fragment);
        }

        @Override public void handleMessage(Message msg) {
            SearchResultFragment fragment = searchFragment.get();
            if (fragment != null) {
                switch (msg.arg1) {
                    case MSG_SHOW_SEARCH_RESULT:
                        ArrayList<UserFile> data = msg.getData().getParcelableArrayList(KEY_BUNDLE);
                        fragment.showResult(data);
                        break;
                }
            }
        }
    }

    private final MHandler mHandler = new MHandler(this);
}
