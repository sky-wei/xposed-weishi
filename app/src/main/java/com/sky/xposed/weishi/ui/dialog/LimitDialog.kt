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
import com.sky.xposed.common.ui.view.CommonFrameLayout
import com.sky.xposed.common.ui.view.EditTextItemView
import com.sky.xposed.common.ui.view.TitleView
import com.sky.xposed.common.util.ConversionUtil
import com.sky.xposed.common.util.ToastUtil
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.R
import com.sky.xposed.weishi.ui.base.BaseDialog
import com.sky.xposed.weishi.ui.util.UriUtil
import com.squareup.picasso.Picasso

/**
 * Created by sky on 2018/8/31.
 */
class LimitDialog : BaseDialog() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout

    private lateinit var mRecordVideoTime: EditTextItemView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.titleView

        mRecordVideoTime = EditTextItemView(context)
        mRecordVideoTime.name = "录制视频最大限制时间"
        mRecordVideoTime.setExtendHint("未设置")
        mRecordVideoTime.unit = "秒"
        mRecordVideoTime.inputType = com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED

        mCommonFrameLayout.addContent(mRecordVideoTime)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("时间设置")
        mToolbar.showBack()
        mToolbar.setOnBackEventListener { dismiss() }

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(mToolbar.backView)

        trackBind(mRecordVideoTime, Constant.Preference.RECORD_VIDEO_TIME,
                Constant.DefaultValue.RECORD_VIDEO_TIME.toString(), mStringChangeListener)
    }

    private val mStringChangeListener = TrackViewStatus.StatusChangeListener<String> { _, key, value ->
        when(key) {
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
}