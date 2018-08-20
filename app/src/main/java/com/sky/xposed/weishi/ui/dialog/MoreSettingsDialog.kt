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
import com.sky.xposed.common.ui.view.EditTextItemView
import com.sky.xposed.common.ui.view.SwitchItemView
import com.sky.xposed.common.ui.view.TitleView
import com.sky.xposed.common.util.ConversionUtil
import com.sky.xposed.common.util.ToastUtil
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.base.BaseDialog

/**
 * Created by sky on 18-6-3.
 */
class MoreSettingsDialog : BaseDialog() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout

    private lateinit var mAutoPlaySleepTime: EditTextItemView
    private lateinit var mRecordVideoTime: EditTextItemView
    private lateinit var sivDisableUpdate: SwitchItemView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.titleView

        mAutoPlaySleepTime = EditTextItemView(context)
        mAutoPlaySleepTime.name = "自动播放休眠时间"
        mAutoPlaySleepTime.setExtendHint("未设置")
        mAutoPlaySleepTime.unit = "秒"
        mAutoPlaySleepTime.inputType = com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED

        mRecordVideoTime = EditTextItemView(context)
        mRecordVideoTime.name = "录制视频最大限制时间"
        mRecordVideoTime.setExtendHint("未设置")
        mRecordVideoTime.unit = "秒"
        mRecordVideoTime.inputType = com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED

        sivDisableUpdate = ViewUtil.newSwitchItemView(context, "禁用微视更新")

        mCommonFrameLayout.addContent(mAutoPlaySleepTime, true)
        mCommonFrameLayout.addContent(mRecordVideoTime, true)
        mCommonFrameLayout.addContent(sivDisableUpdate)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("更多设置")

        trackBind(mAutoPlaySleepTime, Constant.Preference.AUTO_PLAY_SLEEP_TIME,
                Constant.DefaultValue.AUTO_PLAY_SLEEP_TIME.toString(), mStringChangeListener)
        trackBind(mRecordVideoTime, Constant.Preference.RECORD_VIDEO_TIME,
                Constant.DefaultValue.RECORD_VIDEO_TIME.toString(), mStringChangeListener)
        trackBind(sivDisableUpdate, Constant.Preference.DISABLE_UPDATE, false, mBooleanChangeListener)
    }

    private val mStringChangeListener = TrackViewStatus.StatusChangeListener<String> { _, key, value ->
        when(key) {
            Constant.Preference.AUTO_PLAY_SLEEP_TIME -> {

                val sleepTime = ConversionUtil.parseInt(value)

                if (sleepTime <= 5) {
                    ToastUtil.show("设置的休眠数不能少于5秒，请重新设置")
                    return@StatusChangeListener false
                }
            }
            Constant.Preference.RECORD_VIDEO_TIME -> {

                val recordTime = ConversionUtil.parseInt(value)

                if (recordTime <= 0) {
                    ToastUtil.show("设置的最大录制视频时间无效，请重新设置")
                    return@StatusChangeListener false
                }

                if (recordTime > 120) {
                    ToastUtil.show("设置时间值过大，请慎重！")
                }
            }
        }
        sendRefreshPreferenceBroadcast(key, value)
        true
    }

    private val mBooleanChangeListener = TrackViewStatus.StatusChangeListener<Boolean> { _, key, value ->
        sendRefreshPreferenceBroadcast(key, value)
        true
    }
}