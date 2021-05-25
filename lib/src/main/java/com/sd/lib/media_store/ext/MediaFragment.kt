package com.sd.lib.media_store.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import com.zhihu.matisse.internal.utils.MediaStoreCompat
import com.zhihu.matisse.internal.utils.SingleMediaScanner

class MediaFragment : Fragment() {
    private val REQUEST_CODE_CAMERA = 413

    private var _mediaStore: MediaStoreCompat? = null
        private set

    var callback: Callback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _mediaStore = MediaStoreCompat(context as Activity, this).also {
            it.setCaptureStrategy(CaptureStrategyFactory.defaultStrategy(context))
            it.dispatchCaptureIntent(context, REQUEST_CODE_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA) {
            val fragmentActivity = requireActivity()
            if (resultCode == Activity.RESULT_OK) {
                val compat = _mediaStore!!
                val fileUri: Uri = compat.currentPhotoUri
                val filePath: String = compat.currentPhotoPath

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    fragmentActivity.revokeUriPermission(
                        fileUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                SingleMediaScanner(fragmentActivity.applicationContext, filePath) { Log.i("MediaFragment", "scan finish!") }
                callback?.onResult(fileUri)
            } else {
                callback?.onResult(null)
            }

            fragmentActivity.supportFragmentManager.beginTransaction()
                .remove(this).commitNowAllowingStateLoss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback?.onDetach()
    }

    interface Callback {
        fun onResult(uri: Uri?)

        fun onDetach()
    }
}