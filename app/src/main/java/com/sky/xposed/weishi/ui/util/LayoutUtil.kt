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

package com.sky.xposed.weishi.ui.util

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sky.xposed.weishi.util.DisplayUtil

object LayoutUtil {

    fun newLinearLayoutParams(width: Int, height: Int): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(width, height)
    }

    fun newMatchLinearLayoutParams(): LinearLayout.LayoutParams {
        return newLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    fun newWrapLinearLayoutParams(): LinearLayout.LayoutParams {
        return newLinearLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    fun newViewGroupParams(width: Int, height: Int): ViewGroup.LayoutParams {
        return LinearLayout.LayoutParams(width, height)
    }

    fun newMatchViewGroupParams(): ViewGroup.LayoutParams {
        return newViewGroupParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun newWrapViewGroupParams(): ViewGroup.LayoutParams {
        return newViewGroupParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun newFrameLayoutParams(width: Int, height: Int): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(width, height)
    }

    fun newMatchFrameLayoutParams(): FrameLayout.LayoutParams {
        return newFrameLayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    }

    fun newWrapFrameLayoutParams(): FrameLayout.LayoutParams {
        return newFrameLayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
    }

    fun newCommonLayout(context: Context): LinearLayout {

        val content = LinearLayout(context)
        content.layoutParams = LayoutUtil.newMatchLinearLayoutParams()
        content.minimumWidth = DisplayUtil.sp2px(context, 300f)
        content.orientation = LinearLayout.VERTICAL
        content.setBackgroundColor(Color.WHITE)

        return content
    }
}