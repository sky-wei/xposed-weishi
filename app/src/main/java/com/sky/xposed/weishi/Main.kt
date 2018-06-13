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

import android.app.Application
import com.sky.xposed.weishi.hook.HookManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Main : IXposedHookLoadPackage {

    override fun handleLoadPackage(lparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lparam.packageName

        if (Constant.WeiShi.PACKAGE_NAME == packageName) {
            // 初始化
            hookWeiShiApplication(lparam)
        }
    }

    fun hookWeiShiApplication(lparam: XC_LoadPackage.LoadPackageParam) {

        XposedHelpers.findAndHookMethod(
                "com.tencent.oscar.app.LifePlayApplication",
                lparam.classLoader,
                "onCreate",
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)

                        val application = param.thisObject as Application
                        val context = application.applicationContext

                        HookManager
                                .getInstance()
                                .initialization(context, lparam)
                                .handleLoadPackage()
                    }
                }
        )
    }
}