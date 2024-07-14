package ua.oshevchuk.browserrequeststracker.common

import android.view.accessibility.AccessibilityNodeInfo

fun extractUrl(nodeInfo: AccessibilityNodeInfo?): String? {
    if (nodeInfo == null) return null

    var currentNode = nodeInfo
    while (currentNode != null) {
        if (currentNode.className == "android.webkit.WebView" || currentNode.className == "android.widget.EditText") {
            val url = currentNode.text?.toString()
            if (url != null) {
                return url
            }
        }

        currentNode = currentNode.parent
    }

    return null
}