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

package com.shiyan.netdisk_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultVH> implements BaseAdapter {

    static class SearchResultVH extends RecyclerView.ViewHolder {

        @OnClick(R.id.rl) void onItemClick() {
            // not ready
        }

        @BindView(R.id.image) ImageView mIconImage;
        @BindView(R.id.file_name) TextView mFileName;
        @BindView(R.id.create_time) TextView mCreatedTime;

        @OnClick(R.id.more) void onMoreClick() {
            //show more options
            //build a dialog for more options
        }

        UserFile currentFile;

        SearchResultVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private ArrayList<UserFile> mData;

    public SearchResultAdapter(ArrayList<UserFile> data) {
        this.mData = data;
    }

    @Override public SearchResultVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override public void onBindViewHolder(SearchResultVH holder, int position) {
        UserFile file = mData.get(position);
        if (file == null) return;
        holder.currentFile = file;
        holder.mFileName.setText(file.getFileName());
        holder.mCreatedTime.setText(file.getCreateAt());
        if (file.isFolder()) {
            holder.mIconImage.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            holder.mIconImage.setImageResource(R.drawable.ic_note_black_24dp);
        }
    }

    @Override public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override public void changeData(List<UserFile> data) {
        this.mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override public void remove(UserFile file) {

    }

    @Override public void renameItem(UserFile file) {

    }

    @Override public void addItem(UserFile file) {

    }
}
