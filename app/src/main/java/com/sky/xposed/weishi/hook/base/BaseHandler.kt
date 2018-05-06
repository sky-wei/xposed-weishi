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

package com.sky.xposed.weishi.hook.base

import android.content.Context
import android.os.Handler
import android.view.View
import com.sky.xposed.weishi.data.CachePreferences
import com.sky.xposed.weishi.data.ConfigManager
import com.sky.xposed.weishi.data.ObjectManager
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.ResourceUtil

open class BaseHandler(private val mHookManager: HookManager) {

    fun getContext(): Context {
        return mHookManager.getContext()
    }

    fun getHandler(): Handler {
        return mHookManager.getHandler()
    }

    fun getCachePreferences(): CachePreferences {
        return mHookManager.getCachePreferences()
    }

    fun getConfigManager(): ConfigManager {
        return mHookManager.getConfigManager()
    }

    fun getObjectManager(): ObjectManager {
        return mHookManager.getObjectManager()
    }

    fun findViewById(view: View?, id: String): View? {

        return view?.findViewById(ResourceUtil.getId(getContext(), id))

    }

    fun mainPerformClick(viewGroup: View, id: String) {

        getHandler().post { performClick(viewGroup, id) }
    }

    fun mainPerformClick(view: View) {

        getHandler().post { performClick(view) }
    }

    fun performClick(viewGroup: View?, id: String) {

        if (viewGroup == null) return

        val context = viewGroup.context
        performClick(viewGroup.findViewById(ResourceUtil.getId(context, id)))
    }

    fun performClick(view: View?) {

        if (view != null && view.isShown) {
            // 点击
            view.performClick()
        }
    }
}