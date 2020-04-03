package me.yifeiyuan.plugin_app

import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

//ContentCatcher: Interceptor : NameNotFoundException: android.content.pm
//【Android Exception】android.content.pm.PackageManager$NameNotFoundException
class PluginActivityWithRes : AppCompatActivity() {

    companion object {
        const val TAG = "PluginWithRes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: 带资源的插件 Activity 被加载了")
        try {
            setContentView(R.layout.activity_plugin_with_res)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Toast.makeText(this, "带资源的插件 Activity", Toast.LENGTH_LONG).show()
    }

    override fun getResources(): Resources {
        try {
            val AssetManagerClass = Class.forName("android.content.res.AssetManager")
            val addAssetPathMethod =
                AssetManagerClass.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.isAccessible = true
            var pluginAssetManager = AssetManager::class.java.newInstance()

            addAssetPathMethod.invoke(pluginAssetManager, "/sdcard/plugin_app-debug.apk")

            var pluginResources = Resources(
                pluginAssetManager,
                super.getResources().displayMetrics,
                super.getResources().configuration
            )
            return pluginResources
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.getResources()
    }
}