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

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.model.UserFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class WithOneInputDialogFragment extends DialogFragment {

    public static final String KEY = "USER_FILE";

    private String mTitleText = null;
    private String mOkButtonText = null;
    private String mCancelButtonText = null;

    private CallBack callback;

    @BindView(R.id.dialog_title) TextView mTitle;
    @BindView(R.id.input) EditText mInput;
    @BindView(R.id.ok_button) Button mOKButton;
    @BindView(R.id.cancel_button) Button mCancelButton;

    @OnClick(R.id.ok_button) void onOkClick() {
        if (this.callback != null) {
            callback.onOkClick(mInput.getText().toString());
        }
    }

    @OnClick(R.id.cancel_button) void onCancelClick() {
        if (this.callback != null) {
            callback.onCancelClick();
        }
    }

    public static WithOneInputDialogFragment newInstance(UserFile file) {
        Bundle args = new Bundle();
        args.putParcelable(KEY, file);
        WithOneInputDialogFragment fragment = new WithOneInputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
        View root = inflater.inflate(R.layout.fragment_with_one_input_dialog, container);
        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            UserFile file = getArguments().getParcelable(KEY);
            if (file != null) {
                mTitleText = mTitleText == null ? "Rename ".concat(file.getFileName()) : mTitleText;
                mOkButtonText = mOkButtonText == null ? "OK" : mOkButtonText;
                mCancelButtonText = mCancelButtonText == null ? "Cancel" : mCancelButtonText;
                mOKButton.setText(mOkButtonText);
                mCancelButton.setText(mCancelButtonText);
                mTitle.setText(mTitleText);
            } else {
                mTitleText = mTitleText == null ? "Title" : mTitleText;
                mOkButtonText = mOkButtonText == null ? "OK" : mOkButtonText;
                mCancelButtonText = mCancelButtonText == null ? "Cancel" : mCancelButtonText;
                mOKButton.setText(mOkButtonText);
                mCancelButton.setText(mCancelButtonText);
                mTitle.setText(mTitleText);
            }
        }
        return root;
    }

    public WithOneInputDialogFragment setTitle(String title) {
        mTitleText = title;
        return this;
    }

    public WithOneInputDialogFragment setOkText(String okText) {
        mOkButtonText = okText;
        return this;
    }

    public WithOneInputDialogFragment setCancelText(String cancelText) {
        mCancelButtonText = cancelText;
        return this;
    }

    public WithOneInputDialogFragment setCallBack(CallBack callback) {
        this.callback = callback;
        return this;
    }

    public interface CallBack {
        void onOkClick(String text);
        void onCancelClick();
    }
}
