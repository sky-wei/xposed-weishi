/*
 * Copyright (c) 2018. The sky Authors
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

package com.sky.xposed.weishi.data

import android.content.Context

class CachePreferences(mContext: Context, mName: String) {

    private val mMap = HashMap<String, Any?>()

    init {
        val preferences = mContext
                .getSharedPreferences(mName, Context.MODE_PRIVATE)

        // 加载所有
        mMap.putAll(preferences.all)
    }

    fun getString(key: String, defValue: String): String {
        return mMap[key] as String? ?: defValue
    }

    fun getInt(key: String, defValue: Int): Int {
        return mMap[key] as Int? ?: defValue
    }

    fun getLong(key: String, defValue: Long): Long {
        return mMap[key] as Long? ?: defValue
    }

    fun getFloat(key: String, defValue: Float): Float {
        return mMap[key] as Float? ?: defValue
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mMap[key] as Boolean? ?: defValue
    }

    fun contains(key: String): Boolean {
        return mMap.containsKey(key)
    }

    fun putString(key: String, value: String) {
        mMap[key] = value
    }

    fun putInt(key: String, value: Int) {
        mMap[key] = value
    }

    fun putLong(key: String, value: Long) {
        mMap[key] = value
    }

    fun putFloat(key: String, value: Float) {
        mMap[key] = value
    }

    fun putBoolean(key: String, value: Boolean) {
        mMap[key] = value
    }

    fun putAny(key: String, value: Any) {
        mMap[key] = value
    }
}