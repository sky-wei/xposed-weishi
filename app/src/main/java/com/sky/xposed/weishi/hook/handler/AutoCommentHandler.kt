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

import com.sky.xposed.common.util.Alog
import com.sky.xposed.common.util.RandomUtil
import com.sky.xposed.common.util.ToastUtil
import com.sky.xposed.weishi.hook.HookManager
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
                mVersionConfig.classLifePlayApplication, classLoader)
        stMetaPersonClass = XposedHelpers.findClass(
                mVersionConfig.classStMetaPerson, classLoader)
        stMetaCommentClass = XposedHelpers.findClass(
                mVersionConfig.classStMetaComment, classLoader)
        cClass = XposedHelpers.findClass(
                mVersionConfig.classSendComment, classLoader)

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
        val messageList = mUserConfigManager.getCommentList()

        if (data == null || messageList.isEmpty()) return

        try {
            // 获取随机评论
            val message = messageList[RandomUtil.random(messageList.size)]

            val id = XposedHelpers.getObjectField(data, mVersionConfig.fieldDataId)
            val poster = XposedHelpers.getObjectField(data, mVersionConfig.fieldDataPoster)
            val topicId = XposedHelpers.getObjectField(data, mVersionConfig.fieldDataTopicId)
            val shieldId = XposedHelpers.getObjectField(data, mVersionConfig.fieldDataShieldId)

            // 创建评论对象 posterId与poster需要是当前用户
            val accountManager = XposedHelpers.callStaticMethod(
                    lifePlayApplicationClass, mVersionConfig.methodGetAccountManager)
            val curPosterId = XposedHelpers.callMethod(accountManager, mVersionConfig.methodAccountPosterId)

            val comment = XposedHelpers.newInstance(stMetaCommentClass, parameterTypes,
                    "pending_commend_id", message, curPosterId, null, "", null, (System.currentTimeMillis() / 1000).toInt())

            // 发送评论
            XposedHelpers.callStaticMethod(cClass,
                    mVersionConfig.methodSendComment, parameterTypes1, id, topicId, poster, comment, "", "", shieldId)
        } catch (tr: Throwable) {
            Alog.e("发布评论异常", tr)
        }
    }

    fun comment() {

        if (!isComment()) return

        mHandler.postDelayed(this, RandomUtil.random(1500, 3000).toLong())
    }

    fun isComment(): Boolean {

        if (!mUserConfigManager.isAutoComment()) {
            return false
        }

        if (mUserConfigManager.isCommentListEmpty()) {
            ToastUtil.show("请先设置发送评论")
            return false
        }
        return true
    }

    fun cancel() {
        mHandler.removeCallbacks(this)
    }
}