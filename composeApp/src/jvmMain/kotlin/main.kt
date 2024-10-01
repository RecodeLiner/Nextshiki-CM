
import Nextshiki.composeApp.BuildConfig.USER_AGENT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.lizowzskiy.accents.getAccentColor
import com.rcl.nextshiki.App
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.elements.ClipboardImpl
import com.rcl.nextshiki.elements.setupKoin
import com.rcl.nextshiki.setupNapier
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import java.awt.Toolkit


fun main() = application {
    setupNapier()
    startKoin {
        modules(
            *setupKoin(clipboard = ClipboardImpl(Toolkit.getDefaultToolkit().systemClipboard))
        )
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

                            ActionBar(
                                minimizeBlock = { window.isMinimized = it },
                                windowStatement = {
                                    windowState.placement =
                                        if (windowState.placement == WindowPlacement.Maximized)
                                            WindowPlacement.Floating else WindowPlacement.Maximized
                                },
                                exitApplication = ::exitApplication,
                                isMaximized = windowState.placement == WindowPlacement.Maximized
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ActionBar(
    minimizeBlock: (Boolean) -> Unit,
    windowStatement: () -> Unit,
    exitApplication: () -> Unit,
    isMaximized: Boolean
) {
    Row(Modifier.padding(top = 6.dp, end = 4.dp)) {
        IconButton(onClick = { minimizeBlock(true) }) {
            Icon(
                Icons.Default.Minimize,
                contentDescription = "Minimize app"
            )
        }

        IconButton(onClick = {
            windowStatement()
        }) {
            Icon(
                imageVector = if (isMaximized) {
                    Icons.Default.ArrowDropDown
                } else {
                    Icons.Default.ArrowDropUp
                }, contentDescription = "Maximize or floating app"
            )
        }

        IconButton(onClick = exitApplication) {
            Icon(Icons.Default.Close, contentDescription = "Close app")
        }
    }
}
