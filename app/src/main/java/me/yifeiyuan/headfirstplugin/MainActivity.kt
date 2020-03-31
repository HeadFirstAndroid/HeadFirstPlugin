package me.yifeiyuan.headfirstplugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.PathClassLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadPlugin()
    }


    val path = ""

    private fun loadPlugin() {

        val baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader")
        val dexPathListField = baseDexClassLoaderClass.getDeclaredField("pathList")

        val dexPathListClass = Class.forName("dalvik.system.DexPathList")
        val dexElementsFiled = dexPathListClass.getField("dexElements")

        val hostDexPathList = dexPathListField.get(classLoader)
        val hostDexElements : Array<Any> = dexElementsFiled.get(hostDexPathList) as Array<Any>

        val pluginPathClassLoader = PathClassLoader(path, cacheDir.absolutePath, null)
        val pluginDexPathList = dexPathListField.get(pluginPathClassLoader)
        val pluginDexElements: Array<Any> = dexElementsFiled.get(pluginDexPathList) as Array<Any>

        
    }
}