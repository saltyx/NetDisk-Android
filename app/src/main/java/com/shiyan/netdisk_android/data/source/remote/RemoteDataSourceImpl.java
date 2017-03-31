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
    public void createFolder(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.createFolder(file.getFromFolder(), file.getFileName(), new CallBack() {
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
    public void deleteFolder(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.deleteFolder(file.getId(), new CallBack() {
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
    public void updateFolder(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.updateFolder(file.getId(), file.getFileName(), new CallBack() {
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
     * @param file the file
     * @param passPhrase password
     * @param callBack callback when the server responses
     */
    @Override
    public void encryptFolder(UserFile file, String passPhrase, final ResultCallBack callBack) {
        try {

            netHelper.encryptFolder(file.getId(), passPhrase, new CallBack() {
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
    public void decryptFolder(UserFile file, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.decryptFolder(file.getId(), passPhrase, new CallBack() {
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
    public void encryptFile(UserFile file, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.encryptFile(file.getId(), passPhrase, new CallBack() {
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
    public void decryptFile(UserFile file, String passPhrase, final ResultCallBack callBack) {
        try {
            netHelper.decryptFile(file.getId(), passPhrase, new CallBack() {
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
    public void copyFile(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.copyFile(file.getId(), file.getFromFolder(), new CallBack() {
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
    public void deleteFiles(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.deleteFile(file.getId(), new CallBack() {
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
    public void moveFile(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.moveFile(file.getId(), file.getFromFolder(), new CallBack() {
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
    public void updateFile(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.updateFile(file.getId(), file.getFileName(), new CallBack() {
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
    public void shareFile(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.shareFile(file.getId(), new CallBack() {
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
    public void cancelShare(UserFile file, final ResultCallBack callBack) {
        try {
            netHelper.cancelSharingFile(file.getId(), new CallBack() {
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
