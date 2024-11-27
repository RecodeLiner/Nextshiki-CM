import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.nextshiki.App
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.setupNapier
import platform.UIKit.UIViewController

@OptIn(ExperimentalDecomposeApi::class)
fun mainViewController(): UIViewController {
    setupNapier()
    val context = DefaultComponentContext(LifecycleRegistry())
    val dispatcher = context.backHandler as BackDispatcher

    return ComposeUIViewController {
        PredictiveBackGestureOverlay(backDispatcher = dispatcher, backIcon = null) {
            App(rootComponent = RootComponent(context))
        }
    }
}
