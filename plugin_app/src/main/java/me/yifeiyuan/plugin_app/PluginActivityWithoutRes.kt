package me.yifeiyuan.plugin_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PluginActivityWithoutRes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "不带资源的插件 Activity", Toast.LENGTH_LONG).show()

    }
}