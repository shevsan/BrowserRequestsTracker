package ua.oshevchuk.browserrequeststracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ua.oshevchuk.browserrequeststracker.data.database.entities.BrowserRequestDO

@Dao
interface BrowserRequestsDao {
    @Upsert
    suspend fun insertRequest(browserRequestDO: BrowserRequestDO)

    @Query("SELECT * FROM browser_requests ORDER BY timestamp")
    suspend fun getAllBrowserRequests() : List<BrowserRequestDO>

    @Query("DELETE FROM browser_requests")
    suspend fun clearAll()

    @Query("DELETE FROM browser_requests WHERE id = :requestId")
    suspend fun deleteRequestById(requestId: Int)
}