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

public class RecentFileAdapter extends RecyclerView.Adapter<RecentFileAdapter.FileViewHolder>
                        implements BaseAdapter {
    final static int MAX_SIZE = 5;

    final String TAG = getClass().getName();

    private List<UserFile> data;
    private boolean showAll;

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name) TextView fileName;
        @BindView(R.id.file_image) ImageView fileImage;
        @BindView(R.id.update_time) TextView updatedTime;

        FileViewHolder(View item) {
            super(item);
            ButterKnife.bind(this,item);
        }
    }

    public RecentFileAdapter(List<UserFile> data, boolean showAll) {
        if (!showAll) {
            while (data.size() > MAX_SIZE) {
                data.remove(0);
            }
        }
        this.showAll = showAll;
        this.data = data;
    }

    @Override public void changeData(List<UserFile> data) {
        if (!showAll) {
            while (data.size() >= MAX_SIZE) {
                data.remove(0);
            }
        }
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
        if (!showAll) {
            if (data.size() > MAX_SIZE) {
                data.remove(0);
            }
        }
        data.add(file);
        notifyDataSetChanged();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        holder.fileName.setText(data.get(position).getFileName());
        String updatedTime = data.get(position).getUpdateAt();
        holder.updatedTime.setText("updated at ".concat(updatedTime == null || updatedTime.length() == 0 ? "\njust now" : updatedTime));
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }
}
