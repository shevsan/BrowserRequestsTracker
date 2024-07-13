package ua.oshevchuk.browserrequeststracker.domain.usecases.implementations

import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import javax.inject.Inject

class AddRequestUseCaseImpl @Inject constructor(
    private val repository: BrowserTrackingRepository
) : AddRequestUseCase {
    override suspend fun invoke(entity: BrowserRequestEntity) {
        repository.insertRequest(entity)
    }
}