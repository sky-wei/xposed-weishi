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

package com.sky.xposed.weishi.hook.handler

import android.view.View
import com.sky.xposed.common.util.RandomUtil
import com.sky.xposed.weishi.hook.HookManager
import de.robv.android.xposed.XposedHelpers

class AutoLikeHandler(hookManager: HookManager) : CommonHandler(hookManager), Runnable {

    fun like() {

        if (!mUserConfigManager.isAutoLike()) {
            // 不需要处理
            return
        }

        // 开始点赞
        postDelayed(this, RandomUtil.random(1500, 2500).toLong())
    }

    fun cancel() {
        removeCallbacks(this)
    }

    override fun run() {

        // 获取当前显示的下标
        val position = getCurrentPosition()

        if (position < 0) return

        // 获取当前显示的ViewHolder com.tencent.oscar.module.feedlist.c.aa$b
        val viewHolder = getViewHolder(position) ?: return
        val itemView = XposedHelpers
                .getObjectField(viewHolder, mVersionConfig.fieldViewHolder) as View

        val likeContainer = findViewById(itemView, mVersionConfig.idLikeContainer)
        val likeStatus2 = findViewById(itemView, mVersionConfig.idLikeStatus2)

        if (likeStatus2 != null && likeStatus2.isShown) {
            // 已经点赞了，不需要处理
            return
        }

        // 点赞
        mainPerformClick(likeContainer)
    }
}