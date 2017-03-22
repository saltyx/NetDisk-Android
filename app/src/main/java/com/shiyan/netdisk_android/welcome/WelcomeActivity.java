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
import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.main.MainActivity;
import com.shiyan.netdisk_android.setting.SettingActivity;
import com.shiyan.netdisk_android.utils.SPHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {


    @OnClick(R.id.button2) void goSetting() {
        SharedPreferences.Editor editor = SPHelper.getInstance(getApplication())
                .getSP().edit();
        editor.putBoolean(SPHelper.IS_FIRST_KEY, false);
        editor.apply();

        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Boolean isFirst = SPHelper.getInstance(getApplication())
                .getSP().getBoolean(SPHelper.IS_FIRST_KEY, true);
        String IP = SPHelper.getInstance(getApplication())
                .getSP().getString(SPHelper.IP_KEY, "null");
        String PORT = SPHelper.getInstance(getApplication())
                .getSP().getString(SPHelper.PORT_KEY, "null");
        String Token = SPHelper.getInstance(getApplication())
                .getSP().getString(SPHelper.TOKEN_KEY, "null");

        if (IP.contentEquals("null") || PORT.contentEquals("null")
                || Token.contentEquals("null")) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();return;
        }
        if (!isFirst) {
            SecuDiskApplication.IP = IP;
            SecuDiskApplication.Port = PORT;
            SecuDiskApplication.Token = Token;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();return;
        }

        ButterKnife.bind(this);

    }
}
