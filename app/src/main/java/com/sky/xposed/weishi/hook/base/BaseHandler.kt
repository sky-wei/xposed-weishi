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

import android.view.View
import com.sky.xposed.common.util.ResourceUtil
import com.sky.xposed.weishi.hook.HookManager

open class BaseHandler(private val mHookManager: HookManager) {

    val mContext by lazy { mHookManager.getContext() }
    val mCachePreferences by lazy { mHookManager.getCachePreferences() }
    val mUserConfigManager by lazy { mHookManager.getUserConfigManager() }
    val mObjectManager by lazy { mHookManager.getObjectManager() }
    val mVersionManager by lazy { mHookManager.getVersionManager() }
    val mHandler by lazy { mHookManager.getHandler() }

    fun findViewById(view: View?, id: String): View? {

        return view?.findViewById(ResourceUtil.getId(mContext, id))

    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mHandler.postDelayed(runnable, delayMillis)
    }

    fun removeCallbacks(runnable: Runnable) {
        mHandler.removeCallbacks(runnable)
    }

    fun mainPerformClick(viewGroup: View, id: String) {

        mHandler.post { performClick(viewGroup, id) }
    }

    fun mainPerformClick(view: View?) {

        mHandler.post { performClick(view) }
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