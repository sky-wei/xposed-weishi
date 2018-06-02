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

package com.sky.xposed.weishi.hook.version

import android.content.Context
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.PackageUitl

/**
 * Created by sky on 18-6-2.
 */
object VersionManager {

    private val VERSION_MAP = HashMap<String, Class<out WeiShiHook>>()

    init {
        VERSION_MAP["4.3.0.88"] = WeiShiHook43088::class.java
    }

    fun getSupportWeiShiHook(packageInfo: PackageUitl.SimplePackageInfo?): WeiShiHook {

        if (packageInfo == null
                || !VERSION_MAP.containsKey(packageInfo.versionName)) {
            // 返回默认的
            return WeiShiHook()
        }

        return getSupportWeiShiHook(VERSION_MAP[packageInfo.versionName])
    }

    fun getSupportWeiShiHook(tClass: Class<out WeiShiHook>?): WeiShiHook {

        if (tClass == null) return WeiShiHook()

        try {
            // 创建实例
            return tClass.newInstance()
        } catch (tr: Throwable) {
            Alog.d("创建版本Hook异常", tr)
        }
        return WeiShiHook()
    }

    fun getPackageInfo(context: Context): PackageUitl.SimplePackageInfo? {
        return PackageUitl.getSimplePackageInfo(context, context.packageName)
    }
}