package ua.oshevchuk.browserrequeststracker.ui.enitites

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

data class BrowserRequestEntity(
    val id: Int? = null,
    val url: String,
    val timestamp: Long
) {
    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val netDate = Date(timestamp)
        return sdf.format(netDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val sdf = SimpleDateFormat("HH:mm")
        val netDate = Date(timestamp)
        return sdf.format(netDate)
    }


    fun extractQuery(): String? {
        try {
            val decodedUrl = java.net.URLDecoder.decode(url, "UTF-8")

            val queryStartIndex = decodedUrl.indexOf("?q=")

            if (queryStartIndex != -1) {
                val startIndex = queryStartIndex + 3
                val endIndex = decodedUrl.indexOf('&', startIndex)

                val query = if (endIndex != -1) {
                    decodedUrl.substring(startIndex, endIndex)
                } else {
                    decodedUrl.substring(startIndex)
                }

                return query.replace("+", " ")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        fun generateFakeItem() =
            BrowserRequestEntity(
                url = "https://www.google.com/search?q=android&oq=android+&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTIHCAEQABiABDIHCAIQABiABDIHCAMQABiABDIGCAQQRRg7MgcIBRAAGIAEMgcIBhAAGIAEMgcIBxAAGIAEMgcICBAAGIAEMgcICRAAGIAE0gEJNDIzMGowajE1qAIIsAIB&sourceid=chrome&ie=UTF-8",
                timestamp = 1720892673000
            )
    }
}