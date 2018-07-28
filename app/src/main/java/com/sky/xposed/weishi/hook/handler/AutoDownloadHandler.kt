/*
 * Copyright (c) 2018 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.weishi.hook.handler

import android.os.Environment
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.ui.util.CommUtil
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.MD5Util
import com.sky.xposed.weishi.util.RandomUtil
import com.sky.xposed.weishi.util.VToast
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import de.robv.android.xposed.XposedHelpers
import okhttp3.Call
import java.io.File

class AutoDownloadHandler(hookManager: HookManager) : CommonHandler(hookManager), Runnable {

    private val TAG = "AutoDownloadHandler"

    private val mDownloadDir: File = File(
            Environment.getExternalStorageDirectory(), "DCIM")

    /**
     * 延时下载视频
     */
    fun download() {

        if (!mUserConfigManager.isAutoSaveVideo()) {
            return
        }

        // 下载视频
        postDelayed(this, RandomUtil.randomLong(500, 1200))
    }

    fun cancel() {
        // 取消自动下载
        removeCallbacks(this)
    }

    /**
     * 下载当前视频到本地
     */
    fun downloadToLocal() {
        // 下载视频
        download(getAdapterItem(getCurrentPosition()), true)
    }

    override fun run() {
        // 开始下载
        download(getAdapterItem(getCurrentPosition()))
    }

    private fun download(data: Any?) {
        download(data, false)
    }

    private fun download(data: Any?, skip: Boolean) {

        if (data == null) {
            Alog.d("download data is null!")
            return
        }

        if (!skip && !mUserConfigManager.isAutoSaveVideo()) {
            return
        }

        try {
            if (!mDownloadDir.exists()) mDownloadDir.mkdir()

            // 下载视频
            downloadVideo(XposedHelpers
                    .getObjectField(data, mVersionConfig.fieldItemModeVideoUrl) as String)
        } catch (tr: Throwable) {
            Alog.e(TAG, "下载异常了", tr)
        }

    }

    private fun downloadVideo(url: String) {

        val fileName = "${MD5Util.md5sum(url)}.mp4"
        val downloadFile = File(mDownloadDir, fileName)

        if (downloadFile.exists()) {
            VToast.show("视频文件本地已存在不需要下载！")
            return
        }

        VToast.show("开始下载当前视频")

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : FileCallBack(mDownloadDir.path, fileName) {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Alog.e(TAG, e)
                        VToast.show("视频下载错误！")
                    }

                    override fun onResponse(response: File, id: Int) {
                        Alog.e(TAG, "onResponse :$response")
                        VToast.show("视频下载完成：" + response.path)
                        CommUtil.scanFile(mContext, response.path)
                    }
                })

    }
}