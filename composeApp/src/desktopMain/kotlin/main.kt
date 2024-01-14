
import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry.REG_TYPE
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
        val data = getRGBA(readRegistry("HKCU\\Software\\Microsoft\\Windows\\DWM", "AccentColor"))
        App(root, Color(data[0], data[1], data[2], data[3]))
    }
}

fun readRegistry(location: String, key: String): String {
    return WindowsRegistry.getWindowsRegistryEntry(location, key, REG_TYPE.REG_DWORD)
}
fun getRGBA(rgb: String): IntArray {
    val r = rgb.substring(2, 4).toInt(16)
    val g = rgb.substring(4, 6).toInt(16)
    val b = rgb.substring(6, 8).toInt(16)
    val a = rgb.substring(8, 10).toInt(16)
    return intArrayOf(r, g, b, a)
}