package ua.oshevchuk.browserrequeststracker.domain.usecases.implementations

import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.ClearAllUseCase
import javax.inject.Inject

class ClearAllUseCaseImpl @Inject constructor(
    private val repository: BrowserTrackingRepository
) : ClearAllUseCase {
    override suspend fun invoke() {
        repository.clearAll()
    }
}