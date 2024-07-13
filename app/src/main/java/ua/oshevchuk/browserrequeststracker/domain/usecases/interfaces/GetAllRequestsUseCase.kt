package ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces

import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity

interface GetAllRequestsUseCase {
    suspend operator fun invoke() : List<BrowserRequestEntity>
}