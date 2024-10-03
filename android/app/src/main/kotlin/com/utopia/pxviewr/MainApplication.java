package com.utopia.pxviewr

import android.app.Application
import android.content.Context
import android.util.Log
import com.squareup.leakcanary.LeakCanary
// import com.psykar.cookiemanager.CookieManagerPackage;
import com.facebook.react.PackageList
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.bridge.JavaScriptExecutorFactory
import com.facebook.react.ReactApplication
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader
import com.utopia.pxviewr.UgoiraView.UgoiraViewPackage
import java.lang.reflect.InvocationTargetException

class MainApplication : Application(), ReactApplication {

    private val mReactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            @Suppress("UnnecessaryLocalVariable")
            val packages: List<ReactPackage> = PackageList(this).packages
            // Packages that cannot be autolinked yet can be added manually here, for example:
            // packages.add(new MyReactNativePackage());
            // packages.add(new RNFetchBlobPackage());
            // packages.add(new CameraRollPackage());
            packages.add(UgoiraViewPackage())
            return packages
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, /* native exopackage */ false)
        initializeFlipper(this, reactNativeHost.reactInstanceManager)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    /**
     * Loads Flipper in React Native templates. Call this in the onCreate method with something like
     * initializeFlipper(this, reactNativeHost.reactInstanceManager);
     *
     * @param context
     * @param reactInstanceManager
     */
    private fun initializeFlipper(context: Context, reactInstanceManager: ReactInstanceManager) {
        if (BuildConfig.DEBUG) {
            try {
                /*
                 We use reflection here to pick up the class that initializes Flipper,
                since Flipper library is not available in release mode
                */
                val aClass = Class.forName("com.utopia.pxviewr.ReactNativeFlipper")
                aClass.getMethod("initializeFlipper", Context::class.java, ReactInstanceManager::class.java)
                    .invoke(null, context, reactInstanceManager)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }
}
