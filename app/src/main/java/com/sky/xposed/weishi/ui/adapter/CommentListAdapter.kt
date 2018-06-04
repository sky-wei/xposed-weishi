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

package com.sky.xposed.weishi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sky.xposed.weishi.ui.base.BaseListAdapter
import com.sky.xposed.weishi.ui.view.CommentItemView

class CommentListAdapter(context: Context): BaseListAdapter<String>(context) {

    override fun onCreateView(
            layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): View {

        val commentItemView = CommentItemView(context)

        commentItemView

        return commentItemView
    }

    override fun onCreateViewHolder(view: View?, viewType: Int): ViewHolder<String>? {

        if (view == null) return null

        return CommentViewHolder(view, this)
    }


    private inner class CommentViewHolder(itemView: View, baseListAdapter: BaseListAdapter<String>)
        : ViewHolder<String>(itemView, baseListAdapter) {

        lateinit var mCommentItemView: CommentItemView

        override fun onInitialize() {
            super.onInitialize()

            mCommentItemView = itemView as CommentItemView
        }

        override fun onBind(position: Int, viewType: Int) {

            val value = getItem(position) ?: return

            // 设置信息
            mCommentItemView.setContent(value)
        }
    }
}