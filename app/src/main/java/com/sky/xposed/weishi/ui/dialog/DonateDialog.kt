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

package com.sky.xposed.weishi.ui.dialog

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sky.xposed.common.ui.util.LayoutUtil
import com.sky.xposed.common.ui.util.ViewUtil
import com.sky.xposed.common.ui.view.CommonFrameLayout
import com.sky.xposed.common.ui.view.SimpleItemView
import com.sky.xposed.common.ui.view.TitleView
import com.sky.xposed.common.util.Alog
import com.sky.xposed.common.util.DisplayUtil
import com.sky.xposed.common.util.ToastUtil
import com.sky.xposed.weishi.R
import com.sky.xposed.weishi.ui.base.BaseDialog
import com.sky.xposed.weishi.ui.util.DialogUtil
import com.sky.xposed.weishi.ui.util.DonateUtil
import com.sky.xposed.weishi.ui.util.UriUtil
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.lang.Exception


/**
 * Created by sky on 18-6-7
 */
class DonateDialog : BaseDialog() {

    private lateinit var mToolbar: TitleView
    private lateinit var mCommonFrameLayout: CommonFrameLayout
    private lateinit var sivAliPayDonate: SimpleItemView
    private lateinit var sivWeChatDonate: SimpleItemView

    companion object {

        const val CLICK = 0x01

        const val LONG_CLICK = 0x02
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {

        // 不显示默认标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mCommonFrameLayout = CommonFrameLayout(context)
        mToolbar = mCommonFrameLayout.titleView

        sivAliPayDonate = ViewUtil.newSimpleItemView(context, "支付宝捐赠")
        sivWeChatDonate = ViewUtil.newSimpleItemView(context, "微信捐赠")

        mCommonFrameLayout.addContent(sivAliPayDonate)
        mCommonFrameLayout.addContent(sivWeChatDonate)

        return mCommonFrameLayout
    }

    override fun initView(view: View, args: Bundle?) {

        mToolbar.setTitle("支持我们")
        mToolbar.showBack()
        mToolbar.setOnBackEventListener { dismiss() }

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(mToolbar.backView)

        sivAliPayDonate.setOnClickListener {
            // 启动支付宝
            showDonateImageDialog(
                    "*点击支付二维码即可打开支付宝*",
                    DialogUtil.resourceIdToUri(R.drawable.alipay),
                    CLICK) { _, _ ->

                // 直接拉起支付宝
                ToastUtil.show("正在启动支付宝，感谢您的支持！")
                if (!DonateUtil.startAlipay(context,
                                "HTTPS://QR.ALIPAY.COM/FKX05224Z5KOVCQ61BQ729")) {
                    ToastUtil.show("启动支付宝失败")
                }
                true
            }
        }

        sivWeChatDonate.setOnClickListener {
            // 微信捐赠
            showDonateImageDialog(
                    "*长按保存到相册,再通过微信扫码二维码*",
                    DialogUtil.resourceIdToUri(R.drawable.wechat),
                    LONG_CLICK) { _, uri ->

                Picasso.get().load(uri).into(object : Target {

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        ToastUtil.show("保存图片失败")
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                        if (bitmap == null) return

                        val imagePath = File(Environment
                                .getExternalStorageDirectory(), "DCIM/wecaht.png")

                        // 保存图片
                        if (DialogUtil.saveImage2SDCard(imagePath.path, bitmap)) {
                            DialogUtil.scanFile(context, imagePath.path)
                            ToastUtil.show("图片已保存到本地，感谢您的支持！")
                        } else {
                            ToastUtil.show("图片保存失败！")
                        }
                    }
                })

                true
            }
        }
    }

    private fun showDonateImageDialog(desc: String, uri: Uri,
                                      eventType: Int, event: (eventType: Int, uri: Uri) -> Boolean) {

        try {
            val left = DisplayUtil.dip2px(context, 25f)
            val top = DisplayUtil.dip2px(context, 10f)

            val contentParams = LayoutUtil.newMatchLinearLayoutParams()

            val content = LinearLayout(context)
            content.layoutParams = contentParams
            content.orientation = LinearLayout.VERTICAL
            content.setBackgroundColor(Color.WHITE)
            content.setPadding(left, top, left, 0)

            val tvHead = TextView(context)
            tvHead.setTextColor(0xff8f8f8f.toInt())
            tvHead.textSize = 12f
            tvHead.text = desc

            val ivImage = ImageView(context)
            ivImage.layoutParams = LayoutUtil.newWrapLinearLayoutParams()
            Picasso.get().load(uri).into(ivImage)

            when (eventType) {
                CLICK -> { ivImage.setOnClickListener { event(eventType, uri) } }
                LONG_CLICK -> { ivImage.setOnLongClickListener { event(eventType, uri) } }
            }

            content.addView(tvHead)
            content.addView(ivImage)

            // 显示关于
            val builder = AlertDialog.Builder(context)
            builder.setTitle("感谢您的捐赠")
            builder.setView(content)
            builder.setPositiveButton("确定", { dialog, _ -> dialog.dismiss() })
            builder.show()
        } catch (tr: Throwable) {
            Alog.e("异常了", tr)
        }
    }
}