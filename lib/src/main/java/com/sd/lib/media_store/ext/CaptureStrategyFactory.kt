package com.sd.lib.media_store.ext

import android.content.Context
import com.sd.lib.media_store.uri.FMediaFileProvider
import com.zhihu.matisse.internal.entity.CaptureStrategy

object CaptureStrategyFactory {
    @JvmStatic
    fun defaultPublic(context: Context): CaptureStrategy {
        return CaptureStrategy(true, FMediaFileProvider.getAuthority(context))
    }

    @JvmStatic
    fun defaultPrivate(context: Context): CaptureStrategy {
        return CaptureStrategy(false, FMediaFileProvider.getAuthority(context))
    }
}