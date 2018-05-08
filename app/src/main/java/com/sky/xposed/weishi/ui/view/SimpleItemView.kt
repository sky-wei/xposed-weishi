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
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class SimpleItemView : FrameLayout {

    private lateinit var tvName: TextView

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        background = ViewUtil.newBackgroundDrawable()
        layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 40f))

        tvName = TextView(context)
        tvName.setTextColor(Color.BLACK)
        tvName.textSize = 15f

        val params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL
        params.leftMargin = DisplayUtil.dip2px(context, 15f)

        addView(tvName, params)
    }

    fun setName(title: String) {
        tvName.text = title
    }

    fun getName(): String {
        return tvName.text.toString()
    }
}