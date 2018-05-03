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
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import java.io.ByteArrayInputStream
import java.io.Serializable
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object PackageUitl {

    private val TAG = "PackageUtil"

    fun getSerialNumber(context: Context?, packageName: String): String? {

        if (context == null || TextUtils.isEmpty(packageName)) return null

        try {
            val packageInfo = getPackageInfo(
                    context, packageName, PackageManager.GET_SIGNATURES) ?: return null

            val signs = packageInfo.signatures
            val sign = signs[0]

            val certFactory = CertificateFactory.getInstance("X.509")
            val certificate = certFactory.generateCertificate(ByteArrayInputStream(sign.toByteArray())) as X509Certificate

            return certificate.serialNumber.toString()
        } catch (e: Exception) {
            Alog.e(TAG, "获取包签名异常", e)
        }

        return null
    }

    /**
     * 获取指定包的版本名
     * @param context
     * @param packageName
     * @return
     */
    fun getVersionName(context: Context?, packageName: String?): String? {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return packageInfo?.versionName
    }

    /**
     * 获取指定包的版本号
     * @param context
     * @param packageName
     * @return
     */
    fun getVersionCode(context: Context?, packageName: String?): Int {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return packageInfo?.versionCode ?: 0
    }

    /**
     * 获取当前应用的版本名
     * @param context
     * @return
     */
    fun getVersionName(context: Context?): String? {
        return getVersionName(context, context?.packageName)
    }

    /**
     * 获取当前应用的版本号
     * @param context
     * @return
     */
    fun getVersionCode(context: Context?): Int {
        return getVersionCode(context, context?.packageName)
    }

    /**
     * 返回指定的包是否安装
     * @param context
     * @param packageName
     * @return
     */
    fun isInstall(context: Context, packageName: String): Boolean {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return packageInfo != null
    }

    /**
     * 获取简单的包信息
     * @param context
     * @param packageName
     * @return
     */
    fun getSimplePackageInfo(context: Context, packageName: String): SimplePackageInfo? {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return if (packageInfo != null)
            SimplePackageInfo(
                    packageName, packageInfo.versionName, packageInfo.versionCode)
        else
            null
    }

    /**
     * 获取指定包信息
     * @param context
     * @param packageName
     * @param flag
     * @return
     */
    fun getPackageInfo(context: Context?, packageName: String?, flag: Int): PackageInfo? {

        if (context == null || TextUtils.isEmpty(packageName)) return null

        try {
            val manager = context.packageManager
            return manager.getPackageInfo(packageName, flag)
        } catch (e: PackageManager.NameNotFoundException) {
            Alog.e(TAG, "获取的包不存在", e)
        }

        return null
    }

    class SimplePackageInfo(var packageName: String?, var versionName: String?, var versionCode: Int) : Serializable
}