package com.sd.lib.media_store.uri

import android.content.Context
import androidx.core.content.FileProvider

class FMediaFileProvider : FileProvider() {
    companion object {
        @JvmStatic
        fun getAuthority(context: Context): String {
            return "${context.packageName}.${FMediaFileProvider::class.java.simpleName.toLowerCase()}"
        }
    }
}