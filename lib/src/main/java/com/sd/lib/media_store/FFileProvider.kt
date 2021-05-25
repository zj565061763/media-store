package com.sd.lib.media_store

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class FFileProvider : FileProvider() {
    companion object {
        /**
         * [File]转[Uri]
         */
        @JvmStatic
        fun getUri(file: File?, context: Context): Uri? {
            if (file == null) return null
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getUriForFile(context, getAuthority(context), file)
            } else {
                Uri.fromFile(file)
            }
        }

        @JvmStatic
        fun getAuthority(context: Context): String {
            return "${context.packageName}.${FFileProvider::class.java.simpleName.toLowerCase()}"
        }
    }
}