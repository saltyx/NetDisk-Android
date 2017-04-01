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

package com.shiyan.netdisk_android.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import org.json.JSONException;

import java.io.IOException;

/**
 * Contact shiyan233@hotmail.com
 * Blog    https://saltyx.github.io
 */

public class ImageLoader {

    private LruCache<Integer ,Bitmap> mCache;

    public static final long LRU_CACHE_SIZE = Runtime.getRuntime().maxMemory()/8;

    private ImageLoader() {
        mCache = new LruCache<>((int)LRU_CACHE_SIZE);
    }

    public static ImageLoader getInstance() {
        return ILHolder.sInstance;
    }

    private static class ILHolder {
        private final static ImageLoader sInstance = new ImageLoader();
    }

    public Bitmap getImage(final int id) throws JSONException, IOException {
        Bitmap result = mCache.get(id);
        if (result != null) return result;
        result = NetHelper.getInstance().getFile(id);
        if (result == null) return null;
        mCache.put(id, result);
        return result;

    }
}
