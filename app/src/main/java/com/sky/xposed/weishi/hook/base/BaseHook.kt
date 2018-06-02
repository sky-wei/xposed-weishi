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

package com.sky.xposed.weishi.hook.base

import android.app.ActivityThread
import android.content.Context
import com.sky.xposed.weishi.data.CachePreferences
import com.sky.xposed.weishi.data.ConfigManager
import com.sky.xposed.weishi.data.ObjectManager
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.Alog
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class BaseHook {

    private lateinit var mParam: XC_LoadPackage.LoadPackageParam

    fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        this.mParam = lpparam

        try {
            // 处理
            onHandleLoadPackage(lpparam)
        } catch (tr: Throwable) {
            Alog.e("handleLoadPackage异常", tr)
        }
    }

    abstract fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam)

    fun getHookManager(): HookManager {
        return HookManager.getInstance()
    }

    fun getCachePreferences(): CachePreferences {
        return getHookManager().getCachePreferences()
    }

    fun getConfigManager(): ConfigManager {
        return getHookManager().getConfigManager()
    }

    fun getObjectManager(): ObjectManager {
        return getHookManager().getObjectManager()
    }

    fun getContext(): Context {
        return getHookManager().getContext()
    }

    fun getSystemContext(): Context {
        return ActivityThread.currentActivityThread().systemContext
    }

    fun getLoadPackageParam(): XC_LoadPackage.LoadPackageParam {
        return mParam
    }

    fun getProcessName(): String {
        return mParam.processName
    }
}