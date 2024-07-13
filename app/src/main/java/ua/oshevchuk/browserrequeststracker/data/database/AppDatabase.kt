package ua.oshevchuk.browserrequeststracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.oshevchuk.browserrequeststracker.data.database.dao.BrowserRequestsDao
import ua.oshevchuk.browserrequeststracker.data.database.entities.BrowserRequestDO

@Database(entities = [BrowserRequestDO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): BrowserRequestsDao
}