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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.ImageLoader;
import com.shiyan.netdisk_android.utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MHolder> implements BaseAdapter {

    private List<UserFile> data;
    final String TAG = getClass().getName();
    static class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name) TextView mFileName;
        @BindView(R.id.file_size) TextView mFileSize;
        @BindView(R.id.file_image) ImageView mFileImage;

        MHolder(View item) {
            super(item);
            ButterKnife.bind(this,item);
        }

    }

    public FileAdapter(List<UserFile> data) {
        this.data = data;
    }

    @Override public MHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new FileAdapter.MHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_file_all, parent, false));

    }

    @Override public void onBindViewHolder(final MHolder holder, int position) {
        UserFile file = data.get(position);
        holder.mFileName.setText(file.getFileName());
        holder.mFileSize.setText(Utils.calculateFileSize(file.getFileSize()));
        if (!Utils.isImage(file.getFileName())) {
            holder.mFileImage.setImageResource(R.drawable.ic_note_black_24dp);
        } else {
            DownLoadImageTask action = new DownLoadImageTask(holder.mFileImage);
            action.execute(file.getId());
        }
    }

    @Override public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override public void changeData(List<UserFile> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override public void remove(UserFile file) {
        int fileId = file.getId();
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == fileId) {
                index = i; break;
            }
        }
        if (index != -1) {
            data.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override public void renameItem(UserFile file) {
        int id = file.getId();
        String newName = file.getFileName();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == id) {
                data.get(i).setFileName(newName);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override public void addItem(UserFile file) {
        data.add(file);
        notifyDataSetChanged();
    }

}
