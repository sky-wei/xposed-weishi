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
import android.content.Context
import android.content.Intent
import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.ToStringUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Main : IXposedHookLoadPackage {

    override fun handleLoadPackage(lparam: XC_LoadPackage.LoadPackageParam) {

        val packageName = lparam.packageName

        if (Constant.WeiShi.PACKAGE_NAME == packageName) {
            // 初始化
            hookWeiShiApplication(lparam)
        } else if ("com.excelliance.dualaid" == packageName) {
            hookDuaLaidApplication(lparam)
        }
    }

    fun hookWeiShiApplication(lparam: XC_LoadPackage.LoadPackageParam) {

        XposedHelpers.findAndHookMethod(
                "com.tencent.oscar.app.LifePlayApplication",
                lparam.classLoader,
                "onCreate",
                object : XC_MethodHook() {

                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)

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

    fun hookDuaLaidApplication(lparam: XC_LoadPackage.LoadPackageParam) {

//        XposedHelpers.findAndHookMethod(
//                Runtime::class.java,
//                "exec",
//                String::class.java,
//                object : XC_MethodHook() {
//                    override fun beforeHookedMethod(param: MethodHookParam) {
//                        super.beforeHookedMethod(param)
//
//                        Alog.d(">>>>>>>>>>>>>>>>>> ${param.thisObject}")
//                        Alog.d(">>>>>>>>>>>>>>>>>> ${param.args[0]}")
//                    }
//                })

        XposedHelpers.findAndHookMethod(
                "com.excelliance.kxqp.KXQPApplication",
                lparam.classLoader,
                "onCreate",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)

                        try {
                            test(lparam, param)
                        } catch (tr: Throwable) {
                            Alog.d("异常了", tr)
                        }
                    }
                })
    }

    fun test(lparam: XC_LoadPackage.LoadPackageParam, param: XC_MethodHook.MethodHookParam) {

        val addGameActivityClass = XposedHelpers.findClass(
                "com.excelliance.kxqp.platforms.AddGameActivity", lparam.classLoader)
        val tClass = XposedHelpers.findClass(
                "com.excelliance.kxqp.t", lparam.classLoader)

        XposedHelpers.findAndHookMethod(
                addGameActivityClass,
                "a",
                tClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)

                        param.result = false

//                        Alog.d(">>>>>>>>>>>>>>>>>>>>>> ${param.result}")
                    }
                }
        )

        XposedHelpers.findAndHookMethod(
                "android.content.ContextWrapper",
                lparam.classLoader,
                "sendBroadcast",
                Intent::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)

                        val intent = param.args[0] as Intent

                        if (intent.action == "com.excelliance.dualaid.action.dlist") {
                            intent.putExtra("pay", true)
//                            val game = intent.getSerializableExtra("game")
//                            XposedHelpers.setIntField(game, "J", 1)
//                            intent.putExtra("game", game as Serializable)

                            param.args[0] = intent
                        }

//                        Alog.d(">>>>>>>>>>>>>>>>> sendBroadcast " + param.thisObject)
                        ToStringUtil.toString(param.args[0])
                    }
                }
        )

        XposedHelpers.findAndHookMethod(
                "com.excelliance.kxqp.pay.ali.b",
                lparam.classLoader,
                "i",
                Context::class.java,
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        return true
                    }
                }
        )

        XposedHelpers.findAndHookMethod(
                "com.excelliance.kxqp.pay.ali.b",
                lparam.classLoader,
                "g",
                Context::class.java,
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        return true
                    }
                }
        )
    }
}