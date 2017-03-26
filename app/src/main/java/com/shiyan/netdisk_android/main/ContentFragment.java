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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
public class ContentFragment extends Fragment implements MainContract.View {
    
    final String TAG = getClass().getName();

    MainContract.Presenter mPresenter;

    @BindView(R.id.folder_recyler_view)
    RecyclerView folderRecyclerView;

    @BindView(R.id.files_recycler_view)
    RecyclerView fileRecyclerView;

    List<UserFile> data;

    boolean isGrid;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, root);
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
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showFiles(String filesJson) {
        EventBus.getDefault().post(new MessageEvent(filesJson));
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        try {
            data = SerializeUserFile.serialize(files.filesJson);
            FileAdapter adapter = new FileAdapter(data);
            Log.i(TAG, String.valueOf(files.filesJson));
            fileRecyclerView.setLayoutManager(linearLayoutManager);
            fileRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            userFeedBack(e.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showFolder(MessageEvent folders) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        try {
            isGrid = true;
            FolderAdapter adapter = new FolderAdapter(data = SerializeUserFile.serializeFolder(folders.filesJson),
                    getActivity());
            folderRecyclerView.setLayoutManager(layoutManager);
            folderRecyclerView.setAdapter(adapter);
            folderRecyclerView.addItemDecoration(new GridSpaceItemDecoration(12));
        } catch (JSONException e) {
            userFeedBack(e.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLayout(FolderMessageEvent event) {

        FolderAdapter adapter = new FolderAdapter(data,getActivity());

        if (event.toGrid) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            isGrid = true;
            folderRecyclerView.setLayoutManager(layoutManager);
            folderRecyclerView.setAdapter(adapter);
            //folderRecyclerView.addItemDecoration(new GridSpaceItemDecoration(12));
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            isGrid = false;
            folderRecyclerView.setLayoutManager(linearLayoutManager);
            folderRecyclerView.setAdapter(adapter);

        }
    }


}
