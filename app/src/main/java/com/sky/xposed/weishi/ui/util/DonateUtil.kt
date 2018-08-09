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

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.sky.xposed.common.util.Alog
import com.sky.xposed.common.util.RandomUtil
import com.sky.xposed.common.util.ToastUtil
import com.sky.xposed.weishi.Constant

/**
 * Created by sky on 2018/8/9.
 */
object DonateUtil {

    private const val ALI_PAY_URI = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode="

    private val sPassword = arrayOf(
            "https://qr.alipay.com/c1x08451uibdwheqvzojiad",
            "https://qr.alipay.com/c1x06511qhtctpjl7i53dc1"
    )

    /**
     * 显示红包提示框
     * @param context
     * @param sharedPreferences
     */
    fun showHbDialog(context: Context, sharedPreferences: SharedPreferences) {

        val curTime = System.currentTimeMillis()
        val lastTime = sharedPreferences.getLong(Constant.Preference.HB_LAST_TIME, 0)

        if (curTime > lastTime && curTime - lastTime < Constant.Time.HB_MAX_TIME) {
            // 不需要处理
            return
        }

        // 显示提示框
        val builder = AlertDialog.Builder(context)
        builder.setTitle("提示")
        builder.setMessage("感谢您的使用，如果觉得助手好用，每天领个红包也是对作者最好的支持！ 谢谢！")
        builder.setCancelable(false)
        builder.setPositiveButton("去领取") { dialog, which ->
            // 去领取红包
            receiveAliPayHb(context)
        }
        builder.setNegativeButton("残忍拒绝") { dialog, which ->
            // 保存最后时间
            saveLastHbTime(context)
        }
        builder.show()
    }

    /**
     * 保存最后时间
     * @param context
     */
    private fun saveLastHbTime(context: Context) {

        val sharedPreferences = context
                .getSharedPreferences(Constant.Name.WEI_SHI, Context.MODE_PRIVATE)

        val curTime = System.currentTimeMillis()
        // 保存最后时间
        sharedPreferences
                .edit()
                .putLong(Constant.Preference.HB_LAST_TIME, curTime)
                .apply()
    }

    /**
     * 一键领取支付宝红包
     * @param context
     */
    fun receiveAliPayHb(context: Context) {

        saveLastHbTime(context)

        if (!startAlipay(context,
                        sPassword[RandomUtil.randomIndex(sPassword.size)])) {
            ToastUtil.show("启动支付宝失败")
        }
    }

    /**
     * 启动支付宝
     * @param context
     * @param payUrl
     * @return
     */
    fun startAlipay(context: Context, payUrl: String): Boolean {

        return try {
            val intent = Intent("android.intent.action.VIEW")
            intent.data = Uri.parse(ALI_PAY_URI + payUrl)

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                intent.data = Uri.parse(payUrl)
                context.startActivity(intent)
            }
            true
        } catch (tr: Throwable) {
            Alog.e("启动失败", tr)
            false
        }
    }
}