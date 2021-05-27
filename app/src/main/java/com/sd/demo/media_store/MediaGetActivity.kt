package com.sd.demo.media_store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.media_store.databinding.ActivityMediaGetBinding
import com.sd.lib.media_store.FMediaCamera
import com.sd.lib.media_store.ext.CaptureStrategyFactory
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine

class MediaGetActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MediaGetActivity::class.java.simpleName
    private val REQUEST_CODE = 1223

    private lateinit var _binding: ActivityMediaGetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediaGetBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        AndPermission.with(this@MediaGetActivity).runtime()
            .permission(Permission.Group.STORAGE)
            .onGranted {
            }.onDenied {
                finish()
            }.start()
    }

    override fun onClick(v: View?) {
        when (v) {
            _binding.btnDefault -> {
                clickDefault()
            }
            _binding.btnCamera -> {
                clickCamera()
            }
        }
    }

    private fun clickDefault() {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .capture(true)
            .captureStrategy(CaptureStrategyFactory.defaultStrategy(this))
            .countable(true)
            .maxSelectable(1)
            .imageEngine(GlideEngine())
            .forResult(REQUEST_CODE)
    }

    private fun clickCamera() {
        FMediaCamera.getImage(this, object : FMediaCamera.Callback {
            override fun onResult(uri: Uri?) {
                Log.i(TAG, "camera result:${uri}")
            }

            override fun onCancel() {
                Log.i(TAG, "camera onCancel")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val listUri = Matisse.obtainResult(data)
            Log.i(TAG, "onActivityResult:${listUri}")
        }
    }
}