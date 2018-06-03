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
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.base.BaseDialogFragment
import com.sky.xposed.weishi.ui.interfaces.TrackViewStatus
import com.sky.xposed.weishi.ui.view.CommonFrameLayout
import com.sky.xposed.weishi.ui.view.EditTextItemView
import com.sky.xposed.weishi.ui.view.TitleView
import com.sky.xposed.weishi.util.ConversionUtil
import com.sky.xposed.weishi.util.VToast

/**
 * Created by sky on 18-6-3.
 */
class MoreSettingsDialog : BaseDialogFragment() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout

    private lateinit var mAutoPlaySleepTime: EditTextItemView
    private lateinit var mRecordVideoTime: EditTextItemView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.getTitleView()

        mAutoPlaySleepTime = EditTextItemView(context)
        mAutoPlaySleepTime.name = "自动播放休眠时间"
        mAutoPlaySleepTime.setExtendHint("未设置")
        mAutoPlaySleepTime.unit = "秒"
        mAutoPlaySleepTime.inputType = Constant.InputType.NUMBER_SIGNED

        mRecordVideoTime = EditTextItemView(context)
        mRecordVideoTime.name = "录制视频最大限制时间"
        mRecordVideoTime.setExtendHint("未设置")
        mRecordVideoTime.unit = "秒"
        mRecordVideoTime.inputType = Constant.InputType.NUMBER_SIGNED

        mCommonFrameLayout.addContent(mAutoPlaySleepTime, true)
        mCommonFrameLayout.addContent(mRecordVideoTime)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("更多设置")

        trackBind(mAutoPlaySleepTime, Constant.Preference.AUTO_PLAY_SLEEP_TIME,
                Constant.DefaultValue.AUTO_PLAY_SLEEP_TIME.toString(), mStringChangeListener)
        trackBind(mRecordVideoTime, Constant.Preference.RECORD_VIDEO_TIME,
                Constant.DefaultValue.RECORD_VIDEO_TIME.toString(), mStringChangeListener)
    }

    private val mStringChangeListener = object : TrackViewStatus.StatusChangeListener<String> {

        override fun onStatusChange(view: View, key: String, value: String): Boolean {

            when(key) {
                Constant.Preference.AUTO_PLAY_SLEEP_TIME -> {

                    val sleepTime = ConversionUtil.parseInt(value)

                    if (sleepTime <= 5) {
                        VToast.show("设置的休眠数不能少于5秒，请重新设置")
                        return false
                    }
                }
                Constant.Preference.RECORD_VIDEO_TIME -> {

                    val recordTime = ConversionUtil.parseInt(value)

                    if (recordTime <= 0) {
                        VToast.show("设置的最大录制视频时间无效，请重新设置")
                        return false
                    }

                    if (recordTime > 120) {
                        VToast.show("设置时间值过大，请慎重！")
                    }
                }
            }
            sendRefreshPreferenceBroadcast(key, value)
            return true
        }
    }
}