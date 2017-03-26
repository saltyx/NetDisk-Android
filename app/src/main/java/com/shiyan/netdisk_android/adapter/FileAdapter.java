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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author shiyan
 *
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    final String TAG = getClass().getName();

    List<UserFile> data;

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name)
        TextView fileName;

        @BindView(R.id.file_image)
        ImageView fileImage;

        public FileViewHolder(View item) {
            super(item);
            ButterKnife.bind(this,item);
        }
    }

    public FileAdapter(List<UserFile> data) {
        this.data = data;
    }

    public void changeData(List<UserFile> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileViewHolder mHolder = new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_file, parent, false));
        return mHolder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        Log.d(TAG, data.get(position).getFileName());
        holder.fileName.setText(data.get(position).getFileName());
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }
}
