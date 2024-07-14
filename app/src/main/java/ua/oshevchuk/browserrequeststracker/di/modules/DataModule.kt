package ua.oshevchuk.browserrequeststracker.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.oshevchuk.browserrequeststracker.data.database.AppDatabase
import ua.oshevchuk.browserrequeststracker.di.annotations.DefaultDispatcher
import ua.oshevchuk.browserrequeststracker.di.annotations.IoDispatcher
import ua.oshevchuk.browserrequeststracker.di.annotations.MainDispatcher
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.ClearAllUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.DeleteRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import ua.oshevchuk.browserrequeststracker.ui.screens.BrowserTrackingViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java, "browser_requests"
    ).build()

    @Provides
    @Singleton
    fun provideBrowserTrackingViewModelFactory(
        addRequestUseCase: Provider<AddRequestUseCase>,
        getAllRequestsUseCase: Provider<GetAllRequestsUseCase>,
        deleteRequestUseCase: Provider<DeleteRequestUseCase>,
        clearAllUseCase: Provider<ClearAllUseCase>
    ) = BrowserTrackingViewModelFactory(
        addRequestUseCase,
        getAllRequestsUseCase,
        deleteRequestUseCase,
        clearAllUseCase
    )


    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    @Singleton
    @Provides
    fun provideRequestsDao(db: AppDatabase) = db.getDao()
}