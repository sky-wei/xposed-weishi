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

package com.sky.xposed.weishi.ui.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sky.xposed.weishi.BuildConfig
import com.sky.xposed.weishi.R
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.DisplayUtil
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by sky on 18-6-6
 */
object CommUtil {

    fun showAboutDialog(context: Context) {

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
            tvHead.setTextColor(Color.BLACK)
            tvHead.textSize = 14f
            tvHead.text = "版本：v${BuildConfig.VERSION_NAME}\n想了解更多类似作品或者二叶草最新动态,加入二叶草社区"

            val ivCommunity = ImageView(context)
            ivCommunity.layoutParams = LayoutUtil.newWrapLinearLayoutParams()
            Picasso.get().load(resourceIdToUri(R.drawable.community)).into(ivCommunity)

            val tvTail = TextView(context)
            tvTail.setTextColor(Color.BLACK)
            tvTail.textSize = 14f
            tvTail.text = "官方QQ群：781985751\n版权所有　二叶草出品"

            content.addView(tvHead)
            content.addView(ivCommunity)
            content.addView(tvTail)

            // 显示关于
            val builder = AlertDialog.Builder(context)
            builder.setTitle("关于")
            builder.setView(content)
            builder.setPositiveButton("确定", { dialog, _ -> dialog.dismiss() })
            builder.show()
        } catch (tr: Throwable) {
            Alog.e("异常了", tr)
        }
    }

    fun resourceIdToUri(resourceId: Int): Uri {
        return Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/$resourceId")
    }

    fun scanFile(context: Context, file: String) {

        val data = Uri.parse("file://$file")
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data))
    }

    fun saveImage2SDCard(qrSavePath: String, qrBitmap: Bitmap): Boolean {

        try {
            val qrFile = File(qrSavePath)

            val parentFile = qrFile.parentFile
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }

            val fos = FileOutputStream(qrFile)
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            return true
        } catch (e: IOException) {
            Alog.e("保存失败", e)
        }
        return false
    }
}