import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rcl.nextshiki.App

fun main() = application {
    Window(
        title = "Nextshiki",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        onCloseRequest = ::exitApplication,
    ) { App() }
}