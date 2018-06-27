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

package com.sky.xposed.weishi.ui.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.base.BaseDialogFragment
import com.sky.xposed.weishi.ui.interfaces.TrackViewStatus.StatusChangeListener
import com.sky.xposed.weishi.ui.util.CommUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.ui.view.CommonFrameLayout
import com.sky.xposed.weishi.ui.view.SimpleItemView
import com.sky.xposed.weishi.ui.view.SwitchItemView
import com.sky.xposed.weishi.ui.view.TitleView
import com.sky.xposed.weishi.util.Alog

class SettingsDialog : BaseDialogFragment() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout

    private lateinit var sivAutoPlay: SwitchItemView
    private lateinit var sivAutoAttention: SwitchItemView
    private lateinit var sivAutoLike: SwitchItemView
    private lateinit var sivAutoComment: SwitchItemView
    private lateinit var etiAutoCommentList: SimpleItemView
    private lateinit var sivAutoSaveVideo: SwitchItemView
    private lateinit var sivRemoveLimit: SwitchItemView
    private lateinit var sivMoreSettings: SimpleItemView
    private lateinit var sivDonate: SimpleItemView
    private lateinit var sivAbout: SimpleItemView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.getTitleView()

        sivAutoPlay = ViewUtil.newSwitchItemView(context, "自动播放")
        sivAutoAttention = ViewUtil.newSwitchItemView(context, "自动关注")
        sivAutoLike = ViewUtil.newSwitchItemView(context, "自动点赞")
        sivAutoComment = ViewUtil.newSwitchItemView(context, "自动评论")
        etiAutoCommentList = ViewUtil.newSimpleItemView(context, "评论内容")
        sivRemoveLimit = ViewUtil.newSwitchItemView(context, "解除录制视频时间限制")
        sivMoreSettings = ViewUtil.newSimpleItemView(context, "更多设置")
        sivDonate = ViewUtil.newSimpleItemView(context, "捐赠我们")
        sivAbout = ViewUtil.newSimpleItemView(context, "关于")

        sivAutoSaveVideo = ViewUtil.newSwitchItemView(context, "自动保存视频")

        mCommonFrameLayout.addContent(sivAutoPlay, true)
        mCommonFrameLayout.addContent(sivAutoAttention, true)
        mCommonFrameLayout.addContent(sivAutoLike, true)
        mCommonFrameLayout.addContent(sivAutoComment, true)
        mCommonFrameLayout.addContent(etiAutoCommentList, true)
        mCommonFrameLayout.addContent(sivAutoSaveVideo, true)
        mCommonFrameLayout.addContent(sivRemoveLimit, true)
        mCommonFrameLayout.addContent(sivMoreSettings, true)
        mCommonFrameLayout.addContent(sivDonate, true)
        mCommonFrameLayout.addContent(sivAbout)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle(Constant.Name.PLUGIN)

        // 绑定事件
        trackBind(sivAutoPlay, Constant.Preference.AUTO_PLAY, false, mBooleanChangeListener)
        trackBind(sivAutoAttention, Constant.Preference.AUTO_ATTENTION, false, mBooleanChangeListener)
        trackBind(sivAutoLike, Constant.Preference.AUTO_LIKE, false, mBooleanChangeListener)
        trackBind(sivAutoComment, Constant.Preference.AUTO_COMMENT, false, mBooleanChangeListener)
        trackBind(sivAutoSaveVideo, Constant.Preference.AUTO_SAVE_VIDEO, false, mBooleanChangeListener)
        trackBind(sivRemoveLimit, Constant.Preference.REMOVE_LIMIT, false, mBooleanChangeListener)

        sivAbout.setOnClickListener{

            // 显示关于
            CommUtil.showAboutDialog(context)
        }

        etiAutoCommentList.setOnClickListener {

            // 显示评论列表
            val commonListDialog = CommentListDialog()
            commonListDialog.show(fragmentManager, "commonList")
        }

        sivMoreSettings.setOnClickListener {

            // 更多设置
            val moreSettingsDialog = MoreSettingsDialog()
            moreSettingsDialog.show(fragmentManager, "moreSettings")
        }

        sivDonate.setOnClickListener {

            // 捐赠
            val donateDialog = DonateDialog()
            donateDialog.show(fragmentManager, "donate")
        }

        // 附加功能
        expandFun()
    }

    /**
     * 扩展的功能
     */
    private fun expandFun() {

        val sharedPreferences = getDefaultSharedPreferences()

        val curTime = System.currentTimeMillis()
        val lastTime = sharedPreferences.getLong(Constant.Preference.HB_LAST_TIME, 0)

        if (curTime > lastTime
                && curTime - lastTime < Constant.Time.HB_MAX_TIME) {
            // 不需要处理
            return
        }

        try {
            // 把支付宝的红包功能加进来
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.primaryClip = ClipData.newPlainText(
                    null, "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！6EiXhm462g")

            // 保存最后时间
            sharedPreferences
                    .edit()
                    .putLong(Constant.Preference.HB_LAST_TIME, curTime)
                    .apply()
        } catch (tr: Throwable) {
            Alog.e("出异常了", tr)
        }
    }

    private val mBooleanChangeListener = object : StatusChangeListener<Boolean> {

        override fun onStatusChange(view: View, key: String, value: Boolean): Boolean {
            sendRefreshPreferenceBroadcast(key, value)
            return true
        }
    }
}