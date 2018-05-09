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

import android.content.Context
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.PackageUitl
import java.util.*

class ConfigManager(hookManager: HookManager) {

    private val VERSION_MAP = HashMap<String, Class<out VersionConfig>>()

    private var mContext: Context = hookManager.getContext()
    private var mCachePreferences: CachePreferences = hookManager.getCachePreferences()
    private var mVersionConfig: VersionConfig? = null

    init {
//        VERSION_MAP["4.0.1.88"] = VersionConfig40188::class.java
        VERSION_MAP["4.2.0.88"] = VersionConfig42088::class.java
    }

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

    fun isSupportVersion(): Boolean {

        val info = getPackageInfo() ?: return false

        return VERSION_MAP.containsKey(info.versionName)
    }

    fun getVersionConfig(): VersionConfig? {

        val info = getPackageInfo() ?: return null

        return getSupportVersionConfig(VERSION_MAP[info.versionName])
    }

    private fun getSupportVersionConfig(vClass: Class<out VersionConfig>?): VersionConfig? {

        if (vClass == null) return null

        if (mVersionConfig == null) {
            try {
                // 创建实例
                mVersionConfig = vClass.newInstance()
            } catch (tr: Throwable) {
                Alog.d("创建版本配置异常", tr)
            }

        }
        return mVersionConfig
    }

    private fun getPackageInfo(): PackageUitl.SimplePackageInfo? {
        return PackageUitl.getSimplePackageInfo(mContext, mContext.packageName)
    }

    class VersionConfig42088 : VersionConfig()

    class VersionConfig40188 : VersionConfig()

    open class VersionConfig {

    }
}