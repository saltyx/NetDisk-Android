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

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.setting.SettingActivity;
import com.shiyan.netdisk_android.utils.ActivityHelper;
import com.shiyan.netdisk_android.utils.IMMLeaks;
import com.shiyan.netdisk_android.utils.Inject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final String TAG = getClass().getName();

    final String KEY_CONTENT = "CONTENT";
    final String KEY_DETAILED = "DETAILED";

    @BindView(R.id.toolbar_main)
    Toolbar mainToolBar;

    SearchView searchView;
    MainPresenter mPresenter;
    Menu mMenu;
    ContentFragment contentFragment;
    SearchResultFragment searchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mainToolBar.inflateMenu(R.menu.main_toolbar_menus);
        setSupportActionBar(mainToolBar);

        contentFragment =
                (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.content);

        searchResultFragment =
                (SearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.search_result);

        if (null == contentFragment) {
            contentFragment = ContentFragment.newInstance();
            ActivityHelper.addFragmentToActivity(getSupportFragmentManager(),contentFragment ,R.id.content);
        }

        if (null == searchResultFragment) {
            searchResultFragment = SearchResultFragment.newInstance();
            //ActivityHelper.addFragmentToActivity(getSupportFragmentManager(), searchResultFragment, R.id.search_result);
        }

        mPresenter = new MainPresenter(Inject.provideDataRepo(getApplication()), contentFragment, searchResultFragment);

        mPresenter.start();

        IMMLeaks.fixFocusedViewLeak(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main_toolbar_menus, menu);
        final MenuItem actionMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override public boolean onMenuItemActionExpand(MenuItem item) {
                ActivityHelper.replaceFragment(getSupportFragmentManager(), contentFragment,searchResultFragment, R.id.content);
                return true;
            }

            @Override public boolean onMenuItemActionCollapse(MenuItem item) {
                ActivityHelper.replaceFragment(getSupportFragmentManager(),searchResultFragment, contentFragment, R.id.content);

                return true;
            }
        });

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
                mPresenter.query(newText);
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
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        if (mPresenter.backToPrevious() == -1) {
            super.onBackPressed();
        }
    }
}
