
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.nextshiki.BuildKonfig
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.data.local.AppDatabase
import com.rcl.nextshiki.features.di.DIComponentContextImpl
import com.rcl.nextshiki.features.di.GlobalAppModule
import com.rcl.nextshiki.features.di.PlatformSpecificComponents
import com.rcl.nextshiki.features.di.modules.DataStoreBuilder
import com.rcl.nextshiki.features.di.modules.DataStoreBuilder.Companion.DATA_STORE_FILE_NAME
import com.rcl.nextshiki.presentation.App
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.awt.Dimension
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.SwingUtilities

fun main() = application {
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)
    LifecycleController(lifecycle, windowState)
    val platformComponent = JvmPlatformComponent()
    val globalModule = GlobalAppModule(platformComponent)

    val rootComponent = runOnUiThread {
        RootComponent(
            componentContext = DIComponentContextImpl(
                DefaultComponentContext(lifecycle = lifecycle),
                globalModule
            )
        )
    }
    BuildKonfig.appName?.let { name ->
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = name,
            content = {
                window.minimumSize = Dimension(350, 600)
                App(rootComponent)
            },
            onKeyEvent = { event ->
                when (event.key) {
                    Key.Escape -> {
                        rootComponent::navigateBack
                        true
                    }

                    else -> false
                }
            }
        )
    }
}

private fun getDataDirPath(): String {
    val appDataDir = when (System.getProperty("os.name").lowercase()) {
        in listOf("linux", "unix") -> {
            Paths.get(System.getProperty("user.home"), ".config", BuildKonfig.appName).toString()
        }

        in listOf("mac os x", "darwin") -> {
            Paths.get(
                System.getProperty("user.home"),
                "Library",
                "Application Support",
                BuildKonfig.appName
            ).toString()
        }

        else -> {
            Paths.get(
                System.getenv("APPDATA") ?: System.getProperty("user.home"),
                BuildKonfig.appName
            ).toString()
        }
    }

    Files.createDirectories(Paths.get(appDataDir))

    return appDataDir
}

class JvmPlatformComponent(): PlatformSpecificComponents() {
    override val roomDatabase: RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder()
    override val dataStoreBuilder: DataStoreBuilder = getPreferencesDataStoreBuilder()
    override val httpClient: HttpClient = getPlatformClient()
}

fun getPlatformClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
}

fun getPreferencesDataStoreBuilder(): DataStoreBuilder {
    val prFile = File(getDataDirPath(), DATA_STORE_FILE_NAME)
    return DataStoreBuilder(prFile.absolutePath)
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(getDataDirPath(), "app_database.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}