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

object ResourceUtil {

    fun getId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "id")
    }

    fun getLayoutId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "layout")
    }

    fun getStringId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "string")
    }

    fun getDrawableId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "drawable")
    }

    fun getMipmapId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "mipmap")
    }

    fun getColorId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "color")
    }

    fun getDimenId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "dimen")
    }

    fun getAttrId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "attr")
    }

    fun getStyleId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "style")
    }

    fun getAnimId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "anim")
    }

    fun getArrayId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "array")
    }

    fun getIntegerId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "integer")
    }

    fun getBoolId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "bool")
    }

    private fun getIdentifierByType(context: Context, resourceName: String, defType: String): Int {
        return context.resources.getIdentifier(resourceName, defType, context.packageName)
    }
}