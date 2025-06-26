
import com.rcl.nextshiki.data.api.CalendarRemoteDataSource
import com.rcl.nextshiki.data.api.CalendarRemoteDataSourceImpl
import com.rcl.nextshiki.data.repository.CalendarRepository
import com.rcl.nextshiki.data.repository.CalendarRepositoryImpl
import com.rcl.nextshiki.domain.usecases.GetCalendarUseCase
import com.rcl.nextshiki.features.di.GlobalAppModule

class HomeFeatureModule (
    private val globalAppModule: GlobalAppModule
) {
    val calendarRemoteDataSource: CalendarRemoteDataSource by lazy {
        CalendarRemoteDataSourceImpl(globalAppModule.authorizedHttpClient)
    }

    val calendarRepository: CalendarRepository by lazy {
        CalendarRepositoryImpl(
            calendarRemoteDataSource,
            globalAppModule.calendarEntryDao
        )
    }

    val getCalendarUseCase: GetCalendarUseCase by lazy {
        GetCalendarUseCase(
            repository = calendarRepository
        )
    }
}