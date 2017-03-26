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

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.dialog.DetailInfoDialogFragment;
import com.shiyan.netdisk_android.model.UserFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.GridViewHolder> {

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.folder_name)
        TextView folderName;

        @OnClick(R.id.show_more) void onMoreClick() {
            DetailInfoDialogFragment.newInstance(file).show(mActivity.getFragmentManager(),"TAG");
        }

        UserFile file;
        Activity mActivity;

        public GridViewHolder(View item, Activity activity) {
            super(item);
            this.mActivity = activity;
            ButterKnife.bind(this, item);
        }
    }

    private List<UserFile> data;
    private Activity mActivity;

    public FolderAdapter(List<UserFile> data, Activity activity) {
        this.data = data;
        this.mActivity = activity;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_folder, parent, false);
        return new GridViewHolder(root, mActivity);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        String folderName = data.get(position).getFileName();
        holder.folderName.setText(folderName.substring(0,folderName.length() > 5 ? 5 : folderName.length()));
        holder.file = data.get(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
