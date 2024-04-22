import androidx.compose.ui.window.ComposeUIViewController
import com.rcl.nextshiki.app
import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import com.rcl.nextshiki.di.settings.SettingsModule.settingsModule
import com.rcl.nextshiki.setupNapier
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    setupNapier()
    startKoin { modules(networkModule,settingsModule) }
    return ComposeUIViewController { app() }
}
