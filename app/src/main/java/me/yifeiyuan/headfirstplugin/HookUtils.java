package me.yifeiyuan.headfirstplugin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 程序亦非猿 on 2020/4/1.
 */
public class HookUtils {

    private static final String TAG = "HookUtils";

    public static void hookAMS(Context context) {

        try {

            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Method getDefaultMethod = activityManagerNativeClass.getDeclaredMethod("getDefault");
            final Object IActivityManager = getDefaultMethod.invoke(null);

            final Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");

            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);

            Object gDefault = gDefaultField.get(IActivityManager);

            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = SingletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            //mInstance --> mInstance 的 Field --> Singleton 对象 --》 IActivityManagerSingleton 静态

            //获取 IActivityManager
            //1. 获取 ActivityManager
//            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
//            Field IActivityManagerSingletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
//            IActivityManagerSingletonField.setAccessible(true);
//
//            Object IActivityManagerSingleton = IActivityManagerSingletonField.get(null);
//
//            Class<?> SingletonClass = Class.forName("android.util.Singleton");
//
//            Field mInstanceField = SingletonClass.getDeclaredField("mInstance");
//            mInstanceField.setAccessible(true);

//            final Object mInstance_iActivityManager = mInstanceField.get(IActivityManagerSingleton);

            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                    Log.d(TAG, "invoke() called with: proxy = [" + proxy + "], method = [" + method + "], args = [" + args + "]");
                    Log.d(TAG, "invoke: ");
                    //过滤一下 startActivity
                    if ("startActivity".equals(method.getName())) {

                        int index = 0;
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Intent) {
                                index = i;
                                break;
                            }
                        }
                        //这个 Intent 是插件 Activity 的 Intent
                        Intent intent = (Intent) args[index];

                        if (intent.getBooleanExtra("isPlugin", false)) {
                            Intent proxyIntent = new Intent();
                            proxyIntent.setClassName("me.yifeiyuan.headfirstplugin", "me.yifeiyuan.headfirstplugin.ProxyActivity");
                            proxyIntent.putExtra("origin", intent);
                            args[index] = proxyIntent;
                        }
                    }
                    return method.invoke(IActivityManager, args);
                }
            });

            Log.d(TAG, "hookAMS: ");
            mInstanceField.set(gDefault, proxyInstance);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void hookActivityThreadH() {
        //创建 Callback
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Log.d(TAG, "handleMessage() called with: message = [" + msg + "]");

                //LAUNCH_ACTIVITY=100
                if (msg.what != 100) {
                    return false;
                }
                //D/HookUtils: handleMessage() called with: message = [{ when=0 what=149 obj=android.os.BinderProxy@24b4d70 target=android.app.ActivityThread$H planTime=1585840590698 dispatchTime=0 finishTime=0 }]

                //final ActivityClientRecord r = (ActivityClientRecord)msg.obj
                //替换 Intent
                try {
                    //获取到代理的 Intent
                    Field intentField = msg.obj.getClass().getDeclaredField("intent");
                    intentField.setAccessible(true);

                    Intent proxyIntent = (Intent) intentField.get(msg.obj);

                    Intent pluginIntent = proxyIntent.getParcelableExtra("origin");

                    if (pluginIntent != null) {
                        Log.d(TAG, "handleMessage: 发现插件 Intent");
                        intentField.set(msg.obj, pluginIntent);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };

        //拿到 ActivityThread.mH
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");

            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object activityThread = sCurrentActivityThreadField.get(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);

            Object mH = mHField.get(activityThread);

            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);

            callbackField.set(mH, callback);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
