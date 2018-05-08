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

import android.text.TextUtils
import android.util.Log
import java.security.MessageDigest

object MD5Util {

    private val TAG = "MD5Util"

    /**
     * Convert byte[] to hex string
     * @param src byte[] data
     * @return hex string
     */
    fun bytesToHexString(src: ByteArray?): String? {

        if (src == null || src.isEmpty()) return null

        val builder = StringBuilder()

        for (i in src.indices) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                builder.append(0)
            }
            builder.append(hv)
        }
        return builder.toString()
    }

    fun hexStringToBytes(hexString: String): ByteArray? {
        var hexString = hexString

        if (TextUtils.isEmpty(hexString)) return null

        hexString = hexString.toUpperCase()
        val length = hexString.length / 2
        val hexChars = hexString.toCharArray()
        val bytes = ByteArray(length)
        for (i in 0 until length) {
            val pos = i * 2
            bytes[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
        }
        return bytes
    }

    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    fun md5sum(value: String): String? {
        return md5sum(value, "UTF-8")
    }

    fun md5sum(value: String, charsetName: String): String? {

        if (TextUtils.isEmpty(value)) return null

        try {
            return md5sum(value.toByteArray(charset(charsetName)))
        } catch (e: Exception) {
            Log.e(TAG, "字符串编码异常", e)
        }

        return null
    }

    fun md5sum(value: ByteArray?): String? {

        if (value == null) return null

        try {
            // 计算MD5值信息
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(value)

            val bytes = messageDigest.digest()

            return bytesToHexString(bytes)
        } catch (e: Exception) {
            Log.e(TAG, "处理MD5异常", e)
        }

        return null
    }
}