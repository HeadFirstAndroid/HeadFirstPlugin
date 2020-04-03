package me.yifeiyuan.plugin_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PluginActivityWithRes : AppCompatActivity() {

    companion object {
        const val TAG = "PluginWithRes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: 带资源的插件 Activity 被加载了")
        setContentView(R.layout.activity_plugin_with_res)
        Toast.makeText(this, "带资源的插件 Activity", Toast.LENGTH_LONG).show()
    }
}