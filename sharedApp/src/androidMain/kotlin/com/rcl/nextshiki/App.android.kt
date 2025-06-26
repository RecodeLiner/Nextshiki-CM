package com.rcl.nextshiki

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.defaultComponentContext
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

class AndroidApp : Application() {
    private lateinit var globalModule: GlobalAppModule
    override fun onCreate() {
        super.onCreate()
        val platformComponent = AndroidPlatformComponent(this.applicationContext)
        globalModule = GlobalAppModule(platformComponent)
    }
    fun globalModule(): GlobalAppModule = globalModule
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root =
            RootComponent(
                componentContext = DIComponentContextImpl(
                    defaultComponentContext(),
                    (application as AndroidApp).globalModule()
                )
            )

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            App(root)
        }
    }
}

class AndroidPlatformComponent(
    applicationContext: Context
): PlatformSpecificComponents() {
    override val roomDatabase: RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder(ctx = applicationContext)
    override val dataStoreBuilder: DataStoreBuilder = getPreferencesDataStoreBuilder(ctx = applicationContext)
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

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("dpkss.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getPreferencesDataStoreBuilder(ctx: Context): DataStoreBuilder {
    return DataStoreBuilder(ctx.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath)
}