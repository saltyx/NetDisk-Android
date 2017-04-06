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

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.SerializeUserFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class ChooseFolderAdapter extends RecyclerView.Adapter<ChooseFolderAdapter.CFViewHolder> {

    String TAG = getClass().getName();

    static class CFViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_image) ImageView mImageView;
        @BindView(R.id.file_name) TextView mFileNameView;
        @BindView(R.id.create_time) TextView mFileCreatedTime;

        UserFile mFile;
        CallBack mCallback;

        @OnClick(R.id.rl) void onClick() {
            if (mFile != null && mFile.isFolder()) {
                mCallback.onItemClick(mFile);
            }
        }

        CFViewHolder(View item) {
            super(item);
            ButterKnife.bind(this, item);
        }
    }

    private ArrayList<UserFile> mData;
    private CallBack mCallBack;

    public ChooseFolderAdapter(ArrayList<UserFile> mData, CallBack callBack) {
        this.mData = (ArrayList<UserFile>) SerializeUserFile.sort(mData);
        this.mCallBack = callBack;
    }

    @Override public CFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CFViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_folder,parent,false));
    }

    @Override public void onBindViewHolder(CFViewHolder holder, int position) {
        UserFile file = mData.get(position);
        holder.mFileNameView.setText(file.getFileName());
        holder.mFileCreatedTime.setText(file.getCreateAt().concat("-"));
        holder.mCallback = mCallBack;
        holder.mFile = file;

        if (file.isFolder()) {
            holder.mImageView.setImageResource(R.drawable.ic_folder_black_24dp);
            holder.mFileCreatedTime.setTextColor(Color.BLACK);
            holder.mFileNameView.setTextColor(Color.BLACK);
        } else {
            holder.mFileCreatedTime.setTextColor(Color.GRAY);
            holder.mFileNameView.setTextColor(Color.GRAY);
            holder.mImageView.setImageResource(R.drawable.ic_note_black_24dp);
        }
    }

    @Override public int getItemCount() {
        int size = mData == null ? 0 : mData.size();
        return size;
    }

    public void changeData(ArrayList<UserFile> data) {
        this.mData = (ArrayList<UserFile>)SerializeUserFile.sort(data);
        notifyDataSetChanged();
    }

    public interface CallBack {
        void onItemClick(UserFile file);
    }

}
