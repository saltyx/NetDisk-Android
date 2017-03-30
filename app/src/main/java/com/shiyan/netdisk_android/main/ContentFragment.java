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


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shiyan.netdisk_android.R;
import com.shiyan.netdisk_android.SecuDiskApplication;
import com.shiyan.netdisk_android.adapter.FileAdapter;
import com.shiyan.netdisk_android.adapter.RecentFileAdapter;
import com.shiyan.netdisk_android.adapter.FolderAdapter;
import com.shiyan.netdisk_android.adapter.GridSpaceItemDecoration;
import com.shiyan.netdisk_android.dialog.DetailInfoDialogFragment;
import com.shiyan.netdisk_android.dialog.WithOneInputDialogFragment;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.SerializeUserFile;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.BaseFile;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_INDEFINITE;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_LONG;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_SNACKBAR_SHORT;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_TOAST_LONG;
import static com.shiyan.netdisk_android.main.MainContract.FEED_BACK_TOAST_SHORT;
import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements MainContract.View , SwipeRefreshLayout.OnRefreshListener {

    final String TAG = getClass().getName();
    final static String KEY_BUNDLE = "KEY_BUNDLE";
    List<BaseFile> filePaths;

    final static int MSG_SHOW_FOLDERS = 0x0000;
    final static int MSG_SHOW_FILES = 0x0001;
    final static int MSG_SHOW_BY_LIST = 0x0002;
    final static int MSG_SHOW_BY_GRID = 0x0003;
    final static int MSG_REMOVE = 0x0004;
    final static int MSG_RENAME = 0x0005;
    final static int MSG_FEED_BACK = 0x0006;
    final static int MSG_ENCRYPT = 0x0007;
    final static int MSG_DECRYPT = 0x0008;
    final static int MSG_ADD_FILE = 0x0009;
    final static int MSG_ADD_FOLDER = 0x000a;
    final static int MSG_SET_TITLE = 0x000b;
    final static int MSG_SHOW_HIDE_RECENT = 0x000c;

    final static int MSG_REFRESH = 0xf007;

    MainContract.Presenter mPresenter;
    FolderAdapter mFolderAdapter = null;
    RecentFileAdapter mRecentFileAdapter = null;
    FileAdapter mFileAdapter = null;
    List<UserFile> data;
    List<UserFile> allFileData = null;
    List<UserFile> allFolderData = null;
    boolean isGrid;
    boolean isRefresh;
    String title;

    @BindView(R.id.title) TextView mTitleView;
    @BindView(R.id.detailed_folder) RecyclerView mFolderRecyclerView;
    @BindView(R.id.recent_files) RecyclerView RecentFileRecyclerView;
    @BindView(R.id.swipeRerfreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.float_plus) FloatingActionsMenu mFloatBtnPlus;
    @BindView(R.id.upload_photo) FloatingActionButton mFloatBtnUpload;
    @BindView(R.id.upload_doc) FloatingActionButton mFloatBtnUploadDoc;
    @BindView(R.id.create_folder) FloatingActionButton mFloatBtnCreateFolder;
    @BindView(R.id.detailed_files) RecyclerView mFilesRView;

    @OnClick(R.id.upload_video) void onUploadVideoClick() {
        mFloatBtnPlus.collapse();
        Intent intent2 = new Intent(getActivity(), VideoPickActivity.class);
        intent2.putExtra(IS_NEED_CAMERA, true);
        intent2.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
    }

    @OnClick(R.id.upload_doc) void onUploadFileClick() {
        mFloatBtnPlus.collapse();
        Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
        intent4.putExtra(Constant.MAX_NUMBER, 1);
        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf","zip"});
        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
    }

    @OnClick(R.id.upload_photo) void doUploadPhoto() {
        mFloatBtnPlus.collapse();
        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    @OnClick(R.id.create_folder) void createFolder() {
        mFloatBtnPlus.collapse();
        buildDialogForCreateNewFolder();
    }

    public ContentFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, root);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        mFolderRecyclerView.addItemDecoration(new GridSpaceItemDecoration(12));
        mFilesRView.addItemDecoration(new GridSpaceItemDecoration(12));


        return root;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    filePaths = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                }
                break;
            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    filePaths = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                }
                break;
            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    filePaths = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                }
                break;
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    filePaths = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                }
                break;
        }
        if (filePaths == null || filePaths.isEmpty()) {
            userFeedBack("Nothing gonna happen :(",FEED_BACK_SNACKBAR_LONG);
        } else {
            mPresenter.uploadCommonFile(filePaths);
            filePaths.clear();
        }
    }

    @Override public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            mPresenter.set(SecuDiskApplication.CurrentFolder);
        }
    }

    @Override public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override public String getTitle() {
        return this.title;
    }

    @Override public void remove(UserFile file) {
        buildMessage(MSG_REMOVE, file);
    }

    @Override public void rename(UserFile file) {
        buildMessage(MSG_RENAME, file);
    }

    @Override public void encrypt(UserFile file) {
        buildMessage(MSG_ENCRYPT, file);
    }

    @Override public void decrypt(UserFile file) {
        buildMessage(MSG_DECRYPT, file);
    }

    @Override public void showByGrid() {
        buildMessage(MSG_SHOW_BY_GRID, null);
    }

    @Override public void showByList() {
        buildMessage(MSG_SHOW_BY_LIST, null);
    }

    @Override public void add(UserFile file) {
        buildMessage(MSG_ADD_FILE, file);
    }

    @Override public void addFolder(UserFile file) {
        buildMessage(MSG_ADD_FOLDER, file);
    }

    @Override public void setTitle(String title) {
        Message message = Message.obtain();
        message.arg1 = MSG_SET_TITLE;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, title);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @Override public void showOrHideRecentFile(boolean showOrHide) {
        Message message = Message.obtain();
        message.arg1 = MSG_SHOW_HIDE_RECENT;
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_BUNDLE, showOrHide);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private void buildMessage(int type, @Nullable final UserFile file) {
        Message message = Message.obtain();
        message.arg1 = type;
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BUNDLE, file);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @Override public void showFiles(String filesJson) {
        Message showFilesMsg = Message.obtain();
        showFilesMsg.arg1 = MSG_SHOW_FILES;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, filesJson);
        showFilesMsg.setData(bundle);
        mHandler.sendMessage(showFilesMsg);
    }

    @Override public void showFolders(String filesJson) {
        Message showFolder = Message.obtain();
        showFolder.arg1 = MSG_SHOW_FOLDERS;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE, filesJson);
        showFolder.setData(bundle);
        mHandler.sendMessage(showFolder);
    }

    @Override public void toggle() {
        if (isGrid) {
            showByList();
        } else {
            showByGrid();
        }
    }

    @Override public void userFeedBack(String msg, int type) {
        if (msg != null) {
            Message feedback = Message.obtain();
            feedback.arg1 = MSG_FEED_BACK;
            feedback.arg2 = type;
            Bundle bundle = new Bundle();
            bundle.putString(KEY_BUNDLE, msg);
            feedback.setData(bundle);
            mHandler.sendMessage(feedback);
        }
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override public void refresh(boolean refresh) {
        Message fresh = Message.obtain();
        fresh.arg1 = MSG_REFRESH;
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_BUNDLE, refresh);
        fresh.setData(bundle);
        mHandler.sendMessage(fresh);
    }

    public void setRefresh(boolean event) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(event);
        }
    }

    public void updateFiles(String files) {
        try {
            data = SerializeUserFile.serialize(files);
            allFileData = SerializeUserFile.serializeFile(files);
            allFolderData = SerializeUserFile.serializeFolder(files);

            if (mRecentFileAdapter == null) {
                mRecentFileAdapter = new RecentFileAdapter(data, false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecentFileRecyclerView.setLayoutManager(linearLayoutManager);
                RecentFileRecyclerView.setAdapter(mRecentFileAdapter);
            } else {
                mRecentFileAdapter.changeData(data);
            }

            if (mFileAdapter == null) {
                mFileAdapter = new FileAdapter(allFileData);
                mFilesRView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mFilesRView.setAdapter(mFileAdapter);
            } else {
                mFileAdapter.changeData(allFileData);
            }

        } catch (JSONException e) {
            userFeedBack(e.toString(), FEED_BACK_SNACKBAR_INDEFINITE);
        }
    }

    public void updateFolder(String folders) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        isRefresh = false;

        try {
            isGrid = true;
            allFolderData = SerializeUserFile.serializeFolder(folders);
            Log.i(TAG, "updateFolder: ".concat(String.valueOf(allFolderData.size())));
            if (mFolderAdapter == null) {
                mFolderAdapter = new FolderAdapter(allFolderData,
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

                            @Override public void onItemClick(UserFile file) {
                                mPresenter.goToNextFolder(file);
                            }
                        });

                mFolderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mFolderRecyclerView.setAdapter(mFolderAdapter);
            } else {
                mFolderAdapter.changeData(allFolderData);
            }
        } catch (JSONException e) {
            userFeedBack(e.toString(),FEED_BACK_SNACKBAR_INDEFINITE);
        }
    }

    public void changeLayout(Boolean toGrid) {
        if (mFolderAdapter == null) {
            mFolderAdapter = new FolderAdapter(allFolderData, new FolderAdapter.CallBack() {
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

                @Override public void onItemClick(UserFile file) {
                    mPresenter.goToNextFolder(file);
                }
            });
        }

        if (toGrid) {
            isGrid = true;
            mFolderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mFolderRecyclerView.setAdapter(mFolderAdapter);
        } else {
            isGrid = false;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mFolderRecyclerView.setLayoutManager(linearLayoutManager);
            mFolderRecyclerView.setAdapter(mFolderAdapter);

        }
    }

    public void removeItem(UserFile file) {
        if (file.isFolder()) {
            if (mFolderAdapter == null) return;
            mFolderAdapter.remove(file);
        } else {
            if (mRecentFileAdapter != null) mRecentFileAdapter.remove(file);
            if (mFileAdapter != null) mFileAdapter.remove(file);
        }
    }

    public void feedback(final String msg,final int type) {
        Log.i(TAG, msg);
        switch (type) {
            case FEED_BACK_TOAST_SHORT:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                break;
            case FEED_BACK_TOAST_LONG:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                break;
            case FEED_BACK_SNACKBAR_SHORT:
                Snackbar.make(mFolderRecyclerView, msg, Snackbar.LENGTH_SHORT).show();
                break;
            case FEED_BACK_SNACKBAR_LONG:
                Snackbar.make(mFolderRecyclerView, msg, Snackbar.LENGTH_LONG).show();
                break;
            case FEED_BACK_SNACKBAR_INDEFINITE:
                Snackbar.make(mFolderRecyclerView, msg, Snackbar.LENGTH_INDEFINITE).show();
                break;
            default:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void renameItem(UserFile file) {
        if (file.isFolder()) {
            mFolderAdapter.renameItem(file);
        } else {
            mRecentFileAdapter.renameItem(file);
            mFileAdapter.renameItem(file);
        }
    }

    public void encryptItem(UserFile file) {
        if (file.isEncrypted()) {
            //do encrypt
        }
    }

    public void decryptItem(UserFile file) {
        if (!file.isEncrypted()) {
            // do decrypt
        }
    }

    public void addItem(UserFile file) {
        mRecentFileAdapter.addItem(file);
        mFileAdapter.addItem(file);
    }

    public void setTitleItem(String title) {
        mTitleView.setText(title);
        this.title = title;
        Log.i(TAG, "setTitleItem: ".concat(title));
    }

    public void showOrHideRecentItems(boolean showOrHide) {
        if (showOrHide) {
            //show
            RecentFileRecyclerView.setVisibility(View.VISIBLE);
        } else {
            //hide
            RecentFileRecyclerView.setVisibility(View.GONE);
        }
    }

    public void addFolderItem(UserFile file) {
        mFolderAdapter.addItem(file);
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

    private void buildDialogForCreateNewFolder() {
        final WithOneInputDialogFragment dialog = WithOneInputDialogFragment.newInstance(null);
        dialog.setTitle("Create Folder")
                .setCallBack(new WithOneInputDialogFragment.CallBack() {
                    @Override public void onOkClick(String text) {
                        UserFile file = new UserFile();
                        file.setFileName(text);
                        file.setFromFolder(SecuDiskApplication.CurrentFolder);
                        mPresenter.createFolder(file);
                        dialog.dismiss();
                    }

                    @Override public void onCancelClick() {
                        dialog.dismiss();
                    }
                }).show(getActivity().getFragmentManager(), TAG);
    }

    private static class MHandler extends Handler {

        private final WeakReference<ContentFragment> mContentFragment;

        MHandler(ContentFragment fragment) {
            mContentFragment = new WeakReference<>(fragment);
        }

        @Override public void handleMessage(Message msg) {
            ContentFragment fragment = mContentFragment.get();
            if (fragment != null) {
                switch (msg.arg1) {
                    case MSG_SHOW_FOLDERS: case  MSG_SHOW_FILES:
                        String str = msg.getData().getString(KEY_BUNDLE);
                        fragment.updateFiles(str); fragment.updateFolder(str);
                        break;
                    case MSG_SHOW_BY_LIST:
                        fragment.changeLayout(false);
                        break;
                    case MSG_SHOW_BY_GRID:
                        fragment.changeLayout(true);
                        break;
                    case MSG_REMOVE:
                        fragment.removeItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_RENAME:
                        fragment.renameItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_FEED_BACK:
                        fragment.feedback(msg.getData().getString(KEY_BUNDLE),
                                msg.arg2);
                        break;
                    case MSG_ENCRYPT:
                        fragment.encryptItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_DECRYPT:
                        fragment.decryptItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_REFRESH:
                        fragment.setRefresh(msg.getData().getBoolean(KEY_BUNDLE));
                        break;
                    case MSG_ADD_FILE:
                        fragment.addItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_ADD_FOLDER:
                        fragment.addFolderItem((UserFile) msg.getData().getParcelable(KEY_BUNDLE));
                        break;
                    case MSG_SET_TITLE:
                        fragment.setTitleItem(msg.getData().getString(KEY_BUNDLE));
                        break;
                    case MSG_SHOW_HIDE_RECENT:
                        fragment.showOrHideRecentItems(msg.getData().getBoolean(KEY_BUNDLE));
                        break;
                }
            }
        }
    }

    private final MHandler mHandler = new MHandler(this);

}
