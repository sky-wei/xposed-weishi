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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.sky.xposed.weishi.R


class ItemMenu : FrameLayout {

    private lateinit var viewLine: View
    private lateinit var tvName: TextView
    private lateinit var tvDesc: TextView
    private lateinit var ivArrow: ImageView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        LayoutInflater.from(getContext())
                .inflate(R.layout.customize_item_menu, this, true)

        viewLine = findViewById(R.id.view_line)
        tvName = findViewById(R.id.tv_name)
        tvDesc = findViewById(R.id.tv_desc)
        ivArrow = findViewById(R.id.iv_arrow)

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.ItemMenu, defStyleAttr, 0)

        for (i in 0 until a.indexCount) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.ItemMenu_sky_itemMenuName -> setName(a.getString(attr))
                R.styleable.ItemMenu_sky_itemMenuDesc -> setDesc(a.getString(attr))
                R.styleable.ItemMenu_sky_itemMenuLine -> setShowLine(a.getBoolean(attr, true))
                R.styleable.ItemMenu_sky_itemMenuTextSize -> setTextSize(a.getDimensionPixelSize(attr, 14))
                R.styleable.ItemMenu_sky_itemMenuShowMore -> setShowMore(a.getBoolean(attr, true))
            }
        }
        a.recycle()
    }

    fun setName(name: String) {
        tvName.text = name
    }

    fun setDesc(desc: String) {
        tvDesc.text = desc
    }

    fun setTextSize(size: Int) {
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
        tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
    }

    fun setShowLine(show: Boolean) {
        viewLine.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setShowMore(show: Boolean) {
        ivArrow.visibility = if (show) View.VISIBLE else View.GONE
    }
}