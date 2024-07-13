package ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces

import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity

interface AddRequestUseCase {
    suspend operator fun invoke(entity : BrowserRequestEntity)
}