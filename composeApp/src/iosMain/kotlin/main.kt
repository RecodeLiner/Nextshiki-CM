import androidx.compose.ui.window.ComposeUIViewController
import com.rcl.nextshiki.app
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { app() }
}
