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

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.sky.xposed.weishi.ui.interfaces.TrackViewStatus
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.util.DisplayUtil

class EditTextItemView : FrameLayout, View.OnClickListener, TrackViewStatus<String> {

    private lateinit var tvName: TextView
    private var mOnTextChangeListener: OnTextChangeListener? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {

        val left = DisplayUtil.dip2px(context, 15f)

        setPadding(left, 0, left, 0)
        background = ViewUtil.newBackgroundDrawable()
        layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 40f))

        tvName = TextView(context)
        tvName.setTextColor(Color.BLACK)
        tvName.textSize = 15f

        var params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL

        addView(tvName, params)

        params = LayoutUtil.newWrapFrameLayoutParams()
        params.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT

        setOnClickListener(this)
    }

    fun getOnTextChangeListener(): OnTextChangeListener? {
        return mOnTextChangeListener
    }

    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener) {
        mOnTextChangeListener = onTextChangeListener
    }

    fun setName(title: String) {
        tvName.text = title
    }

    fun getName(): String {
        return tvName.text.toString()
    }

    override fun onClick(v: View) {

        if (mOnTextChangeListener == null) return

        val top = DisplayUtil.dip2px(context, 10f)
        val left = DisplayUtil.dip2px(context, 24f)

        val frameLayout = FrameLayout(context)
        frameLayout.layoutParams = LayoutUtil.newFrameLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        frameLayout.setPadding(left, top, left, 0)

        val editText = EditText(context)
        editText.setText(mOnTextChangeListener!!.getDefaultText())
        editText.layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        frameLayout.addView(editText)

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getName())
        builder.setView(frameLayout)
        builder.setPositiveButton("确定") { dialog, which ->
            // 返回文本的内容
            mOnTextChangeListener!!.onTextChanged(editText, editText.text.toString())
        }
        builder.setNegativeButton("取消", null)
        builder.show()
    }

    override fun bind(preferences: SharedPreferences,
             key: String, defValue: String, listener: TrackViewStatus.StatusChangeListener<String>) {

        setOnTextChangeListener(object : OnTextChangeListener {

            override fun getDefaultText(): String {
                // 获取文本信息
                return preferences.getString(key, defValue)
            }

            override fun onTextChanged(view: View, text: String) {
                // 保存信息
                preferences.edit().putString(key, text).apply()
                listener.onStatusChange(view, key, text)
            }
        })
    }

    interface OnTextChangeListener {

        fun getDefaultText(): String

        fun onTextChanged(view: View, text: String)
    }
}