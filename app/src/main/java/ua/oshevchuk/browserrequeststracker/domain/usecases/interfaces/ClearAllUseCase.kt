package ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces

interface ClearAllUseCase {
    suspend operator fun invoke()
}