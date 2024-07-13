package ua.oshevchuk.browserrequeststracker.domain.usecases.implementations

import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import javax.inject.Inject

class GetAllRequestsUseCaseImpl @Inject constructor(
    private val repository: BrowserTrackingRepository
) : GetAllRequestsUseCase {
    override suspend fun invoke(): List<BrowserRequestEntity> = repository.getAllRequests()
}