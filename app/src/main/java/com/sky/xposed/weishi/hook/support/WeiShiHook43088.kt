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

package com.sky.xposed.weishi.hook.support

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sky.xposed.ktx.XposedPlus
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.dialog.SettingsDialog
import com.sky.xposed.weishi.util.FindUtil
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

/**
 * Created by sky on 18-6-2.
 */
class WeiShiHook43088 : WeiShiHook() {

    override fun injectionUISettings() {

        val classType = XposedPlus.findClass(mVersionConfig.classShareType)

        XposedPlus.findAndBeforeHookMethod(
                mVersionConfig.classShareDialog,
                mVersionConfig.methodShareCreateItem,
                String::class.java, Int::class.java, classType
        ) {

            if ("复制链接" == it.args[0]) {
                // 添加入口
                XposedHelpers.callMethod(
                        it.thisObject, mVersionConfig.methodShareCreateItem,
                        Constant.Name.PLUGIN, it.args[1])

                // 添加下载
                XposedHelpers.callMethod(
                        it.thisObject, mVersionConfig.methodShareCreateItem,
                        Constant.Name.SAVE_VIDEO, it.args[1])
            }
        }

        XposedPlus.findAndHookMethodReplacement(
                mVersionConfig.classShareDialog,
                mVersionConfig.methodShareClick,
                View::class.java, Int::class.java
        ) {

            val viewGroup = it.args[0] as ViewGroup
            val textView = FindUtil.findFirstView(viewGroup,
                    mVersionConfig.classAppCompatTextView) as TextView?
            val name = textView?.text

            when(name) {
                Constant.Name.PLUGIN -> {
                    // 显示设置界面
                    val activity = XposedHelpers
                            .getObjectField(it.thisObject, mVersionConfig.fieldShareActivity) as Activity
                    val dialog = SettingsDialog()
                    dialog.show(activity.fragmentManager, "settings")

                    // 关闭界面
                    XposedHelpers.callMethod(it.thisObject, mVersionConfig.methodShareDismiss)
                }
                Constant.Name.SAVE_VIDEO -> {
                    // 下载视频到本地
                    mAutoDownloadHandler.downloadToLocal()

                    // 关闭界面
                    XposedHelpers.callMethod(it.thisObject, mVersionConfig.methodShareDismiss)
                }
                else -> {
                    // 走原来的逻辑
                    XposedBridge.invokeOriginalMethod(it.method, it.thisObject, it.args)
                }
            }
            Unit
        }
    }
}