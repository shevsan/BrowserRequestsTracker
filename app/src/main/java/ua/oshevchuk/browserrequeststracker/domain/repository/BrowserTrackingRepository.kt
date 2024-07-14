package ua.oshevchuk.browserrequeststracker.domain.repository

import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity

interface BrowserTrackingRepository {
    suspend fun insertRequest(request : BrowserRequestEntity)
    suspend fun clearAll()
    suspend fun getAllRequests() : List<BrowserRequestEntity>
    suspend fun deleteRequest(id : Int)
}