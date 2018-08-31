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

import android.net.Uri
import com.sky.xposed.common.util.ResourceUtil
import com.sky.xposed.weishi.BuildConfig

/**
 * Created by sky on 2018/8/31.
 */
object UriUtil {

    fun getResource(resId: Int): Uri {
        return ResourceUtil.resourceIdToUri(BuildConfig.APPLICATION_ID, resId)
    }
}