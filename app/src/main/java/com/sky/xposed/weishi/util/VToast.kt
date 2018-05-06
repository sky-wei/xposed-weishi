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
import android.view.Gravity
import android.widget.Toast

class VToast private constructor() {

    private lateinit var mContext: Context

    companion object {

        private val V_TOAST by lazy { VToast() }

        fun getInstance(): VToast {
            return V_TOAST
        }

        fun show(text: CharSequence) {
            getInstance().showMessage(text, Toast.LENGTH_SHORT)
        }

        fun show(text: CharSequence, duration: Int) {
            getInstance().showMessage(text, duration)
        }

        fun show(resId: Int) {
            getInstance().showMessage(resId, Toast.LENGTH_SHORT)
        }

        fun show(resId: Int, duration: Int) {
            getInstance().showMessage(resId, duration)
        }
    }

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    fun showMessage(text: CharSequence, duration: Int) {
        val toast = Toast.makeText(mContext, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun showMessage(resId: Int, duration: Int) {
        showMessage(mContext!!.getString(resId), duration)
    }
}