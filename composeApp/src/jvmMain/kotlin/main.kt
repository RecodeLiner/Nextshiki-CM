import Nextshiki.composeApp.BuildConfig.USER_AGENT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.lizowzskiy.accents.getAccentColor
import com.rcl.nextshiki.App
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import com.rcl.nextshiki.di.settings.SettingsModule.settingsModule
import com.rcl.nextshiki.setupNapier
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin


@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    setupNapier()
    startKoin {
        modules(networkModule, settingsModule)
    }
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)

    val systemDesktopColor = try {
        getAccentColor()
    } catch (e: Exception) {
        Napier.i(
            tag = "DesktopAccentColor",
            message = "not available, exception - ${e.fillInStackTrace()}"
        )
        null
    }

    val composeColor = systemDesktopColor?.let {
        Color(red = it.r.toInt(), green = it.g.toInt(), blue = it.b.toInt())
    } ?: Color.Red


    LifecycleController(lifecycle, windowState)
    Window(
        transparent = true,
        undecorated = true,
        title = USER_AGENT,
        state = windowState,
        onCloseRequest = ::exitApplication,
    ) {
        val root = remember {
            RootComponent(DefaultComponentContext(lifecycle))
        }

        Box(modifier = Modifier.clip(RoundedCornerShape(15.dp))) {
            App(rootComponent = root, seedColor = composeColor,
                topAppBar = {
                    WindowDraggableArea {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Spacer(Modifier)

                            Row(Modifier.padding(top = 6.dp, end = 4.dp)) {
                                IconButton(onClick = { window.isMinimized = true }) {
                                    Icon(
                                        Icons.Default.Minimize,
                                        contentDescription = "Minimize app"
                                    )
                                }

                                IconButton(onClick = {
                                    windowState.placement =
                                        if (windowState.placement == WindowPlacement.Maximized)
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
            )
        }
    }
}