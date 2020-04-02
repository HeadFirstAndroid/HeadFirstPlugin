package me.yifeiyuan.headfirstplugin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ProxyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proxy)
        Log.e("ProxyActivity", Log.getStackTraceString(Throwable()))
    }
}