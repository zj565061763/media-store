package com.sd.lib.media_store

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.sd.lib.media_store.utils.LibUtils
import java.io.File
import java.util.*

object FMediaImage {
    private const val DEFAULT_EXT = "jpg"
    private const val DEFAULT_MIME_TYPE = "image/jpeg"

    /**
     * 保存图片文件
     */
    @JvmStatic
    fun saveFile(file: File?, context: Context): Uri? {
        if (file == null || !file.exists()) return null

        val ext = LibUtils.getExt(file.absolutePath, DEFAULT_EXT)
        val contentValues = createContentValues(ext)

        val resolver = context.contentResolver
        val uri: Uri = try {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null

        return LibUtils.saveToUri(uri, file.inputStream(), resolver)
    }

    @JvmStatic
    private fun createContentValues(ext: String): ContentValues {
        val uuid = UUID.randomUUID().toString()
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: DEFAULT_MIME_TYPE
        val fullExt = LibUtils.fullExt(ext)

        return ContentValues().apply {
            this.put(MediaStore.Images.ImageColumns.TITLE, uuid)
            this.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, uuid + fullExt)
            this.put(MediaStore.Images.ImageColumns.DESCRIPTION, uuid)
            this.put(MediaStore.Images.ImageColumns.MIME_TYPE, mimeType)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
    }
}