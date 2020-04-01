package me.yifeiyuan.headfirstplugin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.PathClassLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            Class.forName("me.yifeiyuan.plugin_app.Test")
        } catch (ex: Exception) {
            ex.printStackTrace()//java.lang.ClassNotFoundException: me.yifeiyuan.plugin_app.Test
        }
    }

    val path = "/sdcard/plugin_app-debug.apk"

    //6.0.1
    private fun loadPlugin() {

        val baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader")
        val dexPathListField = baseDexClassLoaderClass.getDeclaredField("pathList")
        dexPathListField.isAccessible = true

        val dexPathListClass = Class.forName("dalvik.system.DexPathList")
        val dexElementsFiled = dexPathListClass.getDeclaredField("dexElements")
        dexElementsFiled.isAccessible = true

        val hostDexPathList = dexPathListField.get(classLoader)
        val hostDexElements: Array<Any> = dexElementsFiled.get(hostDexPathList) as Array<Any>

        //加载插件
        val pluginPathClassLoader = PathClassLoader(path, cacheDir.absolutePath, classLoader)
        //获取插件的 ClassLoader 里的 dexPathList
        val pluginDexPathList = dexPathListField.get(pluginPathClassLoader)
        //获取插件的 dexPathList 里的 dexElements
        val pluginDexElements: Array<Any> = dexElementsFiled.get(pluginDexPathList) as Array<Any>

        //创建新的 DexElements[]，用来替换老的宿主 dexElements
        val newDexElements: Array<Any> = java.lang.reflect.Array.newInstance(hostDexElements.javaClass.componentType, hostDexElements.size + pluginDexElements.size) as Array<Any>

        //将原来宿主的 dexElements 复制到新创建的 dexElements
        System.arraycopy(hostDexElements, 0, newDexElements, 0, hostDexElements.size)
        //将插件的 dexElements 复制到新创建的 dexElements
        System.arraycopy(pluginDexElements, 0, newDexElements, hostDexElements.size, pluginDexElements.size)

        //将宿主的 DexPathList 里的 dexElements 替换为新的 dexElements，完成插件的类加载
        dexElementsFiled.set(hostDexPathList, newDexElements)
    }

    fun performLoadPlugin(view: View) {
        try {
            App.loadPlugin(this)
            val testClass = Class.forName("me.yifeiyuan.plugin_app.Test")
            testClass.getDeclaredMethod("helloPlugin").invoke(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}