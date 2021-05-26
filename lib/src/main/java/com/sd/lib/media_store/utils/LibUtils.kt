package com.sd.lib.media_store.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.InputStream

internal object LibUtils {
    /**
     * 将[inputStream]内容保存到[uri]
     */
    @JvmStatic
    fun saveToUri(uri: Uri, inputStream: InputStream?, resolver: ContentResolver): Uri? {
        try {
            resolver.openOutputStream(uri)?.use { output ->
                inputStream?.use { input ->
                    val copySize = input.copyTo(output)
                    if (copySize > 0) return uri
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            runCatching {
                resolver.delete(uri, null, null)
            }
        }
        return null
    }

    /**
     * 获取扩展名
     */
    @JvmStatic
    fun getExt(url: String?, defaultExt: String? = null): String {
        val defaultExtFormat = if (defaultExt == null || defaultExt.isEmpty()) {
            ""
        } else {
            if (defaultExt.startsWith(".")) defaultExt.substring(1) else defaultExt
        }

        if (url == null || url.isEmpty()) {
            return defaultExtFormat
        }

        var ext = MimeTypeMap.getFileExtensionFromUrl(url)
        if (ext == null || ext.isEmpty()) {
            val lastIndex = url.lastIndexOf(".")
            if (lastIndex >= 0) ext = url.substring(lastIndex + 1)
        }

        return if (ext == null || ext.isEmpty()) {
            defaultExtFormat
        } else {
            ext
        }
    }

    /**
     * 完整扩展名
     */
    @JvmStatic
    fun fullExt(ext: String?): String {
        return if (ext == null || ext.isEmpty()) {
            ""
        } else {
            if (ext.startsWith(".")) ext else ".$ext"
        }
    }
}