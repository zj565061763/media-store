package com.sd.lib.media_store

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.sd.lib.media_store.ext.MediaFragment

object FMediaCamera {
    /**
     * 获取图片
     */
    @JvmStatic
    fun getImage(activity: FragmentActivity, callback: Callback) {
        MediaFragment.attach(activity, callback)
    }

    fun interface Callback {
        fun onResult(uri: Uri?)
    }
}