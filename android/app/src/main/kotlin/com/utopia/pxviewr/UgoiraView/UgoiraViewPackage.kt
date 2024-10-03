package com.utopia.pxviewr.UgoiraView

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.Collections

class UgoiraViewPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Collections.emptyList()
    }

    @Suppress("DeprecatedCallableAddReplaceWith") 
    fun createJSModules(): List<Class<out JavaScriptModule>> {
        return Collections.emptyList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(UgoiraViewManager())
    }
}
