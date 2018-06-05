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

package com.sky.xposed.weishi.ui.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.BounceInterpolator
import android.widget.*
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.sky.xposed.weishi.Constant
import com.sky.xposed.weishi.ui.adapter.CommentListAdapter
import com.sky.xposed.weishi.ui.base.BaseDialogFragment
import com.sky.xposed.weishi.ui.util.LayoutUtil
import com.sky.xposed.weishi.ui.util.ViewUtil
import com.sky.xposed.weishi.ui.view.CommonFrameLayout
import com.sky.xposed.weishi.ui.view.TitleView
import com.sky.xposed.weishi.util.DisplayUtil
import com.sky.xposed.weishi.util.VToast

/**
 * Created by sky on 18-6-3.
 */
class CommentListDialog : BaseDialogFragment(),
        SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout
    private lateinit var mAddCommonButton: Button
    private lateinit var mSwipeMenuListView: SwipeMenuListView

    private lateinit var mCommentListAdapter: CommentListAdapter
    private val mCommentList = ArrayList<String>()
    private var mSaveCommentList = false

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.getTitleView()

        val layout = LayoutUtil.newCommonLayout(context)

        val headLayout = FrameLayout(context)
        headLayout.layoutParams = LayoutUtil.newFrameLayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

        val tvTips = TextView(context)
        tvTips.setTextColor(0xffafafaf.toInt())
        tvTips.textSize = 10f
        tvTips.text = "提示:单击编辑左滑可删除"

        mAddCommonButton = Button(context)
        mAddCommonButton.text = "添加"
        mAddCommonButton.textSize = 14f
        mAddCommonButton.setTextColor(0xFFF93F25.toInt())
        mAddCommonButton.setBackgroundColor(0x00000000)

        val tipsParams = LayoutUtil.newWrapFrameLayoutParams()
        tipsParams.leftMargin = DisplayUtil.dip2px(context, 15f)
        tipsParams.gravity = Gravity.CENTER_VERTICAL

        val params = LayoutUtil.newFrameLayoutParams(
                DisplayUtil.dip2px(context, 70f), DisplayUtil.dip2px(context, 40f))
        params.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

        headLayout.addView(tvTips, tipsParams)
        headLayout.addView(mAddCommonButton, params)

        mSwipeMenuListView = SwipeMenuListView(context)
        mSwipeMenuListView.cacheColorHint = 0x00000000
        mSwipeMenuListView.dividerHeight = 0
        mSwipeMenuListView.setMenuCreator(newMenuCreator())
        mSwipeMenuListView.closeInterpolator = BounceInterpolator()
        mSwipeMenuListView.layoutParams = LayoutUtil.newMatchLinearLayoutParams()

        layout.addView(headLayout)
        layout.addView(mSwipeMenuListView)

        mCommonFrameLayout.setContent(layout)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("评论内容列表")

        mCommentListAdapter = CommentListAdapter(context)
        mCommentListAdapter.items = mCommentList

        mAddCommonButton.setOnClickListener {
            // 添加
            showEditDialog("新添加评论", "") {

                if (TextUtils.isEmpty(it)) {
                    // 异常情况
                    VToast.show("无法添加空评论!")
                } else {
                    // 添加到列表中
                    mSaveCommentList = true
                    mCommentList.add(it)
                    mCommentListAdapter.notifyDataSetChanged()
                }
            }
        }

        mSwipeMenuListView.onItemClickListener = this
        mSwipeMenuListView.setOnMenuItemClickListener(this)
        mSwipeMenuListView.adapter = mCommentListAdapter

        // 设置加载的评论信息
        mCommentList.clear()
        mCommentList.addAll(loadUserComment())
        mCommentListAdapter.notifyDataSetChanged()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        if (mSaveCommentList) {
            // 保存评论列表
            saveUserComment(mCommentList)
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        showEditDialog("编辑评论", mCommentList[position]) {

            if (TextUtils.isEmpty(it)) {
                // 异常情况
                VToast.show("编辑的评论不能为空!")
            } else {
                // 添加到列表中
                mSaveCommentList = true
                mCommentList[position] = it
                mCommentListAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onMenuItemClick(position: Int, menu: SwipeMenu, index: Int): Boolean {

        // 删除指定评论
        mSaveCommentList = true
        mCommentList.removeAt(position)
        mCommentListAdapter.notifyDataSetChanged()

        return true
    }

    /**
     * 加载用户评论
     */
    private fun loadUserComment(): List<String> {

        val commentSet = getDefaultSharedPreferences()
                .getStringSet(Constant.Preference.AUTO_COMMENT_LIST, HashSet<String>())

        return commentSet.map { it }
    }

    /**
     * 保存用户评论
     */
    private fun saveUserComment(commentList: List<String>) {

        val commentSet = commentList.toHashSet()

        getDefaultSharedPreferences()
                .edit()
                .putStringSet(Constant.Preference.AUTO_COMMENT_LIST, commentSet)
                .apply()

        // 发送修改广播
        sendRefreshPreferenceBroadcast(Constant.Preference.AUTO_COMMENT_LIST, commentSet)
    }

    /**
     * 显示编辑的Dialog提示框
     */
    private fun showEditDialog(title: String, content: String, onTextChange:(content: String) -> Unit) {

        val top = DisplayUtil.dip2px(context, 10f)
        val left = DisplayUtil.dip2px(context, 24f)

        val frameLayout = FrameLayout(context)
        frameLayout.layoutParams = LayoutUtil.newFrameLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        frameLayout.setPadding(left, top, left, top)

        val editText = EditText(context)
        editText.setText(content)
        editText.layoutParams = LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ViewUtil.setInputType(editText, Constant.InputType.TEXT)
        frameLayout.addView(editText)

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setView(frameLayout)
        builder.setPositiveButton("确定") { _, _ ->
            // 返回文本的内容
            onTextChange(editText.text.toString())
        }
        builder.setNegativeButton("取消", null)
        builder.show()
    }

    /**
     * 创建左滑菜单
     */
    private fun newMenuCreator(): SwipeMenuCreator {

        return SwipeMenuCreator { menu ->

            val deleteItem = SwipeMenuItem(context)

            deleteItem.background = ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25))
            deleteItem.width = DisplayUtil.dip2px(context, 80f)
            deleteItem.title = "删除"
            deleteItem.titleSize = 14
            deleteItem.titleColor = Color.WHITE

            menu.addMenuItem(deleteItem)
        }
    }
}