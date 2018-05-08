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
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class TitleView : FrameLayout, View.OnClickListener {

    private lateinit var ivClose: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var ivMore: ImageButton
    private var mOnTitleEventListener: OnTitleEventListener? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        val height = DisplayUtil.dip2px(context, 45f)

        layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height)
        setBackgroundColor(0xFF16102E.toInt())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 6f
        }

        val tLayout = LinearLayout(context)
        tLayout.gravity = Gravity.CENTER_VERTICAL
        tLayout.orientation = LinearLayout.HORIZONTAL

        ivClose = ImageButton(context)
        ivClose.layoutParams = LayoutUtil.newViewGroupParams(height, height)
        ivClose.tag = "close"
        ivClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        ivClose.background = ViewUtil.newBackgroundDrawable()
        ivClose.setOnClickListener(this)

        tvTitle = TextView(context)
        tvTitle.setTextColor(Color.WHITE)
        tvTitle.textSize = 18f

        tLayout.addView(ivClose)
        tLayout.addView(tvTitle)

        ivMore = ImageButton(context)
        ivMore.tag = "more"
        ivMore.setImageResource(android.R.drawable.ic_menu_more)
        ivMore.background = ViewUtil.newBackgroundDrawable()
        ivMore.setOnClickListener(this)

        val params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL

        val imageParams = LayoutUtil.newFrameLayoutParams(height, height)
        imageParams.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT

        addView(tLayout, params)
        addView(ivMore, imageParams)

        hideClose()
        hideMore()
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun showMore() {
        ViewUtil.setVisibility(ivMore, View.VISIBLE)
    }

    fun hideMore() {
        ViewUtil.setVisibility(ivMore, View.GONE)
    }

    fun showClose() {
        ViewUtil.setVisibility(ivClose, View.VISIBLE)
        tvTitle.setPadding(0, 0, 0, 0)
    }

    fun hideClose() {
        ViewUtil.setVisibility(ivClose, View.GONE)
        tvTitle.setPadding(DisplayUtil.dip2px(context, 15f), 0, 0, 0)
    }

    fun getOnTitleEventListener(): OnTitleEventListener? {
        return mOnTitleEventListener
    }

    fun setOnTitleEventListener(onTitleEventListener: OnTitleEventListener) {
        mOnTitleEventListener = onTitleEventListener
    }

    override fun onClick(v: View) {

        if (mOnTitleEventListener == null) return

        val tag = v.tag as String

        if ("close" == tag) {
            mOnTitleEventListener!!.onCloseEvent(v)
        } else {
            mOnTitleEventListener!!.onMoreEvent(v)
        }
    }

    interface OnTitleEventListener {

        fun onCloseEvent(view: View)

        fun onMoreEvent(view: View)
    }
}