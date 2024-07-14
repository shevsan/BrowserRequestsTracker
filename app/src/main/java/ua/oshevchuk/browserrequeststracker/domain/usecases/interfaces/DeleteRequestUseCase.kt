package ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces

interface DeleteRequestUseCase {
    suspend operator fun invoke(id : Int)
}