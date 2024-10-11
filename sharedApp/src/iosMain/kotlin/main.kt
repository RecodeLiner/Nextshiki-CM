import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.nextshiki.App
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.clipboard.ClipboardModule
import com.rcl.nextshiki.di.language.LanguageModule
import com.rcl.nextshiki.di.settings.SettingsModuleObject
import com.rcl.nextshiki.setupNapier
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

@OptIn(ExperimentalDecomposeApi::class)
fun mainViewController(): UIViewController {
    setupNapier()
    val context = DefaultComponentContext(LifecycleRegistry())
    val dispatcher = context.backHandler as BackDispatcher

    startKoin {
        modules(
            ClipboardModule.clipboardModule,
            LanguageModule.langModule,
            RepositoryModule.networkModule,
            SettingsModuleObject.settingsModule
        )
    }
    return ComposeUIViewController {
        PredictiveBackGestureOverlay(backDispatcher = dispatcher, backIcon = null) {
            App(rootComponent = RootComponent(context))
        }
    }
}
