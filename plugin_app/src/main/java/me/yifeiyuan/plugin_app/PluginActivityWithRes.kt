package me.yifeiyuan.plugin_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PluginActivityWithRes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_with_res)
        Toast.makeText(this,"带资源的插件 Activity",Toast.LENGTH_LONG).show()
    }
}