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
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sky.xposed.ktx.XposedPlus
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.hook.base.BaseHook
import com.sky.xposed.weishi.hook.handler.*
import com.sky.xposed.weishi.ui.dialog.SettingsDialog
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.Alog
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

open class WeiShiHook : BaseHook() {

    val mAutoPlayHandler = AutoPlayHandler(mHookManager)
    val mAutoLikeHandler = AutoLikeHandler(mHookManager)
    val mAutoAttentionHandler = AutoAttentionHandler(mHookManager)
    val mAutoCommentHandler = AutoCommentHandler(mHookManager)
    val mAutoDownloadHandler = AutoDownloadHandler(mHookManager)

    val mVersionConfig = mVersionManager.getSupportConfig()!!

    override fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        if (Alog.debug) debugWeiShiHook()
        if (Alog.debug) testHook()

        // 注入UI设置入口
        injectionUISettings()

        // 自动播放
        autoPlayHook()

        // 切换视频
        videoSwitchHook()

        // 移除时间限制
        removeLimitHook()
    }

    /**
     * 注入UI配置入口
     */
    open fun injectionUISettings() {

        XposedPlus.findAndBeforeHookMethod(
                mVersionConfig.classShareDialog,
                mVersionConfig.methodShareCreateItem,
                String::class.java, Int::class.java
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
            val textView = ViewUtil.findFirstView(viewGroup,
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

    /**
     * 自动播放Hook方法
     * @param param
     */
    open fun autoPlayHook() {

        XposedPlus.findAndAfterHookMethod(
                mVersionConfig.classFeedList,
                mVersionConfig.methodFeedListOnResume) {

            mObjectManager.setViewPager(
                    XposedHelpers.getObjectField(it.thisObject, mVersionConfig.fieldFeedListViewPager))

            // 自动播放
            mAutoPlayHandler.startPlay()

            // 开始处理
            startHandlerLike()
        }

        XposedPlus.findAndAfterHookMethod(
                mVersionConfig.classFeedList,
                mVersionConfig.methodFeedListOnPause) {

            // 停止播放
            mAutoPlayHandler.stopPlay()

            mObjectManager.setViewPager(null)
        }

        XposedPlus.findAndAfterHookMethod(
                mVersionConfig.classMainFeed,
                mVersionConfig.methodMainFeedOnResume) {

            mObjectManager.setViewPager(
                    XposedHelpers.getObjectField(it.thisObject, mVersionConfig.fieldMainFeedViewPager))

            // 自动播放
            mAutoPlayHandler.startPlay()

            // 开始处理
            startHandlerLike()
        }

        XposedPlus.findAndAfterHookMethod(
                mVersionConfig.classMainFeed,
                mVersionConfig.methodMainFeedOnPause) {

            // 停止播放
            mAutoPlayHandler.stopPlay()

            mObjectManager.setViewPager(null)
        }
    }

    /**
     * 视频切换
     */
    open fun videoSwitchHook() {

        XposedPlus.findAndAfterHookMethod(
                mVersionConfig.classRecyclerViewPager,
                mVersionConfig.methodViewPagerSmooth,
                Int::class.java
        ) {

            // 开始处理
            startHandlerLike()
        }
    }

    /**
     * 移除时间限制
     */
    open fun removeLimitHook() {

        XposedPlus.findAndHookMethodReplacement(
                mVersionConfig.classWeishiParams,
                mVersionConfig.methodParamsLimit,
                Long::class.java, Long::class.java
        ) {

            if (mUserConfigManager.isRemoveLimit()) {
                // 最大录制时间
                mUserConfigManager.getRecordVideoTime()
            } else {
                // 调用默认的
                XposedBridge.invokeOriginalMethod(it.method, it.thisObject, it.args)
            }
        }
    }

    /**
     * 开始处理点赞跟关注
     */
    open fun startHandlerLike() {

        mAutoLikeHandler.cancel()
        mAutoLikeHandler.like()

        mAutoAttentionHandler.cancel()
        mAutoAttentionHandler.attention()

        mAutoCommentHandler.cancel()
        mAutoCommentHandler.comment()

        mAutoDownloadHandler.cancel()
        mAutoDownloadHandler.download()
    }

    open fun debugWeiShiHook() {

        // 开启日志
        val globalClass = XposedPlus.findClass("com.tencent.base.Global")
        XposedHelpers.setStaticBooleanField(globalClass, "isDebug", true)
        XposedHelpers.setStaticBooleanField(globalClass, "isGray", true)
    }

    open fun testHook() {

//        XposedPlus.findAndBeforeHookMethod(
//                "android.support.v4.app.Fragment",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }
//
//        XposedPlus.findAndBeforeHookMethod(
//                "com.tencent.oscar.widget.comment.CommentPostBoxFragment",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }
//
//        XposedPlus.findAndBeforeHookMethod(
//                "android.support.design.widget.BottomSheetDialog",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject)
//        }

//        val stMetaPersonClass = findClass("NS_KING_SOCIALIZE_META.stMetaPerson")
//        val stMetaCommentClass = findClass("NS_KING_SOCIALIZE_META.stMetaComment")
//
//        findAndBeforeHookMethod(
//                "com.tencent.oscar.module.d.a.c",
//                "a",
//                String::class.java, String::class.java, stMetaPersonClass,
//                stMetaCommentClass, String::class.java, String::class.java, String::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>> ${it.args[0]}")
//            Alog.d(">>>>>>>>>>>>>>>>> ${it.args[1]}")
//            ToStringUtil.toString(it.args[2])
//            ToStringUtil.toString(it.args[3])
//            Alog.d(">>>>>>>>>>>>>>>>> ${it.args[4]}")
//            Alog.d(">>>>>>>>>>>>>>>>> ${it.args[5]}")
//            Alog.d(">>>>>>>>>>>>>>>>> ${it.args[6]}")
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

    open fun onModifyValue(key: String, value: Any) {

        if (Constant.Preference.AUTO_PLAY == key) {
            // 设置自动播放
            mAutoPlayHandler.setAutoPlay(value as Boolean)
        }
    }
}