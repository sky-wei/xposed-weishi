/*
 * Copyright (c) 2018. The sky Authors
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
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class CommentItemView : FrameLayout {

    private lateinit var tvContent: TextView

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        background = ViewUtil.newBackgroundDrawable()
        layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        tvContent = TextView(context)
        tvContent.setTextColor(Color.BLACK)
        tvContent.minLines = 2
        tvContent.maxLines = 2
        tvContent.textSize = 14f
        tvContent.gravity = Gravity.CENTER_VERTICAL
        tvContent.ellipsize = TextUtils.TruncateAt.END

        val topMargin = DisplayUtil.dip2px(context, 6f)
        val leftMargin = DisplayUtil.dip2px(context, 15f)

        val params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL
        params.leftMargin = leftMargin
        params.rightMargin = leftMargin
        params.topMargin = topMargin
        params.bottomMargin = topMargin

        addView(ViewUtil.newLineView(context))
        addView(tvContent, params)
    }

    fun setContent(content: String) {
        tvContent.text = content
    }

    fun getContent(): String {
        return tvContent.text.toString()
    }
}