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

object Constant {

    object WeiShi {

        val PACKAGE_NAME = "com.tencent.weishi"
    }

    object Preference {

        /** 自动播放  */
        val AUTO_PLAY = "auto_play"

        /** 自动关注  */
        val AUTO_ATTENTION = "auto_attention"

        /** 自动点赞  */
        val AUTO_LIKE = "auto_like"

        /** 自动评论  */
        val AUTO_COMMENT = "auto_comment"

        /** 自动评论内容  */
        val AUTO_COMMENT_MESSAGE = "auto_comment_message"

        /** 自动保存视频  */
        val AUTO_SAVE_VIDEO = "auto_save_video"

        /** 解除15s视频限制  */
        val REMOVE_LIMIT = "remove_limit"
    }

    object Action {

        val REFRESH_PREFERENCE = BuildConfig.APPLICATION_ID + ".ACTION_REFRESH_PREFERENCE"
    }

    object Key {

        val DATA = "data"
    }

    object Name {

        val WEI_SHI = "weishi"

        val PLUGIN = "微视助手"

        val SAVE_VIDEO = "无水印保存"
    }
}