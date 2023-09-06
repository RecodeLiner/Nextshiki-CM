import androidx.compose.ui.window.ComposeUIViewController
import com.rcl.nextshiki.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
