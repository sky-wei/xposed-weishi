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

        const val PACKAGE_NAME = "com.tencent.weishi"
    }

    object Preference {

        /** 自动播放  */
        const val AUTO_PLAY = "auto_play"

        /** 自动关注  */
        const val AUTO_ATTENTION = "auto_attention"

        /** 自动点赞  */
        const val AUTO_LIKE = "auto_like"

        /** 自动评论  */
        const val AUTO_COMMENT = "auto_comment"

        /** 自动评论内容列表  */
        const val AUTO_COMMENT_LIST = "auto_comment_list"

        /** 自动保存视频  */
        const val AUTO_SAVE_VIDEO = "auto_save_video"

        /** 解除15s视频限制  */
        const val REMOVE_LIMIT = "remove_limit"

        /** 自动播放休眠时间 */
        const val AUTO_PLAY_SLEEP_TIME = "auto_play_sleep_time"

        /** 录制视频的最大时间 */
        const val RECORD_VIDEO_TIME = "record_video_time"

        /** 红包的最大时间  */
        const val HB_LAST_TIME = "hb_last_time"
    }

    object Action {

        const val REFRESH_PREFERENCE = BuildConfig.APPLICATION_ID + ".ACTION_REFRESH_PREFERENCE"
    }

    object Key {

        const val DATA = "data"
    }

    object Name {

        const val WEI_SHI = "weishi"

        const val PLUGIN = "微视助手"

        const val SAVE_VIDEO = "无水印保存"
    }

    object InputType {

        const val NUMBER = 0

        const val NUMBER_SIGNED = 1

        const val NUMBER_DECIMAL = 2

        const val TEXT = 3

        const val PHONE = 4

        const val TEXT_PASSWORD = 5

        const val NUMBER_PASSWORD = 6
    }

    object DefaultValue {

        const val AUTO_PLAY_SLEEP_TIME = 15   // 单位:秒

        const val RECORD_VIDEO_TIME = 120     // 单位:秒
    }

    object Bugly {

        const val APP_ID = "3443561b97"
    }

    object Time {

        /** 红包最大间隔时间 */
        const val HB_MAX_TIME = 1000 * 60 * 60 * 24
    }
}