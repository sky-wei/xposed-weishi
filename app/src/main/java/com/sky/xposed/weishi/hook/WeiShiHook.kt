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

package com.sky.xposed.weishi.hook

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.hook.base.BaseHook
import com.sky.xposed.weishi.hook.handler.AutoAttentionHandler
import com.sky.xposed.weishi.hook.handler.AutoLikeHandler
import com.sky.xposed.weishi.hook.handler.AutoPlayHandler
import com.sky.xposed.weishi.ui.dialog.SettingsDialog
import com.sky.xposed.weishi.ui.util.ViewUtil
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WeiShiHook : BaseHook() {

    private val mAutoPlayHandler: AutoPlayHandler = AutoPlayHandler(getHookManager())
    private val mAutoLikeHandler: AutoLikeHandler = AutoLikeHandler(getHookManager())
    private val mAutoAttentionHandler: AutoAttentionHandler = AutoAttentionHandler(getHookManager())

    override fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        debugWeiShiHook()

        // 注入UI设置入口
        injectionUISettings()

        autoPlayHook()

        videoSwitchHook()

        testHook()
    }

    /**
     * 注入UI配置入口
     */
    private fun injectionUISettings() {

        findAndBeforeHookMethod(
                "com.tencent.oscar.module.share.a.b",
                "a",
                String::class.java, Int::class.java
        ) {

            if ("复制链接" == it.args[0]) {
                // 添加入口
                XposedHelpers.callMethod(
                        it.thisObject, "a",
                        Constant.Name.PLUGIN, it.args[1])
            }
        }

        findAndHookMethodReplacement(
                "com.tencent.oscar.module.share.a.b",
                "a",
                View::class.java, Int::class.java
        ) {

            val viewGroup = it.args[0] as ViewGroup
            val textView = ViewUtil.findFirstView(viewGroup,
                    "android.support.v7.widget.AppCompatTextView") as TextView?

            if (textView != null
                    && Constant.Name.PLUGIN == textView.text) {
                // 显示设置界面
                val activity = XposedHelpers
                        .getObjectField(it.thisObject, "a") as Activity
                val dialog = SettingsDialog()
                dialog.show(activity.fragmentManager, "settings")

                // 关闭界面
                XposedHelpers.callMethod(it.thisObject, "dismiss")
            } else {
                // 走原来的逻辑
                XposedBridge.invokeOriginalMethod(it.method, it.thisObject, it.args)
            }
            Unit
        }
    }

    /**
     * 自动播放Hook方法
     * @param param
     */
    private fun autoPlayHook() {

        findAndAfterHookMethod(
                "com.tencent.oscar.module.feedlist.c.af",
                "onResume") {

            getObjectManager().setViewPager(
                    XposedHelpers.getObjectField(it.thisObject, "a"))
        }

        findAndAfterHookMethod(
                "com.tencent.oscar.module.feedlist.c.af",
                "onPause") {

            getObjectManager().setViewPager(null)
        }

        findAndAfterHookMethod(
                "com.tencent.oscar.module.main.feed.f",
                "onResume") {

            getObjectManager().setViewPager(
                    XposedHelpers.getObjectField(it.thisObject, "a"))
        }

        findAndAfterHookMethod(
                "com.tencent.oscar.module.main.feed.f",
                "onPause") {

            getObjectManager().setViewPager(null)
        }
    }

    /**
     * 视频切换
     */
    private fun videoSwitchHook() {

        findAndAfterHookMethod(
                "com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager",
                "smoothScrollToPosition",
                Int::class.java
        ) {

            // 切换的下标
            val position = it.args[0] as Int

            mAutoLikeHandler.cancel()
            mAutoLikeHandler.like(position)

            mAutoAttentionHandler.cancel()
            mAutoAttentionHandler.attention(position)
        }
    }

    private fun debugWeiShiHook() {

        // 开启日志
        val globalClass = findClass("com.tencent.base.Global")
        XposedHelpers.setStaticBooleanField(globalClass, "isDebug", true)
        XposedHelpers.setStaticBooleanField(globalClass, "isGray", true)
    }

    private fun testHook() {

//        findAndBeforeHookMethod(
//                "android.support.v4.app.Fragment",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }

//        findAndBeforeHookMethod(
//                "android.support.v7.app.AppCompatDialog",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }
//
//        findAndBeforeHookMethod(
//                "android.app.Dialog",
//                "show"
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> show " + it.thisObject.javaClass)
//        }

//        findAndAfterHookMethod(
//                "com.tencent.oscar.module.share.a.b",
//                "a",
//                Int::class.java, String::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> aaaa ${it.args[0]} ${it.args[1]}")
//        }

//        findAndBeforeHookMethod(
//                "com.tencent.oscar.module.share.a.b",
//                "b",
//                View::class.java, Int::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> b ${it.args[0]} ${it.args[1]}")
//        }
    }

    fun onModifyValue(key: String, value: Any) {

    }
}