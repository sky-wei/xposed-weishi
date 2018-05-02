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

package com.sky.xposed.weishi.ui.base

import android.app.DialogFragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.interfaces.TrackViewStatus

abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        return createView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化View
        initView(view, arguments)
    }

    /**
     * 针对New Dialog处理的
     * @return
     */
    open fun createDialogView(): View {

        // 创建View
        val view = createView(getSkyLayoutInflater(), null)

        // 初始化
        initView(view, arguments)

        return view
    }

    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?): View

    abstract fun initView(view: View, args: Bundle)

    override fun getContext(): Context {
        return activity
    }

    fun getSkyLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    fun getApplicationContext(): Context {
        return activity.applicationContext
    }

    fun getSharedPreferences(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getDefaultSharedPreferences(): SharedPreferences {
        return getSharedPreferences(Constant.Name.WEI_SHI)
    }

    fun <T> trackBind(track: TrackViewStatus<T>, key: String, defValue: T, listener: TrackViewStatus.StatusChangeListener<T>) {
        track.bind(getDefaultSharedPreferences(), key, defValue, listener)
    }
}