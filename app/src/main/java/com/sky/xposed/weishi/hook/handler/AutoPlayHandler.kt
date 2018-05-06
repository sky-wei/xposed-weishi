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

import android.view.ViewGroup
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.RandomUtil
import com.sky.xposed.weishi.util.VToast
import de.robv.android.xposed.XposedHelpers

class AutoPlayHandler(hookManager: HookManager) : CommonHandler(hookManager), Runnable {

    private var isPlaying = false

    fun setAutoPlay(play: Boolean) {

        if (play) {
            // 开始自动播放
            startPlay()
        } else {
            if (isPlaying) VToast.show("停止自动播放")
            // 关闭自动播放
            stopPlay()
        }
    }

    fun startPlay() {
        startPlay(RandomUtil.randomLong(15000, 20000))
    }

    fun startPlay(delayMillis: Long) {

        if (!getConfigManager().isAutoPlay()) {
            // 不进行播放处理
            return
        }

        if (isPlaying) {
            Alog.d("正在自动播放视频")
            return
        }

        // 标识正在播放
        isPlaying = true

        // 播放下一个
        playNext(delayMillis)
    }

    fun stopPlay() {
        isPlaying = false
        getHandler().removeCallbacks(this)
    }

    private fun playNext(delayMillis: Long) {
        // 开始播放
        getHandler().postDelayed(this, delayMillis)
        VToast.show((delayMillis / 1000).toString() + "秒后播放下一个视频")
    }

    override fun run() {

        if (!isPlaying) return

        val mViewPager = getObjectManager().getViewPager() as ViewGroup

        if (mViewPager == null || !getConfigManager().isAutoPlay()) {
            // 停止播放处理
            stopPlay()
            return
        }

        if (!mViewPager.isShown) {
            // 停止播放(界面不可见了)
            stopPlay()
            VToast.show("界面切换，自动播放将暂停")
            return
        }

        // 获取当前页
        val currentItem = XposedHelpers
                .callMethod(mViewPager, "getCurrentItem") as Int

        // 切换页面
        XposedHelpers.callMethod(mViewPager, "a",
                *arrayOf(currentItem + 1, true, true, -1270 + RandomUtil.random(100)))

        // 继续播放下一个
        playNext(RandomUtil.randomLong(15000, 20000))
    }
}