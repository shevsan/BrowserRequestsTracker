package ua.oshevchuk.browserrequeststracker.data.database.converters

import ua.oshevchuk.browserrequeststracker.data.database.entities.BrowserRequestDO
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity

fun BrowserRequestEntity.toDO() = BrowserRequestDO(
    url = this.url,
    timestamp = this.timestamp
)

fun BrowserRequestDO.toEntity() = BrowserRequestEntity(
    url = this.url ?: "",
    timestamp = this.timestamp ?: 0L,
    id = this.id
)