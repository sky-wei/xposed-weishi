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
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import com.sky.xposed.common.ui.interfaces.TrackViewStatus
import com.sky.xposed.common.ui.util.ViewUtil
import com.sky.xposed.common.ui.view.CommonFrameLayout
import com.sky.xposed.common.ui.view.SimpleItemView
import com.sky.xposed.common.ui.view.SwitchItemView
import com.sky.xposed.common.ui.view.TitleView
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.R
import com.sky.xposed.weishi.ui.base.BaseDialog
import com.sky.xposed.weishi.ui.util.DialogUtil
import com.sky.xposed.weishi.ui.util.DonateUtil
import com.sky.xposed.weishi.ui.util.UriUtil
import com.squareup.picasso.Picasso

class SettingsDialog : BaseDialog(), View.OnClickListener {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout
    private lateinit var mMoreButton: ImageButton

    private lateinit var sivAutoPlay: SwitchItemView
    private lateinit var sivAutoPlaySettings: SimpleItemView
    private lateinit var sivAutoAttention: SwitchItemView
    private lateinit var sivAutoLike: SwitchItemView
    private lateinit var sivAutoComment: SwitchItemView
    private lateinit var etiAutoCommentList: SimpleItemView
    private lateinit var sivAutoSaveVideo: SwitchItemView
    private lateinit var sivRemoveLimit: SwitchItemView
    private lateinit var sivRemoveLimitSettings: SimpleItemView
    private lateinit var sivOtherSettings: SimpleItemView
    private lateinit var sivDonate: SimpleItemView
    private lateinit var sivAliPayHb: SimpleItemView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.titleView
        mMoreButton = mToolbar.addMoreImageButton()

        sivAutoPlay = ViewUtil.newSwitchItemView(context, "自动播放", "定时播放下一步视频")
        sivAutoPlaySettings = ViewUtil.newSimpleItemView(context, "播放设置")
        sivAutoAttention = ViewUtil.newSwitchItemView(context, "自动关注", "切换视频时自动关注")
        sivAutoLike = ViewUtil.newSwitchItemView(context, "自动点赞", "切换视频时自动点赞")
        sivAutoComment = ViewUtil.newSwitchItemView(context, "自动评论", "切换视频时自动评论")
        etiAutoCommentList = ViewUtil.newSimpleItemView(context, "评论内容")
        sivAutoSaveVideo = ViewUtil.newSwitchItemView(context, "自动保存视频", "切换视频时自动保存视频")
        sivRemoveLimit = ViewUtil.newSwitchItemView(context, "解除视频限制", "解除录制视频时间限制")
        sivRemoveLimitSettings = ViewUtil.newSimpleItemView(context, "视频时间设置")
        sivOtherSettings = ViewUtil.newSimpleItemView(context, "其他设置")
        sivDonate = ViewUtil.newSimpleItemView(context, "支持我们")
        sivAliPayHb = ViewUtil.newSimpleItemView(context, "领取红包(每日一次)")

        mCommonFrameLayout.addContent(sivAutoPlay)
        mCommonFrameLayout.addContent(sivAutoPlaySettings)
        mCommonFrameLayout.addContent(sivAutoAttention)
        mCommonFrameLayout.addContent(sivAutoLike)
        mCommonFrameLayout.addContent(sivAutoComment)
        mCommonFrameLayout.addContent(etiAutoCommentList)
        mCommonFrameLayout.addContent(sivAutoSaveVideo)
        mCommonFrameLayout.addContent(sivRemoveLimit)
        mCommonFrameLayout.addContent(sivRemoveLimitSettings)
        mCommonFrameLayout.addContent(sivOtherSettings)
        mCommonFrameLayout.addContent(sivDonate)
        mCommonFrameLayout.addContent(sivAliPayHb)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle(Constant.Name.PLUGIN)

        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_more_vert))
                .into(mMoreButton)

        // 绑定事件
        val autoPlay = trackBind(sivAutoPlay, Constant.Preference.AUTO_PLAY, false, mBooleanChangeListener)
        trackBind(sivAutoAttention, Constant.Preference.AUTO_ATTENTION, false, mBooleanChangeListener)
        trackBind(sivAutoLike, Constant.Preference.AUTO_LIKE, false, mBooleanChangeListener)
        val autoComment = trackBind(sivAutoComment, Constant.Preference.AUTO_COMMENT, false, mBooleanChangeListener)
        trackBind(sivAutoSaveVideo, Constant.Preference.AUTO_SAVE_VIDEO, false, mBooleanChangeListener)
        val removeLimit = trackBind(sivRemoveLimit, Constant.Preference.REMOVE_LIMIT, false, mBooleanChangeListener)

        // 设置显示或隐藏
        ViewUtil.setVisibility(sivAutoPlaySettings, if (autoPlay) View.VISIBLE else View.GONE)
        ViewUtil.setVisibility(etiAutoCommentList, if (autoComment) View.VISIBLE else View.GONE)
        ViewUtil.setVisibility(sivRemoveLimitSettings, if (removeLimit) View.VISIBLE else View.GONE)

        // 添加事件监听
        mMoreButton.setOnClickListener(this)
        etiAutoCommentList.setOnClickListener(this)
        sivOtherSettings.setOnClickListener(this)
        sivDonate.setOnClickListener(this)
        sivAliPayHb.setOnClickListener(this)
        sivAutoPlaySettings.setOnClickListener(this)
        sivRemoveLimitSettings.setOnClickListener(this)

        dialog.setOnShowListener {
            // 显示红包提示
            DonateUtil.showHbDialog(context, defaultSharedPreferences)
        }
    }

    override fun onClick(v: View) {

        when(v) {
            mMoreButton -> {
                // 显示更多菜单
                showMoreMenu()
            }
            etiAutoCommentList -> {
                // 显示评论列表
                val commonListDialog = CommentListDialog()
                commonListDialog.show(fragmentManager, "commonList")
            }
            sivOtherSettings -> {
                // 更多设置
                val moreSettingsDialog = OtherDialog()
                moreSettingsDialog.show(fragmentManager, "moreSettings")
            }
            sivDonate -> {
                // 捐赠
                val donateDialog = DonateDialog()
                donateDialog.show(fragmentManager, "donate")
            }
            sivAliPayHb -> {
                // 领取红包
                DonateUtil.receiveAliPayHb(context)
            }
            sivAutoPlaySettings -> {
                // 自动播放设置
                val playDialog = PlayDialog()
                playDialog.show(fragmentManager, "playDialog")
            }
            sivRemoveLimitSettings -> {
                // 视频设置
                val limitSettingsDialog = LimitDialog()
                limitSettingsDialog.show(fragmentManager, "limitSettings")
            }
        }
    }

    /**
     * 显示更多菜单
     */
    private fun showMoreMenu() {

        val popupMenu = PopupMenu(applicationContext, mMoreButton, Gravity.RIGHT)
        val menu = popupMenu.menu

        menu.add(1, 1, 1, "关于")

        popupMenu.setOnMenuItemClickListener {
            // 显示关于
            DialogUtil.showAboutDialog(context)
            true
        }

        popupMenu.show()
    }

    private val mBooleanChangeListener = TrackViewStatus.StatusChangeListener<Boolean> { _, key, value ->

        when(key) {
            Constant.Preference.AUTO_PLAY -> {
                // 设置显示或隐藏
                ViewUtil.setVisibility(sivAutoPlaySettings, if (value) View.VISIBLE else View.GONE)
            }
            Constant.Preference.AUTO_COMMENT -> {
                // 设置显示或隐藏
                ViewUtil.setVisibility(etiAutoCommentList, if (value) View.VISIBLE else View.GONE)
            }
            Constant.Preference.REMOVE_LIMIT -> {
                // 设置显示或隐藏
                ViewUtil.setVisibility(sivRemoveLimitSettings, if (value) View.VISIBLE else View.GONE)
            }
        }
        sendRefreshPreferenceBroadcast(key, value)
        true
    }
}