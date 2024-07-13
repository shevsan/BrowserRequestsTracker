package ua.oshevchuk.browserrequeststracker.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.oshevchuk.browserrequeststracker.data.repositories.BrowserTrackingRepositoryImpl
import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHeartRateRepository(browserTrackingRepositoryImpl: BrowserTrackingRepositoryImpl): BrowserTrackingRepository

}