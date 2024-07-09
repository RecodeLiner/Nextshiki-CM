
import com.rcl.nextshiki.di.ktor.KtorModuleObject.networkModule
import com.rcl.nextshiki.di.settings.SettingsModuleObject.settingsModule
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.test.verify.Verify.verify
import kotlin.test.Test

class DependenciesTest {
    @KoinExperimentalAPI
    @Test
    fun depVerifyTest() {
        koinApplication {
            verify(settingsModule)
            verify(networkModule, extraTypes = listOf(HttpClientEngine::class, HttpClientConfig::class))
        }
    }
}
