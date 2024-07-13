package ua.oshevchuk.browserrequeststracker.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ua.oshevchuk.browserrequeststracker.data.database.converters.toDO
import ua.oshevchuk.browserrequeststracker.data.database.converters.toEntity
import ua.oshevchuk.browserrequeststracker.data.database.dao.BrowserRequestsDao
import ua.oshevchuk.browserrequeststracker.di.annotations.IoDispatcher
import ua.oshevchuk.browserrequeststracker.domain.repository.BrowserTrackingRepository
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrowserTrackingRepositoryImpl @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val dao: BrowserRequestsDao
) : BrowserTrackingRepository {
    override suspend fun insertRequest(requst: BrowserRequestEntity) {
        withContext(ioDispatcher) {
            dao.insertRequest(requst.toDO())
        }
    }

    override suspend fun clearAll() {
        withContext(ioDispatcher) {
            dao.clearAll()
        }
    }

    override suspend fun getAllRequests(): List<BrowserRequestEntity> = withContext(ioDispatcher) {
        dao.getAllBrowserRequests().map { it.toEntity() }.reversed()
    }
}