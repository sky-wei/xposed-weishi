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

package com.sky.xposed.weishi.hook.handler

import android.text.TextUtils
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.RandomUtil
import com.sky.xposed.weishi.util.VToast
import de.robv.android.xposed.XposedHelpers

class AutoCommentHandler(hookManager: HookManager) : CommonHandler(hookManager), Runnable {

    private val lifePlayApplicationClass: Class<*>
    private val stMetaPersonClass: Class<*>
    private val stMetaCommentClass: Class<*>
    private val cClass: Class<*>
    private val parameterTypes: Array<Class<*>>
    private val parameterTypes1: Array<Class<*>>

    init {
        val classLoader = hookManager
                .getLoadPackageParam().classLoader

        lifePlayApplicationClass = XposedHelpers.findClass(
                "com.tencent.oscar.app.LifePlayApplication", classLoader)
        stMetaPersonClass = XposedHelpers.findClass(
                "NS_KING_SOCIALIZE_META.stMetaPerson", classLoader)
        stMetaCommentClass = XposedHelpers.findClass(
                "NS_KING_SOCIALIZE_META.stMetaComment", classLoader)
        cClass = XposedHelpers.findClass(
                "com.tencent.oscar.module.d.a.c", classLoader)

        // 参数类型
        parameterTypes = arrayOf(String::class.java, String::class.java,
                String::class.java, stMetaPersonClass, String::class.java, stMetaPersonClass, Int::class.java)
        // 参数类型
        parameterTypes1 = arrayOf(String::class.java, String::class.java,
                stMetaPersonClass, stMetaCommentClass, String::class.java, String::class.java, String::class.java)
    }

    override fun run() {

        // 获取当前分享视频的相关信息
        val data = getAdapterItem(getCurrentPosition())
        val message = getConfigManager().getCommentMessage()

        if (data == null || TextUtils.isEmpty(message)) return

        try {
            val id = XposedHelpers.getObjectField(data, "id")
            val poster = XposedHelpers.getObjectField(data, "poster")
            val topicId = XposedHelpers.getObjectField(data, "topic_id")
            val shieldId = XposedHelpers.getObjectField(data, "shieldId")

            // 创建评论对象 posterId与poster需要是当前用户
            val accountManager = XposedHelpers.callStaticMethod(
                    lifePlayApplicationClass, "getAccountManager")
            val curPosterId = XposedHelpers.callMethod(accountManager, "b")

            val comment = XposedHelpers.newInstance(stMetaCommentClass, parameterTypes,
                    "pending_commend_id", message, curPosterId, null, "", null, (System.currentTimeMillis() / 1000).toInt())

            // 发送评论
            XposedHelpers.callStaticMethod(cClass,
                    "a", parameterTypes1, id, topicId, poster, comment, "", "", shieldId)
        } catch (tr: Throwable) {
            Alog.e("发布评论异常", tr)
        }
    }

    fun comment() {

        if (!isComment()) return

        getHandler().postDelayed(this, RandomUtil.randomLong(1500, 3000))
    }

    fun isComment(): Boolean {

        if (!getConfigManager().isAutoComment()) {
            return false
        }

        if (TextUtils.isEmpty(getConfigManager().getCommentMessage())) {
            VToast.show("请先设置发送消息")
            return false
        }
        return true
    }

    fun cancel() {
        getHandler().removeCallbacks(this)
    }
}