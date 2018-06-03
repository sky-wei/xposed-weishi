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

package com.sky.xposed.weishi.data

import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.hook.HookManager

class UserConfigManager(hookManager: HookManager) {

    private var mCachePreferences: CachePreferences = hookManager.getCachePreferences()

    fun isAutoPlay(): Boolean {
        return getBoolean(Constant.Preference.AUTO_PLAY)!!
    }

    fun isAutoAttention(): Boolean {
        return getBoolean(Constant.Preference.AUTO_ATTENTION)!!
    }

    fun isAutoLike(): Boolean {
        return getBoolean(Constant.Preference.AUTO_LIKE)!!
    }

    fun isAutoComment(): Boolean {
        return getBoolean(Constant.Preference.AUTO_COMMENT)!!
    }

    fun isAutoSaveVideo(): Boolean {
        return getBoolean(Constant.Preference.AUTO_SAVE_VIDEO)!!
    }

    fun isRemoveLimit(): Boolean {
        return getBoolean(Constant.Preference.REMOVE_LIMIT)!!
    }

    fun getCommentMessage(): String {
        return getString(Constant.Preference.AUTO_COMMENT_MESSAGE)
    }

    private fun getBoolean(key: String): Boolean? {
        return mCachePreferences.getBoolean(key, false)
    }

    private fun getString(key: String): String {
        return mCachePreferences.getString(key, "")
    }
}