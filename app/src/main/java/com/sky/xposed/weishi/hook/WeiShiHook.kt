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

package com.sky.xposed.weishi.hook

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.sky.xposed.weishi.hook.base.BaseHook
import com.sky.xposed.weishi.util.Alog
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WeiShiHook : BaseHook() {

    override fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        testHook()
    }

    private fun testHook() {

//        findAndBeforeHookMethod(
//                "android.support.v4.app.Fragment",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }
//
//        findAndBeforeHookMethod(
//                "android.support.v7.app.AppCompatDialog",
//                "onCreate",
//                Bundle::class.java
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> onCreate " + it.thisObject.javaClass)
//        }
//
//        findAndBeforeHookMethod(
//                "android.app.Dialog",
//                "show"
//        ) {
//
//            Alog.d(">>>>>>>>>>>>>>>>>>>> show " + it.thisObject.javaClass)
//        }


        findAndBeforeHookMethod(
                "com.tencent.oscar.module.share.a.b",
                "a",
                View::class.java, Int::class.java
        ) {

            Alog.d(">>>>>>>>>>>>>>>>>>>> show " + it.thisObject.javaClass)
        }
    }

    fun onModifyValue(key: String, value: Any) {

    }
}