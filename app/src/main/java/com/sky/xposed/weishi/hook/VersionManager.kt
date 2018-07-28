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

import android.content.Context
import com.sky.xposed.weishi.hook.support.WeiShiHook
import com.sky.xposed.weishi.hook.support.WeiShiHook43088
import com.sky.xposed.weishi.hook.support.WeiShiHook44188
import com.sky.xposed.weishi.util.Alog
import com.sky.xposed.weishi.util.PackageUitl

/**
 * Created by sky on 18-6-2.
 */
class VersionManager(hookManager: HookManager) {

    private val CONFIG_MAP = HashMap<String, Class<out Config>>()
    private val HOOK_MAP = HashMap<String, Class<out WeiShiHook>>()

    private var mContext: Context = hookManager.getContext()
    private var mVersionConfig: VersionManager.Config? = null

    init {
        /** 版本配置 */
        CONFIG_MAP["4.2.0.88"] = Config42088::class.java
        CONFIG_MAP["4.2.5.88"] = Config42588::class.java
        CONFIG_MAP["4.3.0.88"] = Config43088::class.java
        CONFIG_MAP["4.3.2.88"] = Config43288::class.java
        CONFIG_MAP["4.4.1.88"] = Config44188::class.java
        CONFIG_MAP["4.5.0.588"] = Config450588::class.java

        /** Hook */
        HOOK_MAP["4.3.0.88"] = WeiShiHook43088::class.java
        HOOK_MAP["4.3.2.88"] = WeiShiHook43088::class.java
        HOOK_MAP["4.4.1.88"] = WeiShiHook44188::class.java
        HOOK_MAP["4.5.0.588"] = WeiShiHook44188::class.java
    }

    fun isSupportVersion(): Boolean {

        val info = getPackageInfo() ?: return false

        return CONFIG_MAP.containsKey(info.versionName)
    }

    fun getSupportConfig(): Config? {

        val info = getPackageInfo() ?: return null

        return getSupportConfig(CONFIG_MAP[info.versionName])
    }

    private fun getSupportConfig(vClass: Class<out Config>?): Config? {

        if (vClass == null) return null

        if (mVersionConfig == null) {
            try {
                // 创建实例
                mVersionConfig = vClass.newInstance()
            } catch (tr: Throwable) {
                Alog.d("创建版本配置异常", tr)
            }

        }
        return mVersionConfig
    }

    fun getSupportWeiShiHook(): WeiShiHook {

        val info = getPackageInfo() ?: return WeiShiHook()

        if (info == null
                || !HOOK_MAP.containsKey(info.versionName)) {
            // 返回默认的
            return WeiShiHook()
        }

        // 获取支持的版本
        return getSupportWeiShiHook(HOOK_MAP[info.versionName])
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

    fun getPackageInfo(): PackageUitl.SimplePackageInfo? {
        return PackageUitl.getSimplePackageInfo(mContext, mContext.packageName)
    }

    fun getPackageInfo(context: Context): PackageUitl.SimplePackageInfo? {
        return PackageUitl.getSimplePackageInfo(context, context.packageName)
    }

    class Config450588 : Config() {

        init {
            classShareDialog = "com.tencent.oscar.module.share.a.b"
            classShareType = "com.tencent.oscar.module.share.ShareConstants\$ShareOptionsId"

            classFeedList = "com.tencent.oscar.module.feedlist.d.ap"
            classMainFeed = "com.tencent.oscar.module.main.feed.h"

            classItemModel = "com.tencent.oscar.module.main.feed.bi"
            classSendComment = "com.tencent.oscar.module.f.a.c"

            fieldItemModeList = "l"
            fieldItemModeList2 = "j"
        }
    }

    class Config44188 : Config() {

        init {
            classShareDialog = "com.tencent.oscar.module.share.b.b"

            classFeedList = "com.tencent.oscar.module.feedlist.d.am"
            classMainFeed = "com.tencent.oscar.module.main.feed.h"

            classItemModel = "com.tencent.oscar.module.main.feed.ay"
            classSendComment = "com.tencent.oscar.module.f.a.c"

            fieldItemModeList = "l"
            fieldItemModeList2 = "i"
        }
    }

    class Config43288 : Config() {

        init {
            classShareDialog = "com.tencent.oscar.module.share.b.b"

            classFeedList = "com.tencent.oscar.module.feedlist.d.al"

            classItemModel = "com.tencent.oscar.module.main.feed.av"
        }
    }

    class Config43088 : Config() {

        init {
            classShareDialog = "com.tencent.oscar.module.share.b.b"

            classFeedList = "com.tencent.oscar.module.feedlist.d.al"

            classItemModel = "com.tencent.oscar.module.main.feed.av"
        }
    }

    class Config42588 : Config() {

        init {
            classFeedList = "com.tencent.oscar.module.feedlist.d.ag"
        }
    }

    class Config42088 : Config()

    /**
     * 类与字段名相关配置字段信息
     */
    open class Config {

        /** ShareDialog   */
        var classShareDialog = "com.tencent.oscar.module.share.a.b"

        var classAppCompatTextView = "android.support.v7.widget.AppCompatTextView"

        /** "RecommendPageFragment", "onLastItemVisible" */
        var classFeedList = "com.tencent.oscar.module.feedlist.c.af"

        /** "FeedFragment */
        var classMainFeed = "com.tencent.oscar.module.main.feed.f"

        var classRecyclerViewPager = "com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager"

        var classWeishiParams = "com.tencent.oscar.config.WeishiParams"

        var classItemModel = "com.tencent.oscar.module.main.feed.as"

        var classLifePlayApplication = "com.tencent.oscar.app.LifePlayApplication"

        var classStMetaPerson = "NS_KING_SOCIALIZE_META.stMetaPerson"

        var classStMetaComment = "NS_KING_SOCIALIZE_META.stMetaComment"

        /**  "source" "shieldid" */
        var classSendComment = "com.tencent.oscar.module.d.a.c"

        var classShareType = "com.tencent.oscar.module.share.c.d"

        var methodShareCreateItem = "a"

        var methodShareClick = "a"

        var methodShareDismiss = "dismiss"

        var methodFeedListOnResume = "onResume"

        var methodFeedListOnPause = "onPause"

        var methodMainFeedOnResume = "onResume"

        var methodMainFeedOnPause = "onPause"

        var methodViewPagerSmooth = "smoothScrollToPosition"

        var methodParamsLimit = "getUserVideoDurationLimit"

        var methodViewPagerAdapterPosition = "findViewHolderForAdapterPosition"

        var methodViewPagerGetAdapter = "getAdapter"

        var methodViewPagerGetCurrentPosition = "getCurrentPosition"

        var methodSendComment = "a"

        var methodGetAccountManager = "getAccountManager"

        var methodAccountPosterId = "b"

        var fieldShareActivity = "a"

        var fieldFeedListViewPager = "a"

        var fieldMainFeedViewPager = "a"

        var fieldViewHolder = "itemView"

        var fieldItemModeList = "j"

        var fieldItemModeList2 = "h"

        var fieldItemModeVideoUrl = "video_url"

        var fieldDataId = "id"

        var fieldDataPoster = "poster"

        var fieldDataTopicId = "topic_id"

        var fieldDataShieldId = "shieldId"

        var idAttention = "follow_flag"

        var idLikeContainer = "feed_like_status_container"

        var idLikeStatus2 = "feed_like_status2"
    }
}