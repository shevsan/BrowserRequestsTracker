package ua.oshevchuk.browserrequeststracker.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.oshevchuk.browserrequeststracker.domain.usecases.implementations.AddRequestUseCaseImpl
import ua.oshevchuk.browserrequeststracker.domain.usecases.implementations.ClearAllUseCaseImpl
import ua.oshevchuk.browserrequeststracker.domain.usecases.implementations.DeleteRequestUseCaseImpl
import ua.oshevchuk.browserrequeststracker.domain.usecases.implementations.GetAllRequestsUseCaseImpl
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.ClearAllUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.DeleteRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindAddRequestUseCase(addRequestUseCaseImpl: AddRequestUseCaseImpl): AddRequestUseCase

    @Binds
    abstract fun bindClearAllUseCase(clearAllUseCaseImpl: ClearAllUseCaseImpl): ClearAllUseCase

    @Binds
    abstract fun bindGetAllRequestsUseCase(getAllRequestsUseCaseImpl: GetAllRequestsUseCaseImpl): GetAllRequestsUseCase

    @Binds
    abstract fun bindDeleteRequestUseCase(deleteRequestUseCaseImpl: DeleteRequestUseCaseImpl): DeleteRequestUseCase
}