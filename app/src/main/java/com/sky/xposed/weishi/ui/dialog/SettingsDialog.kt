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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sky.xposed.common.ui.interfaces.TrackViewStatus
import com.sky.xposed.common.ui.util.ViewUtil
import com.sky.xposed.common.ui.view.CommonFrameLayout
import com.sky.xposed.common.ui.view.SimpleItemView
import com.sky.xposed.common.ui.view.SwitchItemView
import com.sky.xposed.common.ui.view.TitleView
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.base.BaseDialog
import com.sky.xposed.weishi.ui.util.DialogUtil
import com.sky.xposed.weishi.ui.util.DonateUtil

class SettingsDialog : BaseDialog() {

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
    private lateinit var sivAliPayHb: SimpleItemView
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
        sivDonate = ViewUtil.newSimpleItemView(context, "支持我们")
        sivAliPayHb = ViewUtil.newSimpleItemView(context, "领取红包(每日一次)")
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
        mCommonFrameLayout.addContent(sivAliPayHb, true)
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
            DialogUtil.showAboutDialog(context)
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

        sivAliPayHb.setOnClickListener {
            // 领取红包
            DonateUtil.receiveAliPayHb(context)
        }

        dialog.setOnShowListener {
            // 显示红包提示
            DonateUtil.showHbDialog(context, defaultSharedPreferences)
        }
    }

    private val mBooleanChangeListener = TrackViewStatus.StatusChangeListener<Boolean> { _, key, value ->
        sendRefreshPreferenceBroadcast(key, value)
        true
    }
}