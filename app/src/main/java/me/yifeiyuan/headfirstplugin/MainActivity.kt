package me.yifeiyuan.headfirstplugin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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

    /**
     * 加载插件里的类
     */
    fun performLoadPlugin(view: View) {
        try {
            App.loadPlugin(this)
            val testClass = Class.forName("me.yifeiyuan.plugin_app.Test")
            testClass.getDeclaredMethod("helloPlugin").invoke(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    /**
     * 加载插件里不带资源的 Activity
     */
    fun performLoadPluginActivityWithoutRes(view: View) {

        //android.app.Instrumentation
    }


    /**
     * 加载插件里带资源的 Activity
     */
    fun performLoadPluginActivityWithRes(view: View) {

    }
}