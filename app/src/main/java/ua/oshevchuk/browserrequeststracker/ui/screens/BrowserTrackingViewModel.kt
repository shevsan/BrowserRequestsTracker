package ua.oshevchuk.browserrequeststracker.ui.screens

import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.oshevchuk.browserrequeststracker.common.Response
import ua.oshevchuk.browserrequeststracker.common.executeSafely
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class BrowserTrackingViewModel @Inject constructor(
    private val addRequestUseCase: AddRequestUseCase,
    private val getAllRequestsUseCase: GetAllRequestsUseCase
): ViewModel() {


    private val _resultsState = MutableStateFlow<Response<List<BrowserRequestEntity>>?>(null)
    val resultsState = _resultsState.asStateFlow()


    init{
        viewModelScope.launch {
            executeSafely {
                _resultsState.emit(Response.Loading())
                getAllRequestsUseCase()
            }.onSuccess {
                _resultsState.emit(Response.Success(it))
            }.onFailure {
                _resultsState.emit(Response.Error(it.message ?: "error"))
            }
        }
    }


    fun extractUrl(nodeInfo: AccessibilityNodeInfo?): String? {
        if (nodeInfo == null) return null

        var currentNode = nodeInfo
        while (currentNode != null) {
            if (currentNode.className == "android.webkit.WebView" || currentNode.className == "android.widget.EditText") {
                val url = currentNode.text?.toString()
                if (url != null ) {
                    return url
                }
            }

            currentNode = currentNode.parent
        }

        return null
    }

    fun saveUrlToDatabase(url: String, timestamp: Long) {
        viewModelScope.launch {
            executeSafely {
                addRequestUseCase(
                    BrowserRequestEntity(
                        url = url,
                        timestamp = timestamp
                    )
                )
            }
        }
    }


}