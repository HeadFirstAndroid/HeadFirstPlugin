# HeadFirstPlugin

尝试学一下插件化。

outputs 目录下有个插件的 apk，可以用来被测试。

跑 Demo 步骤：
1. 将 outputs/plugin_app-debug.apk 放到手机的 sdcard 目录下；
2. 运行项目的 app 工程；
3. 授权 app 存储权限；
4. 点击首页按钮
5. 查看 logcat 日志，如果有"D/程序亦非猿: hello plugin，来自插件的方法" 输出就代表成功

核心代码：
```kotlin
        val path = "/sdcard/plugin_app-debug.apk"

        fun loadPlugin(context: Context) {

            val baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader")
            val dexPathListField = baseDexClassLoaderClass.getDeclaredField("pathList")
            dexPathListField.isAccessible = true

            val dexPathListClass = Class.forName("dalvik.system.DexPathList")
            val dexElementsFiled = dexPathListClass.getDeclaredField("dexElements")
            dexElementsFiled.isAccessible = true

            val hostDexPathList = dexPathListField.get(context.classLoader)
            val hostDexElements: Array<Any> = dexElementsFiled.get(hostDexPathList) as Array<Any>

            //加载插件
            val pluginPathClassLoader =
                PathClassLoader(path, context.cacheDir.absolutePath, context.classLoader)
            //获取插件的 ClassLoader 里的 dexPathList
            val pluginDexPathList = dexPathListField.get(pluginPathClassLoader)
            //获取插件的 dexPathList 里的 dexElements
            val pluginDexElements: Array<Any> =
                dexElementsFiled.get(pluginDexPathList) as Array<Any>

            //创建新的 DexElements[]，用来替换老的宿主 dexElements
            val newDexElements: Array<Any> = java.lang.reflect.Array.newInstance(
                hostDexElements.javaClass.componentType,
                hostDexElements.size + pluginDexElements.size
            ) as Array<Any>

            //将原来宿主的 dexElements 复制到新创建的 dexElements
            System.arraycopy(hostDexElements, 0, newDexElements, 0, hostDexElements.size)
            //将插件的 dexElements 复制到新创建的 dexElements
            System.arraycopy(
                pluginDexElements,
                0,
                newDexElements,
                hostDexElements.size,
                pluginDexElements.size
            )
            //将宿主的 DexPathList 里的 dexElements 替换为新的 dexElements，完成插件的类加载
            dexElementsFiled.set(hostDexPathList, newDexElements)
        }
    }
```