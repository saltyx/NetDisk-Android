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


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.adapter.FileAdapter;
import com.shiyan.netdisk_android.adapter.FolderAdapter;
import com.shiyan.netdisk_android.adapter.GridSpaceItemDecoration;
import com.shiyan.netdisk_android.event.FolderMessageEvent;
import com.shiyan.netdisk_android.event.MessageEvent;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.SerializeUserFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements MainContract.View , SwipeRefreshLayout.OnRefreshListener {
    
    final String TAG = getClass().getName();

    MainContract.Presenter mPresenter;

    @BindView(R.id.folder_recyler_view)
    RecyclerView folderRecyclerView;

    FolderAdapter folderAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    GridLayoutManager gridLayoutManager = null;

    @BindView(R.id.files_recycler_view)
    RecyclerView fileRecyclerView;

    FileAdapter fileAdapter = null;

    @BindView(R.id.swipeRerfreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<UserFile> data;

    boolean isGrid;

    boolean isRefresh;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, root);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        folderRecyclerView.addItemDecoration(new GridSpaceItemDecoration(12));

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            mPresenter.set();
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showFiles(String filesJson) {
        EventBus.getDefault().post(new MessageEvent(filesJson));
        mSwipeRefreshLayout.setRefreshing(false);
        isRefresh = false;
        Log.i(TAG, String.valueOf(filesJson));
    }

    @Override
    public void showFolders(List<UserFile> folders) {

    }

    @Override
    public void showByGrid() {
        EventBus.getDefault().post(new FolderMessageEvent(true));
    }

    @Override
    public void showByList() {
        EventBus.getDefault().post(new FolderMessageEvent(false));

    }

    @Override
    public void toggle() {
        if (isGrid) {
            showByList();
        } else {
            showByGrid();
        }
    }

    @Override
    public void userFeedBack(String msg) {
        Snackbar.make(folderRecyclerView, msg, Snackbar.LENGTH_INDEFINITE).show();
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateFiles(MessageEvent files) {
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        try {
            data = SerializeUserFile.serialize(files.filesJson);
            if (fileAdapter == null) {
                fileAdapter = new FileAdapter(data);
                Log.i(TAG, String.valueOf(files.filesJson));
                fileRecyclerView.setLayoutManager(linearLayoutManager);
                fileRecyclerView.setAdapter(fileAdapter);
            } else {
                fileAdapter.changeData(data);
            }

        } catch (JSONException e) {
            userFeedBack(e.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showFolder(MessageEvent folders) {
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }
        try {
            isGrid = true;
            data = SerializeUserFile.serializeFolder(folders.filesJson);
            if (folderAdapter == null) {
                folderAdapter = new FolderAdapter(data,
                        getActivity());
                folderRecyclerView.setLayoutManager(gridLayoutManager);
                folderRecyclerView.setAdapter(folderAdapter);
            } else {
                folderAdapter.changeData(data);
            }
        } catch (JSONException e) {
            userFeedBack(e.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLayout(FolderMessageEvent event) {
        if (folderAdapter == null) {
            folderAdapter = new FolderAdapter(data, getActivity());
        }

        if (event.toGrid) {
            isGrid = true;
            folderRecyclerView.setLayoutManager(gridLayoutManager);
            folderRecyclerView.setAdapter(folderAdapter);
        } else {
            isGrid = false;
            folderRecyclerView.setLayoutManager(linearLayoutManager);
            folderRecyclerView.setAdapter(folderAdapter);

        }
    }


}
