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

package com.shiyan.netdisk_android.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.utils.ActivityHelper;
import com.shiyan.netdisk_android.utils.IMMLeaks;
import com.shiyan.netdisk_android.utils.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    Toolbar mainToolBar;

    SearchView searchView;
    MainPresenter mPresenter;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mainToolBar.inflateMenu(R.menu.main_toolbar_menus);
        setSupportActionBar(mainToolBar);

        ContentFragment contentFragment =
                (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (contentFragment == null) {
            contentFragment = ContentFragment.newInstance();
            ActivityHelper.addFragmentToActivity(getSupportFragmentManager(),contentFragment ,R.id.content);
        }

        mPresenter = new MainPresenter(Inject.provideDataRepo(getApplication()), contentFragment);

        mPresenter.start();

        IMMLeaks.fixFocusedViewLeak(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main_toolbar_menus, menu);
        final MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) actionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                actionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return true;
    }

    private void showGrid() {
        for (int i = 0; i < mMenu.size(); i++) {
            if (mMenu.getItem(i).getItemId() == R.id.change_to_grid) {
                mMenu.getItem(i).setVisible(false);
            } else if (mMenu.getItem(i).getItemId() == R.id.change_to_list) {
                mMenu.getItem(i).setVisible(true);
            }
        }
    }

    private void showList() {
        for (int i = 0; i < mMenu.size(); i++) {
            if (mMenu.getItem(i).getItemId() == R.id.change_to_grid) {
                mMenu.getItem(i).setVisible(true);
            } else if (mMenu.getItem(i).getItemId() == R.id.change_to_list) {
                mMenu.getItem(i).setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change_to_grid:
                showGrid();mPresenter.change();
                return true;
            case R.id.change_to_list:
                showList();mPresenter.change();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
