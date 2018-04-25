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

package com.sky.xposed.weishi.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.sky.xposed.weishi.util.Alog

class ReceiverHelper {

    private var mContext: Context
    private var mCallback: (action: String, intent: Intent) -> Unit
    private var mIntentFilter: IntentFilter

    private var mHelperBroadcastReceiver: HelperBroadcastReceiver? = null

    companion object {

        fun buildIntentFilter(vararg actions: String): IntentFilter {

            val filter = IntentFilter()

            if (actions.isEmpty()) {
                // 暂无
                return filter
            }

            for (action in actions) {
                // 添加Action
                filter.addAction(action)
            }

            return filter
        }

        fun sendBroadcastReceiver(context: Context, action: String) {
            sendBroadcastReceiver(context, Intent(action))
        }

        fun sendBroadcastReceiver(context: Context, intent: Intent) {
            // 发送广播
            context.sendBroadcast(intent)
        }
    }

    constructor(context: Context, callback: (action: String, intent: Intent) -> Unit, vararg actions: String) :
            this(context, callback, buildIntentFilter(*actions))

    constructor(context: Context, callback: (action: String, intent: Intent) -> Unit, intentFilter: IntentFilter) {
        mContext = context
        mCallback = callback
        mIntentFilter = intentFilter
    }

    fun registerReceiver() {

        if (mHelperBroadcastReceiver != null) return

        try {
            mHelperBroadcastReceiver = HelperBroadcastReceiver()
            mContext.registerReceiver(mHelperBroadcastReceiver, mIntentFilter)
        } catch (e: Exception) {
            Alog.e("Exception", e)
        }
    }

    fun unregisterReceiver() {

        if (mHelperBroadcastReceiver == null) return

        try {
            mContext.unregisterReceiver(mHelperBroadcastReceiver)
            mHelperBroadcastReceiver = null
        } catch (e: Exception) {
            Alog.e("Exception", e)
        }
    }

    private inner class HelperBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // 直接回调出去就可以了
            mCallback.invoke(intent.action, intent)
        }
    }
}