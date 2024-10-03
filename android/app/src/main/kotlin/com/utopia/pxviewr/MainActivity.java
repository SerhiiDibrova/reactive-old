package com.utopia.pxviewr

import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.ReactRootView
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView
import org.devio.rn.splashscreen.SplashScreen

class MainActivity : ReactActivity() {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    override fun getMainComponentName(): String {
        return "PxViewR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // workaround for crashing on killed app restore
        // https://github.com/kmagiera/react-native-screens/issues/17#issuecomment-424704067
        // https://github.com/kmagiera/react-native-screens/issues/114
        super.onCreate(null)
        SplashScreen.show(this)
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return object : ReactActivityDelegate(this, mainComponentName) {
            override fun createRootView(): ReactRootView {
                return RNGestureHandlerEnabledRootView(this@MainActivity)
            }
        }
    }
}
