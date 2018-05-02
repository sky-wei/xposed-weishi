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

package com.sky.xposed.weishi.hook

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.data.CachePreferences
import com.sky.xposed.weishi.helper.ReceiverHelper
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.*

class HookManager private constructor() {

    private lateinit var mContext: Context
    private lateinit var mHandler: Handler
    private lateinit var mLoadPackageParam: XC_LoadPackage.LoadPackageParam
    private lateinit var mCachePreferences: CachePreferences
    private lateinit var mReceiverHelper: ReceiverHelper

    companion object {

        private val HOOK_MANAGER by lazy { HookManager() }

        fun getInstance()= HOOK_MANAGER
    }

    fun initialization(context: Context, param: XC_LoadPackage.LoadPackageParam): HookManager {

        mContext = context
        mHandler = AppHandler()
        mLoadPackageParam = param
        mCachePreferences = CachePreferences(context, Constant.Name.WEI_SHI)

        // 注册监听
        mReceiverHelper = ReceiverHelper(context,
                { action, intent ->  onReceive(action, intent) },
                Constant.Action.REFRESH_PREFERENCE)

        return this
    }

    fun handleLoadPackage() {

    }

    fun release() {

        // 释放监听
        mReceiverHelper.unregisterReceiver()
    }

    private fun onReceive(action: String, intent: Intent) {

        if (Constant.Action.REFRESH_PREFERENCE == action) {

            // 获取刷新的值
            val data = intent.getSerializableExtra(
                    Constant.Key.DATA) as ArrayList<Pair<String, Any>>

            for ((first, second) in data) {
                // 重新设置值
                mCachePreferences.putAny(first, second)
            }
        }
    }

    private class AppHandler : Handler()
}