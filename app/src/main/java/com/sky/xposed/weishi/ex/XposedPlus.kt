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

package com.sky.xposed.weishi.ex

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 18-6-1.
 */

object XposedPlus {

    private var mInitialize = false
    private lateinit var mLoadPackage: LoadPackage

    fun initDefaultLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        if (mInitialize) return

        mInitialize = true
        mLoadPackage = XposedPlus.LoadPackage(param)
    }

    fun getDefalutLoadPackage()= mLoadPackage

    fun findClass(className: String): Class<*> {
        return findClass(className, mLoadPackage.getClassLoader())
    }

    fun findClass(className: String, classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(className, classLoader)
    }

    fun findAndBeforeHookMethod(
            className: String, methodName: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookMethod(className, methodName,
                *parameterTypes, beforeHook = beforeHook, afterHook = {})
    }

    fun findAndAfterHookMethod(
            className: String, methodName: String,
            vararg parameterTypes: Any,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookMethod(className, methodName,
                *parameterTypes, beforeHook = {}, afterHook = afterHook)
    }

    fun findAndBeforeHookMethod(
            tClass: Class<*>, methodName: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookMethod(tClass, methodName,
                *parameterTypes, beforeHook = beforeHook, afterHook = {})
    }

    fun findAndAfterHookMethod(
            tClass: Class<*>, methodName: String,
            vararg parameterTypes: Any,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookMethod(tClass, methodName,
                *parameterTypes, beforeHook = {}, afterHook = afterHook)
    }

    fun findAndBeforeHookConstructor(
            className: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookConstructor(className,
                *parameterTypes, beforeHook = beforeHook, afterHook = {})
    }


    fun findAndAfterHookConstructor(
            className: String,
            vararg parameterTypes: Any,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookConstructor(className,
                *parameterTypes, beforeHook = {}, afterHook = afterHook)
    }


    fun findAndBeforeHookConstructor(
            tClass: Class<*>,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookConstructor(tClass,
                *parameterTypes, beforeHook = beforeHook, afterHook = {})
    }


    fun findAndAfterHookConstructor(
            tClass: Class<*>,
            vararg parameterTypes: Any,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        return findAndHookConstructor(tClass,
                *parameterTypes, beforeHook = {}, afterHook = afterHook)
    }

    fun findAndHookMethod(
            className: String, methodName: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodHook(beforeHook, afterHook))

        return findAndHookMethod(className, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(
            tClass: Class<*>, methodName: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodHook(beforeHook, afterHook))

        return findAndHookMethod(tClass, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookConstructor(
            className: String,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodHook(beforeHook, afterHook))

        return findAndHookConstructor(className, *parameterTypesAndCallback)
    }

    fun findAndHookConstructor(
            tClass: Class<*>,
            vararg parameterTypes: Any,
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodHook(beforeHook, afterHook))

        return findAndHookConstructor(tClass, *parameterTypesAndCallback)
    }

    fun findAndHookMethodReplacement(
            className: String, methodName: String,
            vararg parameterTypes: Any,
            replaceHook: (param: XC_MethodHook.MethodHookParam) -> Any?): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodReplacement(replaceHook))

        return findAndHookMethod(className, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethodReplacement(
            tClass: Class<*>, methodName: String,
            replaceHook: (param: XC_MethodHook.MethodHookParam) -> Any?,
            vararg parameterTypes: Any): XC_MethodHook.Unhook {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes, newMethodReplacement(replaceHook))

        return findAndHookMethod(tClass, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(
            tClass: Class<*>, methodName: String,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return XposedHelpers.findAndHookMethod(tClass, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(
            className: String, methodName: String,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return findAndHookMethod(className,
                mLoadPackage.getClassLoader(), methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(
            className: String, classLoader: ClassLoader, methodName: String,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return XposedHelpers.findAndHookMethod(
                className, classLoader, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookConstructor(
            tClass: Class<*>,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return XposedHelpers.findAndHookConstructor(tClass, *parameterTypesAndCallback)
    }

    fun findAndHookConstructor(
            className: String,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return findAndHookConstructor(className,
                mLoadPackage.getClassLoader(), *parameterTypesAndCallback)
    }

    fun findAndHookConstructor(
            className: String, classLoader: ClassLoader,
            vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {

        return XposedHelpers.findAndHookConstructor(
                className, classLoader, *parameterTypesAndCallback)
    }

    fun newMethodReplacement(
            replaceHook: (param: XC_MethodHook.MethodHookParam) -> Any?): XC_MethodReplacement {

        return object : XC_MethodReplacement() {

            override fun replaceHookedMethod(param: MethodHookParam): Any? {
                // 直接调用
                return replaceHook(param)
            }
        }
    }

    fun newMethodHook(
            beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
            afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit): XC_MethodHook {

        return object : XC_MethodHook() {

            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                // 直接调用
                beforeHook(param)
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                // 直接调用
                afterHook(param)
            }
        }
    }

    class LoadPackage(private val param: XC_LoadPackage.LoadPackageParam) {

        fun getClassLoader(): ClassLoader {
            return param.classLoader
        }
    }
}