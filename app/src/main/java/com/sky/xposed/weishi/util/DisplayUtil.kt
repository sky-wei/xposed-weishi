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

package com.sky.xposed.weishi.util

import android.content.Context

object DisplayUtil {

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getScaledDensity(context: Context): Float {
        return context.resources.displayMetrics.scaledDensity
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        return dip2px(dipValue, getDensity(context))
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        return px2dip(pxValue, getDensity(context))
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        return px2sp(pxValue, getScaledDensity(context))
    }

    fun sp2px(context: Context, spValue: Float): Int {
        return sp2px(spValue, getScaledDensity(context))
    }

    fun px2dip(pxValue: Float, scale: Float): Int {
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dip2px(dipValue: Float, scale: Float): Int {
        return (dipValue * scale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float, fontScale: Float): Int {
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(spValue: Float, fontScale: Float): Int {
        return (spValue * fontScale + 0.5f).toInt()
    }
}