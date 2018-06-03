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

package com.sky.xposed.weishi.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import com.sky.xposed.weishi.ui.interfaces.TrackViewStatus
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class SwitchItemView : FrameLayout, View.OnClickListener, TrackViewStatus<Boolean> {

    private lateinit var tvName: TextView
    private lateinit var mSwitch: Switch
    private var onCheckedChangeListener: ((view: View, isChecked: Boolean) -> Unit)? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        val left = DisplayUtil.dip2px(context, 15f)

        setPadding(left, 0, left, 0)

        background = ViewUtil.newDefaultBackgroundDrawable()
        layoutParams = LayoutUtil.newViewGroupParams(
                FrameLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 40f))

        tvName = TextView(context)
        tvName.setTextColor(Color.BLACK)
        tvName.textSize = 15f

        var params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL

        addView(tvName, params)

        mSwitch = Switch(context)
        mSwitch.isClickable = false
        mSwitch.isFocusable = false
        mSwitch.isFocusableInTouchMode = false

        params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT

        addView(mSwitch, params)

        setOnClickListener(this)
    }

    var name: String
        get() = tvName.text.toString()
        set(title) {
            tvName.text = title
        }

    var isChecked: Boolean
        get() = mSwitch.isChecked
        set(checked) {
            mSwitch.isChecked = checked
        }

    override fun onClick(v: View) {

        isChecked = !isChecked
        onCheckedChangeListener?.invoke(this, isChecked)
    }

    override fun bind(preferences: SharedPreferences,
                      key: String, defValue: Boolean, listener: TrackViewStatus.StatusChangeListener<Boolean>) {
        // 设置状态
        isChecked = preferences.getBoolean(key, defValue)
        setOnCheckedChangeListener { view, isChecked ->

            if (listener.onStatusChange(view, key, isChecked)) {
                // 保存状态信息
                preferences.edit().putBoolean(key, isChecked).apply()
            }
        }
    }

    fun setOnCheckedChangeListener(listener: (view: View, isChecked: Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }
}