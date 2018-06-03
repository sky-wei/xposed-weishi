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
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class CommonFrameLayout : LinearLayout {

    private lateinit var mTitleView: TitleView
    private lateinit var mScrollView: ScrollView
    private lateinit var mContent: LinearLayout

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        orientation = LinearLayout.VERTICAL
        setBackgroundColor(Color.WHITE)
        layoutParams = LayoutUtil.newMatchLinearLayoutParams()

        // 添加标题
        mTitleView = TitleView(context)
        addView(mTitleView)

        mScrollView = ScrollView(context)
        mScrollView.layoutParams = LayoutUtil.newMatchLinearLayoutParams()

        val top = DisplayUtil.dip2px(context, 5f)

        mContent = LayoutUtil.newCommonLayout(context)
        mContent.setPadding(0, top, 0, top)
        mScrollView.addView(mContent)

        addView(mScrollView)
    }

    fun getTitleView(): TitleView {
        return mTitleView
    }

    fun setTitle(title: String) {
        mTitleView.setTitle(title)
    }

    fun addContent(child: View) {
        addContent(child, false)
    }

    fun addContent(child: View, line: Boolean) {
        mContent.addView(child)
        if (line) mContent.addView(ViewUtil.newLineView(context))
    }

    fun setContent(view: View) {
        removeView(mScrollView)
        addView(view)
    }

    fun addContent(child: View, params: ViewGroup.LayoutParams) {
        mContent.addView(child, params)
    }
}