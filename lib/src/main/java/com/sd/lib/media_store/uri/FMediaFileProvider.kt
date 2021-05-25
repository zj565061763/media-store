package com.sd.lib.media_store.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class FMediaFileProvider : FileProvider() {
    companion object {
        @JvmStatic
        fun getAuthority(context: Context): String {
            return "${context.packageName}.${FMediaFileProvider::class.java.simpleName.toLowerCase()}"
        }

        @JvmStatic
        fun fileToUri(file: File?, context: Context): Uri? {
            if (file == null) return null
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getUriForFile(context, getAuthority(context), file)
            } else {
                Uri.fromFile(file)
            }
        }
    }
}