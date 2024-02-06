import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.nextshiki.App
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import org.koin.core.context.startKoin
import java.awt.Rectangle
import java.awt.Shape


@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    startKoin {
        modules(networkModule)
    }
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)

    LifecycleController(lifecycle, windowState)
    Window(
        title = BuildConfig.USER_AGENT,
        state = windowState,
        onCloseRequest = ::exitApplication,
    ) {
        val density = LocalDensity.current.density
        val spotInfoState = mutableStateMapOf<Any, Pair<Rectangle, Int>>()
        val spotsWithInfo = spotInfoState.toMap()
        val spots: Map<Shape, Int> = spotsWithInfo.values.associate { (rect, spot) ->
            Rectangle(rect.x, rect.y, rect.width, rect.height) to spot
        }

        CustomWindowDecorationAccessing.setCustomDecorationEnabled(window, true)
        CustomWindowDecorationAccessing.setCustomDecorationTitleBarHeight(window, (64 * density).toInt())
        CustomWindowDecorationAccessing.setCustomDecorationHitTestSpotsMethod(window, spots)

        val root = remember {
            RootComponent(DefaultComponentContext(lifecycle))
        }
        val data = getRGBA(getSystemAccent())
        WindowDraggableArea {
            App(rootComponent = root, seedColor = Color(data[0], data[1], data[2], data[3])) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Spacer(Modifier)

                    Row(Modifier.padding(top = 6.dp, end = 4.dp)) {
                        IconButton(onClick = { window.isMinimized = true }) {
                            Icon(Icons.Default.Minimize, contentDescription = "Minimize app")
                        }

                        IconButton(onClick = {
                            windowState.placement = if (windowState.placement == WindowPlacement.Maximized)
                                WindowPlacement.Floating else WindowPlacement.Maximized
                        }) {
                            Icon(
                                imageVector = if (windowState.placement == WindowPlacement.Maximized) {
                                    Icons.Default.ArrowDropDown
                                } else {
                                    Icons.Default.ArrowDropUp
                                }, contentDescription = "Maximize or floating app"
                            )
                        }

                        IconButton(onClick = ::exitApplication) {
                            Icon(Icons.Default.Close, contentDescription = "Close app")
                        }
                    }
                }
            }
        }
    }
}