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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by sky on 18-6-3.
 */
abstract class BaseListAdapter<T>(val context: Context) : BaseAdapter() {

    var items: List<T>? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return if (items == null) 0 else items!!.size
    }

    override fun getItem(position: Int): T? {
        return if (items == null) null else items!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val viewType = getItemViewType(position)

        if (convertView == null) {

            // 创建View
            convertView = onCreateView(mLayoutInflater, parent, viewType)

            // 创建ViewHolder
            val viewHolder = onCreateViewHolder(convertView, viewType)

            // 初始化操作
            viewHolder?.onInitialize()

            // 保存
            convertView.tag = viewHolder
        }

        val viewHolder = convertView.tag as ViewHolder<*>

        if (viewHolder != null) {
            // 进行绑定
            viewHolder.adapterPosition = position
            viewHolder.onBind(position, viewType)
        }

        return convertView
    }

    /**
     * 实例显示的View
     * @param layoutInflater
     * @param parent
     * @param viewType
     * @return
     */
    abstract fun onCreateView(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): View

    /**
     * 初始化View
     * @param view
     * @return
     */
    abstract fun onCreateViewHolder(view: View?, viewType: Int): ViewHolder<T>?


    abstract inner class ViewHolder<T>(
            val itemView: View, val baseListAdapter: BaseListAdapter<T>) {

        /**
         * 获取适配器中数据下标
         * @return
         */
        var adapterPosition: Int = 0

        /**
         * 获取适配器的Item的数量
         * @return
         */
        fun getCount(): Int {
            return baseListAdapter.count
        }

        open fun onInitialize() {

        }

        /**
         * 绑定View，用于处理数据跟View进行关联
         * @param position 数据索引id
         * @param viewType View类型
         */
        abstract fun onBind(position: Int, viewType: Int)

        /**
         * 通过控件id查找相应的View
         * @param id 控件id
         * @return 返回View
         */
        fun findViewById(id: Int): View? {
            return if (itemView == null) null else itemView!!.findViewById(id)
        }

        /**
         * 获取指定索引id的内容信息
         * @param position 索引id
         * @return 指定id的内容信息
         */
        fun getItem(position: Int): T? {
            return baseListAdapter.getItem(position)
        }
    }
}