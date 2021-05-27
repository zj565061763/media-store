package com.sd.lib.media_store

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.sd.lib.media_store.ext.CaptureStrategyFactory
import com.zhihu.matisse.sunday.callback.OnResultCallback
import com.zhihu.matisse.sunday.fragment.CameraResultFragment

object FMediaCamera {
    /**
     * 获取图片
     */
    @JvmStatic
    fun getImage(activity: FragmentActivity, callback: Callback) {
        val captureStrategy = CaptureStrategyFactory.defaultStrategy(activity);
        CameraResultFragment.start(activity, captureStrategy, object : OnResultCallback {
            override fun onResult(list: MutableList<Uri>) {
                val uri = list.firstOrNull()
                callback.onResult(uri)
            }

            override fun onCancel() {
                callback.onCancel()
            }
        })
    }

    interface Callback {
        fun onResult(uri: Uri?)

        fun onCancel()
    }
}