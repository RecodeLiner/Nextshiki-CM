
import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.rcl.nextshiki.base.main.text


@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)

    LifecycleController(lifecycle, windowState)
    Window(
        title = BuildConfig.USER_AGENT,
        undecorated = true,
        state = windowState,
        onCloseRequest = ::exitApplication,
    ) {
        val root = remember {
            RootComponent(DefaultComponentContext(lifecycle))
        }
        val data = getRGBA(getSystemAccent())
        App(root, Color(data[0], data[1], data[2], data[3]))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Spacer(Modifier)

            Row(Modifier.padding(top = 6.dp, end = 4.dp)) {
                /*IconButton(onClick = { window.isMinimized = true }) {
                    Icon(Icons.Default.Minimize, contentDescription = null)
                }*/

                IconButton(onClick = { text = getSystemAccent()}) {
                    Icon(Icons.Default.Minimize, contentDescription = null)
                }

                IconButton(onClick = ::exitApplication) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}