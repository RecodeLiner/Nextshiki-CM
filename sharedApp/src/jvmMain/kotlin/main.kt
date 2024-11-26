import Nextshiki.resources.BuildConfig.APP_NAME
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.lizowzskiy.accents.getAccentColor
import com.rcl.nextshiki.SharedRes
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.compose.App
import com.rcl.nextshiki.compose.setupNapier
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.clipboard.ClipboardImpl
import com.rcl.nextshiki.di.clipboard.IClipboard
import com.rcl.nextshiki.di.language.LanguageModule
import com.rcl.nextshiki.di.settings.SettingsModuleObject
import io.github.aakira.napier.Napier
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.createDefaultTextStyle
import org.jetbrains.jewel.intui.standalone.theme.createEditorTextStyle
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.ui.component.painterResource
import org.jetbrains.jewel.window.DecoratedWindow
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.awt.Toolkit

fun main() = application {
    setupNapier()
    startKoin {
        modules(
            module {
                single<IClipboard> {
                    ClipboardImpl().setClipboard(Toolkit.getDefaultToolkit().systemClipboard)
                }
            },
            LanguageModule.langModule,
            RepositoryModule.networkModule,
            SettingsModuleObject.settingsModule
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

    val icon = painterResource(SharedRes.images.icon.filePath)

    val root = remember {
        RootComponent(DefaultComponentContext(lifecycle))
    }
    val themeDefinition =
        if (isSystemInDarkTheme()) {
            JewelTheme.darkThemeDefinition(
                defaultTextStyle = JewelTheme.createDefaultTextStyle(),
                editorTextStyle = JewelTheme.createEditorTextStyle()
            )
        } else {
            JewelTheme.lightThemeDefinition(
                defaultTextStyle = JewelTheme.createDefaultTextStyle(),
                editorTextStyle = JewelTheme.createEditorTextStyle()
            )
        }
    IntUiTheme(
        theme = themeDefinition,
        styling = ComponentStyling.default().decoratedWindow()
    ) {
        DecoratedWindow(
            icon = icon,
            state = windowState,
            title = APP_NAME,
            onCloseRequest = ::exitApplication
        ) {
            TitleBarView()
            App(rootComponent = root, seedColor = composeColor)
        }
    }
}