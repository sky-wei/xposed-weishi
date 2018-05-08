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

package com.sky.xposed.weishi.util

import android.util.Log
import java.security.MessageDigest

object MD5Util {

    private val TAG = "MD5Util"

    fun md5sum(value: String): ByteArray? {
        return md5sum(value, "UTF-8")
    }

    fun md5sum(value: String, charsetName: String): ByteArray? {
        try {
            return md5sum(value.toByteArray(charset(charsetName)))
        } catch (e: Exception) {
            Log.e(TAG, "字符串编码异常", e)
        }
        return null
    }

    fun md5sum(value: ByteArray?): ByteArray? {

        if (value == null) return null

        try {
            // 计算MD5值信息
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(value)

            return messageDigest.digest()
        } catch (e: Exception) {
            Log.e(TAG, "处理MD5异常", e)
        }
        return null
    }
}