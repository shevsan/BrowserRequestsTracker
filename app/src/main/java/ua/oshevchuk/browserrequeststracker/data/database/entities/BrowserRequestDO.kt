package ua.oshevchuk.browserrequeststracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "browser_requests")
data class BrowserRequestDO(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val url : String? = null,
    val timestamp : Long? = null
)