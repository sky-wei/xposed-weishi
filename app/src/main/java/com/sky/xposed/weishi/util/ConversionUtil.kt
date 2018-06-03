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

/**
 * Created by sky on 18-6-3.
 */
object ConversionUtil {

    private val TAG = "ConversionUtils"

    fun parseInt(value: String): Int {
        return parseInt(value, 0)
    }

    fun parseInt(value: String, defaultValue: Int): Int {

        var result = defaultValue

        if (!TextUtils.isEmpty(value)) {
            try {
                result = value.toInt()
            } catch (e: NumberFormatException) {
                Alog.e(TAG, "NumberFormatException", e)
            }
        }
        return result
    }

    fun parseLong(value: String): Long {
        return parseLong(value, 0L)
    }

    fun parseLong(value: String, defaultValue: Long): Long {

        var result = defaultValue

        if (!TextUtils.isEmpty(value)) {
            try {
                result = value.toLong()
            } catch (e: NumberFormatException) {
                Alog.e(TAG, "NumberFormatException", e)
            }
        }
        return result
    }

    fun parseFloat(value: String): Float {
        return parseFloat(value, 0.0f)
    }

    fun parseFloat(value: String, defaultValue: Float): Float {

        var result = defaultValue

        if (!TextUtils.isEmpty(value)) {
            try {
                result = value.toFloat()
            } catch (e: NumberFormatException) {
                Alog.e(TAG, "NumberFormatException", e)
            }
        }
        return result
    }

    fun parseBoolean(value: String, defaultValue: Boolean): Boolean {

        var result = defaultValue

        if (!TextUtils.isEmpty(value)) {
            result = value.toBoolean()
        }
        return result
    }

    fun parseBoolean(value: String): Boolean {
        return parseBoolean(value, false)
    }

    fun booleanValue(value: Boolean?, defaultValue: Boolean): Boolean {
        return value ?: defaultValue
    }

    fun booleanValue(value: Boolean?): Boolean {
        return booleanValue(value, false)
    }

    fun intValue(value: Int?): Int {
        return intValue(value, 0)
    }

    fun intValue(value: Number?, defaultValue: Int): Int {
        return value?.toInt() ?: defaultValue
    }

    fun intValue(value: Number): Int {
        return intValue(value, 0)
    }

    fun longValue(value: Long?, defaultValue: Long): Long {
        return value ?: defaultValue
    }

    fun longValue(value: Long?): Long {
        return longValue(value, 0L)
    }

    fun floatValue(value: Float?, defaultValue: Float): Float {
        return value ?: defaultValue
    }

    fun floatValue(value: Float?): Float {
        return floatValue(value, 0.0f)
    }
}