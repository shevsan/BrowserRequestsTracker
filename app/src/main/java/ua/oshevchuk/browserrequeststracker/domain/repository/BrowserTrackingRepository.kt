package ua.oshevchuk.browserrequeststracker.domain.repository

import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity

interface BrowserTrackingRepository {
    suspend fun insertRequest(requst : BrowserRequestEntity)
    suspend fun clearAll()
    suspend fun getAllRequests() : List<BrowserRequestEntity>
}