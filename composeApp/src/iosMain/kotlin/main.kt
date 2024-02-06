import androidx.compose.ui.window.ComposeUIViewController
import com.rcl.nextshiki.app
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin { modules(networkModule) }
    return ComposeUIViewController { app() }
}
