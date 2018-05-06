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

package com.sky.xposed.weishi.util

import java.util.*

object RandomUtil {

    private val RANDOM = Random(System.currentTimeMillis())

    /**
     * 去数组随机下标
     * @param size > 0
     * @return
     */
    fun randomIndex(size: Int): Int {
        return RANDOM.nextInt(size)
    }

    fun random(start: Int, end: Int): Int {
        return random(end - start) + start
    }

    fun randomLong(start: Int, end: Int): Long {
        return random(start, end).toLong()
    }

    fun random(value: Int): Int {
        return RANDOM.nextInt(value)
    }
}