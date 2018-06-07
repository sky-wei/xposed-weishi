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

package com.sky.xposed.weishi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.sky.xposed.weishi.ui.dialog.DonateDialog
import com.sky.xposed.weishi.ui.dialog.SettingsDialog
import com.sky.xposed.weishi.ui.util.CommUtil
import com.sky.xposed.weishi.ui.view.ItemMenu
import com.sky.xposed.weishi.util.PackageUitl
import com.sky.xposed.weishi.util.VToast

class MainActivity : Activity() {

    private lateinit var imVersion: ItemMenu
    private lateinit var imWeiShiVersion: ItemMenu
    private lateinit var tvSupportVersion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化
        VToast.getInstance().init(applicationContext)

        imVersion = findViewById(R.id.im_version)
        imWeiShiVersion = findViewById(R.id.im_weishi_version)
        tvSupportVersion = findViewById(R.id.tv_support_version)

        imVersion.setDesc("v${BuildConfig.VERSION_NAME}")
        imWeiShiVersion.setDesc(getWeiShiVersionName())

        tvSupportVersion.text = "支持微视的版本: v4.2.0.88, v4.2.5.88, v4.3.0.88"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (BuildConfig.DEBUG) {
            menuInflater.inflate(R.menu.activity_main_menu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (R.id.menu_settings == item.itemId) {
            val dialog = SettingsDialog()
            dialog.show(fragmentManager, "settings")
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClick(view: View) {

        when(view.id) {
            R.id.im_source -> {
                openUrl("https://github.com/jingcai-wei/xposed-weishi")
            }
            R.id.im_download -> {
                openUrl("http://repo.xposed.info/module/com.sky.xposed.weishi")
            }
            R.id.im_donate -> {
                // 捐赠
                val donateDialog = DonateDialog()
                donateDialog.show(fragmentManager, "donate")
            }
            R.id.im_about -> {
                CommUtil.showAboutDialog(this)
            }
        }
    }

    private fun openUrl(url: String) {

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            // 调用系统浏览器打开
            startActivity(intent)
        } catch (tr: Throwable) {
            VToast.show("打开浏览器异常")
        }
    }

    private fun getWeiShiVersionName(): String {

        // 获取微视版本名
        val info = PackageUitl.getSimplePackageInfo(
                this, Constant.WeiShi.PACKAGE_NAME) ?: return "Unknown"

        return "v${info.versionName}"
    }
}
