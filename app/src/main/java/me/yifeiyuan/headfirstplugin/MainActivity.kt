package me.yifeiyuan.headfirstplugin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            Toast.makeText(this, "加载插件的类成功", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    /**
     * 加载插件里不带资源的 Activity
     */
    fun performLoadPluginActivityWithoutRes(view: View) {
        val intent = Intent()
        intent.setClassName(
            "me.yifeiyuan.plugin_app",
            "me.yifeiyuan.plugin_app.PluginActivityWithoutRes"
        )
        intent.putExtra("isPlugin",true)
        startActivity(intent)
    }


    /**
     * 加载插件里带资源的 Activity
     */
    fun performLoadPluginActivityWithRes(view: View) {
        val intent = Intent()
        intent.setClassName(
            "me.yifeiyuan.plugin_app",
            "me.yifeiyuan.plugin_app.PluginActivityWithRes"
        )
        intent.putExtra("isPlugin",true)
        startActivity(intent)
    }

    fun loadHostProxyActivity(view: View) {
        startActivity(Intent(this,ProxyActivity::class.java))
    }
}