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