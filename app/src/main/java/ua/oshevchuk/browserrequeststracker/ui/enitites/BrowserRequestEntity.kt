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
        return try {
            val decodedUrl = java.net.URLDecoder.decode(url, "UTF-8")

            val queryStartIndex = decodedUrl.indexOf("?q=")
            val altQueryStartIndex = decodedUrl.indexOf("&q=")

            val startIndex = if (queryStartIndex != -1) {
                queryStartIndex + 3
            } else if (altQueryStartIndex != -1) {
                altQueryStartIndex + 3
            } else {
                -1
            }

            if (startIndex != -1) {
                val endIndex = decodedUrl.indexOf('&', startIndex)

                val query = if (endIndex != -1) {
                    decodedUrl.substring(startIndex, endIndex)
                } else {
                    decodedUrl.substring(startIndex)
                }

                query.replace("+", " ")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    companion object {
        fun generateFakeItem() =
            BrowserRequestEntity(
                url = "https://www.google.com/search?q=android&oq=android+&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTIHCAEQABiABDIHCAIQABiABDIHCAMQABiABDIGCAQQRRg7MgcIBRAAGIAEMgcIBhAAGIAEMgcIBxAAGIAEMgcICBAAGIAEMgcICRAAGIAE0gEJNDIzMGowajE1qAIIsAIB&sourceid=chrome&ie=UTF-8",
                timestamp = 1720892673000
            )
    }
}