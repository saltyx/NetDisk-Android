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


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.adapter.FileAdapter;
import com.shiyan.netdisk_android.adapter.FolderAdapter;
import com.shiyan.netdisk_android.adapter.GridSpaceItemDecoration;
import com.shiyan.netdisk_android.dialog.DetailInfoDialogFragment;
import com.shiyan.netdisk_android.dialog.WithOneInputDialogFragment;
import com.shiyan.netdisk_android.event.DeleteEvent;
import com.shiyan.netdisk_android.event.EncryptOrDecryptEvent;
import com.shiyan.netdisk_android.event.FolderMessageEvent;
import com.shiyan.netdisk_android.event.MessageEvent;
import com.shiyan.netdisk_android.event.RenameEvent;
import com.shiyan.netdisk_android.event.UserFeedBackEvent;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.SerializeUserFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_INDEFINITE;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_LONG;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_SHORT;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_TOAST_LONG;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_TOAST_SHORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements MainContract.View , SwipeRefreshLayout.OnRefreshListener {

    final String TAG = getClass().getName();

    MainContract.Presenter mPresenter;

    @BindView(R.id.folder_recyler_view)
    RecyclerView folderRecyclerView;

    FolderAdapter folderAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    GridLayoutManager gridLayoutManager = null;

    @BindView(R.id.files_recycler_view)
    RecyclerView fileRecyclerView;

    FileAdapter fileAdapter = null;

    @BindView(R.id.swipeRerfreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<UserFile> data;

    boolean isGrid;

    boolean isRefresh;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, root);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        folderRecyclerView.addItemDecoration(new GridSpaceItemDecoration(12));

        Log.i(TAG, "onCreateView: Running");
        
        return root;
    }


    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            mPresenter.set();
        }
    }

    @Override public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override public void showFiles(String filesJson) {
        EventBus.getDefault().post(new MessageEvent(filesJson));
    }

    @Override public void showFolders(List<UserFile> folders) {

    }

    @Override public void showByGrid() {
        EventBus.getDefault().post(new FolderMessageEvent(true));
    }

    @Override public void showByList() {
        EventBus.getDefault().post(new FolderMessageEvent(false));

    }

    @Override public void remove(int fileId,boolean isFolder) {
        EventBus.getDefault().post(new DeleteEvent(fileId, isFolder));
    }

    @Override public void rename(int id, String newName, boolean isFolder) {
        EventBus.getDefault().post(new RenameEvent(newName, id, isFolder));
    }

    @Override public void toggle() {
        if (isGrid) {
            showByList();
        } else {
            showByGrid();
        }
    }

    @Override public void userFeedBack(String msg, int type) {
        EventBus.getDefault().post(new UserFeedBackEvent(msg, type));
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void updateFiles(MessageEvent files) {
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        try {
            data = SerializeUserFile.serialize(files.filesJson);
            if (fileAdapter == null) {
                fileAdapter = new FileAdapter(data);
                Log.i(TAG, String.valueOf(files.filesJson));
                fileRecyclerView.setLayoutManager(linearLayoutManager);
                fileRecyclerView.setAdapter(fileAdapter);
            } else {
                fileAdapter.changeData(data);
            }

        } catch (JSONException e) {
            userFeedBack(e.toString(), FEED_BACK_SNACKBAR_INDEFINITE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void showFolder(MessageEvent folders) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        isRefresh = false;
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }
        try {
            isGrid = true;
            data = SerializeUserFile.serializeFolder(folders.filesJson);
            if (folderAdapter == null) {
                folderAdapter = new FolderAdapter(data,
                        new FolderAdapter.CallBack() {
                            @Override
                            public void onMoreClick(UserFile file) {
                                if (file != null) {
                                    final DetailInfoDialogFragment dialog = DetailInfoDialogFragment.newInstance(file);
                                    dialog.setCallBack(new DetailInfoDialogFragment.OnMoreCallBack() {
                                        @Override
                                        public void onDeletedClick(UserFile file) {
                                            mPresenter.delete(file);
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onRenameClick(UserFile file) {
                                            buildDialogForRename(file);
                                            dialog.dismiss();

                                        }

                                        @Override
                                        public void onEncryptOrDecryptClick(UserFile file) {
                                            buildDialogForEncryptOrDecrypt(file);
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onShareClick(UserFile file) {
                                            file.setShared(!file.isShared());//取否
                                            mPresenter.shareOrCancel(file);
                                            dialog.dismiss();
                                        }
                                    }).show(getActivity().getFragmentManager(),TAG);
                                }
                            }
                        });

                folderRecyclerView.setLayoutManager(gridLayoutManager);
                folderRecyclerView.setAdapter(folderAdapter);
            } else {
                folderAdapter.changeData(data);
            }
        } catch (JSONException e) {
            userFeedBack(e.toString(),FEED_BACK_SNACKBAR_INDEFINITE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void changeLayout(FolderMessageEvent event) {
        if (folderAdapter == null) {
            folderAdapter = new FolderAdapter(data, new FolderAdapter.CallBack() {
                @Override
                public void onMoreClick(UserFile file) {
                    if (file != null) {
                        final DetailInfoDialogFragment dialog = DetailInfoDialogFragment.newInstance(file);
                        dialog.setCallBack(new DetailInfoDialogFragment.OnMoreCallBack() {
                            @Override
                            public void onDeletedClick(UserFile file) {
                                mPresenter.delete(file);
                                dialog.dismiss();
                            }

                            @Override
                            public void onRenameClick(UserFile file) {
                                buildDialogForRename(file);
                                dialog.dismiss();
                            }

                            @Override
                            public void onEncryptOrDecryptClick(UserFile file) {
                                buildDialogForEncryptOrDecrypt(file);
                                dialog.dismiss();
                            }

                            @Override
                            public void onShareClick(UserFile file) {

                            }
                        }).show(getActivity().getFragmentManager(),TAG);
                    }
                }
            });
        }

        if (event.toGrid) {
            isGrid = true;
            folderRecyclerView.setLayoutManager(gridLayoutManager);
            folderRecyclerView.setAdapter(folderAdapter);
        } else {
            isGrid = false;
            folderRecyclerView.setLayoutManager(linearLayoutManager);
            folderRecyclerView.setAdapter(folderAdapter);

        }
    }

    @Override public void encrypt(int id) {
        EventBus.getDefault().post(new EncryptOrDecryptEvent(id, true));
    }

    @Override public void decrypt(int id) {
        EventBus.getDefault().post(new EncryptOrDecryptEvent(id, false));
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void removeItem(DeleteEvent event) {
        if (event.isFolder) {
            if (folderAdapter == null) return;
            folderAdapter.remove(event.id);
        } else {
            if (fileAdapter == null) return;
            fileAdapter.remove(event.id);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void feedback(UserFeedBackEvent event) {
        final String msg = event.message;
        Log.i(TAG, msg);
        switch (event.type) {
            case FEED_BACK_TOAST_SHORT:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                break;
            case FEED_BACK_TOAST_LONG:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                break;
            case FEED_BACK_SNACKBAR_SHORT:
                Snackbar.make(folderRecyclerView, msg, Snackbar.LENGTH_SHORT).show();
                break;
            case FEED_BACK_SNACKBAR_LONG:
                Snackbar.make(folderRecyclerView, msg, Snackbar.LENGTH_LONG).show();
                break;
            case FEED_BACK_SNACKBAR_INDEFINITE:
                Snackbar.make(folderRecyclerView, msg, Snackbar.LENGTH_INDEFINITE).show();
                break;
            default:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void renameItem(RenameEvent event) {
        if (event.isFolder) {
            folderAdapter.renameItem(event.id, event.newName);
        } else {
            fileAdapter.renameItem(event.id, event.newName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void encryptItem(EncryptOrDecryptEvent event) {
        if (event.encryptIt) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void decryptItem(EncryptOrDecryptEvent event) {
        if (!event.encryptIt) {

        }
    }
    private void buildDialogForRename(final UserFile file) {
        final WithOneInputDialogFragment dialog = WithOneInputDialogFragment.newInstance(file);
        dialog.setCallBack(new WithOneInputDialogFragment.CallBack() {
            @Override public void onOkClick(String text) {
                file.setFileName(text);
                mPresenter.rename(file);
                dialog.dismiss();
            }

            @Override public void onCancelClick() {
                dialog.dismiss();
            }
        }).show(getActivity().getFragmentManager(), TAG);
    }

    private void buildDialogForEncryptOrDecrypt(final UserFile file) {
        final WithOneInputDialogFragment dialog = WithOneInputDialogFragment.newInstance(file);
        String title = file.isEncrypted() ? "Decrypt " : "Encrypt ";
        dialog.setTitle(title.concat(file.getFileName()))
                .setCallBack(new WithOneInputDialogFragment.CallBack() {
                    @Override public void onOkClick(String text) {
                        mPresenter.encryptOrDecrypt(file, text);
                        dialog.dismiss();
                    }

                    @Override public void onCancelClick() {
                        dialog.dismiss();
                    }
                }).show(getActivity().getFragmentManager(), TAG);
    }

}
