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

package com.shiyan.netdisk_android.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shiyan.netdisk_android.data.DataSource;
import com.shiyan.netdisk_android.model.UserFile;
import com.shiyan.netdisk_android.utils.CallBack;
import com.shiyan.netdisk_android.utils.NetHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class RemoteDataSourceImpl implements DataSource {
    
    private final String TAG = getClass().getName();
    private final NetHelper netHelper ;

    private RemoteDataSourceImpl() {
        netHelper = NetHelper.getInstance() ;
    }

    public static RemoteDataSourceImpl getInstance() {

        return RemoteDBImplHolder.sInstance;
    }

    private static class RemoteDBImplHolder {
        private static final RemoteDataSourceImpl sInstance = new RemoteDataSourceImpl();
    }

    @Override
    public void createFolder(String folderName, int fromFolder, final ResultCallBack callBack) {
        try {
            netHelper.createFolder(fromFolder, folderName, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });

        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void deleteFolder(int id, final ResultCallBack callBack) {
        try {
            netHelper.deleteFolder(id, new CallBack() {
                @Override
                public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override
                public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        }catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void updateFolder(int id, String newName, final ResultCallBack callBack) {
        try {
            netHelper.updateFolder(id, newName, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });

        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    /**
     * encrypt all the files in the folder
     * @param id folder's id
     * @param passPhrase password
     * @param callBack callback when the server responses
     */
    @Override
    public void encryptFolder(int id, String passPhrase, final ResultCallBack callBack) {
        try {

            netHelper.encryptFolder(id, passPhrase, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });

        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void decryptFolder(int id, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.decryptFolder(id, passPhrase, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void getFolder(int id, final GetData callback) {
        netHelper.getFilesByFolder(id, new CallBack() {
            @Override public void success(@NonNull String data) {
                callback.onLoaded(data);
            }

            @Override public void error(@Nullable String error) {
                callback.onDataNotAvailable(error);
            }
        });
    }

    /**
     * Get files by folder from the server
     * @param id folder's id
     * @param callback when data is ready, callback the json string
     */
    @SuppressWarnings("unchecked")
    @Override
    public void getFilesByFolder(int id, final GetData callback) {
        netHelper.getFilesByFolder(id, new CallBack() {
            @Override
            public void success(@NonNull String data) {
                try {
                    Log.i(TAG, data);
                    JSONObject obj = new JSONObject(data);
                    String info = obj.getString("info");
                    callback.onLoaded(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable(e.toString());
                }
            }

            @Override
            public void error(@Nullable String error) {
                callback.onDataNotAvailable(error);
            }
        });
    }

    @Override
    public void encryptFile(int id, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.encryptFile(id, passPhrase, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void decryptFile(int id, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.decryptFile(id, passPhrase, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });

        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void copyFile(int id, int dstFolder, final ResultCallBack callBack) {
        try {
            netHelper.copyFile(id, dstFolder, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void deleteFiles(int id, final ResultCallBack callBack) {
        try {
            netHelper.deleteFile(id, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void moveFile(int id, int dstFolder, final ResultCallBack callBack) {
        try {
            netHelper.moveFile(id, dstFolder, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e){
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void updateFile(int id, String newName, final ResultCallBack callBack) {
        try {
            netHelper.updateFile(id, newName, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void shareFile(int id, final ResultCallBack callBack) {
        try {
            netHelper.shareFile(id, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onError(JSON_EXCEPTION);
        }
    }

    @Override
    public void cancelShare(int id, final ResultCallBack callBack) {
        try {
            netHelper.cancelSharingFile(id, new CallBack() {
                @Override public void success(@NonNull String data) {
                    callBack.onSuccess(data);
                }

                @Override public void error(@Nullable String error) {
                    callBack.onError(error);
                }
            });
        } catch (JSONException e) {
            callBack.onSuccess(JSON_EXCEPTION);
        }
    }

    @Override public void createFile(UserFile file, final ResultCallBack callBack) {

        netHelper.uploadFile(new File(file.getRemark()), file.getFileSize(), file.getFromFolder(), new CallBack() {
            @Override public void success(@NonNull String data) {
                callBack.onSuccess(data);
            }

            @Override public void error(@Nullable String error) {
                callBack.onError(error);
            }
        });
    }
}
