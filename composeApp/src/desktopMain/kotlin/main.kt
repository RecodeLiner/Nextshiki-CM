import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.nextshiki.App
import com.rcl.nextshiki.base.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)
    LifecycleController(lifecycle, windowState)
    Window(
        title = BuildConfig.USER_AGENT,
        state = windowState,
        onCloseRequest = ::exitApplication,
    ) {
        val root = remember {
            RootComponent(DefaultComponentContext(lifecycle))
        }
        App(root)
    }
}