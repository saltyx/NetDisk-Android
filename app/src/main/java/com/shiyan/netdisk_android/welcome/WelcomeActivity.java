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

package com.shiyan.netdisk_android.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.login.LoginActivity;
import com.shiyan.netdisk_android.setting.SettingActivity;
import com.shiyan.netdisk_android.utils.SPHelper;
import com.shiyan.netdisk_android.SercuDiskApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    private String isFirstKey = "IS_FIRST";

    @OnClick(R.id.button2) void goSetting() {
        SharedPreferences.Editor editor = SPHelper.getInstance((SercuDiskApplication) getApplication())
                .getSP().edit();
        editor.putBoolean(isFirstKey, false);
        editor.commit();

        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Boolean isFirst = SPHelper.getInstance((SercuDiskApplication) getApplication())
                .getSP().getBoolean(isFirstKey, true);
        if (!isFirst) {
            // TODO: 2017/3/19 跳转到初始界面
        }

        ButterKnife.bind(this);

    }
}
