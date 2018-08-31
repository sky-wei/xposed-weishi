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
class PlayDialog : BaseDialog() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout

    private lateinit var mAutoPlaySleepTime: EditTextItemView

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

        mCommonFrameLayout.addContent(mAutoPlaySleepTime)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("播放设置")
        mToolbar.showBack()
        mToolbar.setOnBackEventListener { dismiss() }

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(mToolbar.backView)

        trackBind(mAutoPlaySleepTime, Constant.Preference.AUTO_PLAY_SLEEP_TIME,
                Constant.DefaultValue.AUTO_PLAY_SLEEP_TIME.toString(), mStringChangeListener)
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
        }
        sendRefreshPreferenceBroadcast(key, value)
        true
    }
}