package ua.oshevchuk.browserrequeststracker.domain.usecases.implementations

import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.DeleteRequestUseCase
import javax.inject.Inject

class DeleteRequestUseCaseImpl @Inject constructor(private val repository: BrowserTrackingRepository) : DeleteRequestUseCase {
    override suspend fun invoke(id: Int) {
        repository.deleteRequest(id)
    }
}