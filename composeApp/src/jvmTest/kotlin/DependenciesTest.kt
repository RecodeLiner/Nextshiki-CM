import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import com.rcl.nextshiki.di.settings.SettingsModule.settingsModule
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import kotlin.test.Test

class DependenciesTest {
    @Test
    fun depVerifyTest() {
        koinApplication {
            modules(settingsModule, networkModule)
            checkModules()
        }
    }
}