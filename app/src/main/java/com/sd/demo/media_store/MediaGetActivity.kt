package com.sd.demo.media_store

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.media_store.databinding.ActivityMediaGetBinding
import com.sd.lib.media_store.FMediaCamera
import com.sd.lib.media_store.ext.CaptureStrategyFactory
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.sunday.callback.OnCaptureClickCallback
import com.zhihu.matisse.sunday.callback.OnResultCallback

class MediaGetActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MediaGetActivity::class.java.simpleName

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
                AndPermission.with(this@MediaGetActivity).runtime()
                    .permission(Permission.CAMERA)
                    .onGranted {
                        clickCamera()
                    }.onDenied {
                        Toast.makeText(this@MediaGetActivity, "permission onDenied", Toast.LENGTH_SHORT).show()
                    }.start()
            }
        }
    }

    private fun clickDefault() {
        val onCaptureClickCallback = OnCaptureClickCallback { task ->
            AndPermission.with(this@MediaGetActivity).runtime()
                .permission(Permission.CAMERA)
                .onGranted {
                    task.run()
                }.onDenied {
                    Toast.makeText(this@MediaGetActivity, "permission onDenied", Toast.LENGTH_SHORT).show()
                }.start()
        }

        Matisse.from(this)
            .choose(MimeType.ofImage())
            .capture(true)
            .captureStrategy(CaptureStrategyFactory.defaultStrategy(this))
            .countable(true)
            .maxSelectable(1)
            .imageEngine(GlideEngine())
            .setOnCaptureClickCallback(onCaptureClickCallback)
            .forResult(object : OnResultCallback {
                override fun onResult(list: MutableList<Uri>) {
                    Log.i(TAG, "OnResultCallback onResult:${list}")
                }

                override fun onCancel() {
                    Log.i(TAG, "OnResultCallback onCancel")
                }
            })
    }

    private fun clickCamera() {
        FMediaCamera.getImage(this, object : FMediaCamera.Callback {
            override fun onResult(uri: Uri?) {
                Log.i(TAG, "camera onResult:${uri}")
            }

            override fun onCancel() {
                Log.i(TAG, "camera onCancel")
            }
        })
    }
}