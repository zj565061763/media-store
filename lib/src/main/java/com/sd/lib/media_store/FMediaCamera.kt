package com.sd.lib.media_store

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.sd.lib.media_store.ext.MediaFragment

object FMediaCamera {
    private var _fragment: MediaFragment? = null
    private var _callback: Callback? = null

    /**
     * 设置回调对象
     */
    fun setCallback(callback: Callback) {
        _callback = callback
    }

    /**
     * 移除回调对象
     */
    fun removeCallback(callback: Callback) {
        if (_callback == callback) {
            _callback = null
        }
    }

    /**
     * 获取图片
     */
    fun getImage(activity: FragmentActivity) {
        if (activity.isFinishing) return
        if (_fragment != null) return
        startFragment(activity)
    }

    private fun startFragment(activity: FragmentActivity) {
        if (_fragment == null) {
            _fragment = MediaFragment().also {
                it.callback = object : MediaFragment.Callback {
                    override fun onResult(uri: Uri?) {
                        _fragment = null
                        if (uri != null) {
                            _callback?.onResult(uri)
                        }
                    }

                    override fun onDetach() {
                        _fragment = null
                    }
                }
                activity.supportFragmentManager.beginTransaction()
                    .add(it, null).commitNowAllowingStateLoss()
            }
        }
    }

    fun interface Callback {
        fun onResult(uri: Uri)
    }
}