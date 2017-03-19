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

package com.shiyan.netdisk_android.setting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.utils.CallBack;
import com.shiyan.netdisk_android.utils.NetHelper;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @OnClick(R.id.ok) void goToMain() {
        CharSequence ip = mIP.getText();
        CharSequence port = mPort.getText();
        CharSequence username = mUsername.getText();
        CharSequence password = mPassword.getText();

        if (ip.length() == 0 || username.length() == 0
                || password.length() == 0 || port.length() == 0) {
            Snackbar.make(ok,"IP, username, password or port can not be null",Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(ok, "Connecting...",Snackbar.LENGTH_INDEFINITE).show();
            try {
                NetHelper.getInstance().login(new NetHelper.LoginModel(ip.toString(), port.toString(), username.toString(), password.toString()), new CallBack() {
                    @Override
                    public void success(@NonNull String data) {
                        Snackbar.make(ok, "Connected!",Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(@Nullable String error) {
                        Snackbar.make(ok, "Error!",Snackbar.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                Snackbar.make(ok, e.toString(),Snackbar.LENGTH_SHORT).show();
            } catch (IllegalArgumentException ee) {
                Snackbar.make(ok, ee.toString(), Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    @BindView(R.id.ip)
    EditText mIP;

    @BindView(R.id.port)
    EditText mPort;

    @BindView(R.id.username)
    EditText mUsername;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.ok)
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);


    }
}
