package me.yifeiyuan.plugin_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PluginActivityWithoutRes : AppCompatActivity() {

    companion object {
        const val TAG = "PluginWithoutRes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: 不带资源的插件 Activity 被加载了")
        Toast.makeText(this, "不带资源的插件 Activity 被加载", Toast.LENGTH_LONG).show()
    }
}