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

package com.shiyan.netdisk_android.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class DetailInfoDialogFragment extends AttachDialogFragment {

    public static final String KEY_USER_FILE = "USER_FILE";

    private UserFile file;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.delete)
    TextView delete;

    @BindView(R.id.lock)
    TextView lock;

    @BindView(R.id.rename)
    TextView rename;

    @BindView(R.id.share)
    TextView share;

    public static DetailInfoDialogFragment newInstance(UserFile userFile) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_USER_FILE, userFile);
        DetailInfoDialogFragment fragment = new DetailInfoDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.horizontalMargin = 0;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
        View root = inflater.inflate(R.layout.fragment_detail_info_dialog, container);
        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            this.file = getArguments().getParcelable(KEY_USER_FILE);
            name.setText(file.getFileName());
        }
        return root;
    }
}
