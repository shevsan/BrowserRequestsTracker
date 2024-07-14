package ua.oshevchuk.browserrequeststracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.oshevchuk.browserrequeststracker.common.Response
import ua.oshevchuk.browserrequeststracker.common.executeSafely
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.ClearAllUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.DeleteRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import javax.inject.Inject

@HiltViewModel
class BrowserTrackingViewModel @Inject constructor(
    private val getAllRequestsUseCase: GetAllRequestsUseCase,
    private val deleteRequestUseCase: DeleteRequestUseCase,
    private val clearAllUseCase: ClearAllUseCase
) : ViewModel() {


    private val _clearAllState = MutableStateFlow<Response<Unit>?>(null)
    val clearAllState = _clearAllState.asStateFlow()

    private val _removeRequestState = MutableStateFlow<Response<Unit>?>(null)
    val removeRequestState = _removeRequestState.asStateFlow()

    private val _resultsState = MutableStateFlow<Response<List<BrowserRequestEntity>>?>(null)
    val resultsState = _resultsState.asStateFlow()

    init {
        getAllRequests()
    }


    fun getAllRequests() {
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

    fun removeRequest(requestId: Int) {
        viewModelScope.launch {
            executeSafely {
                _removeRequestState.emit(Response.Loading())
                deleteRequestUseCase(requestId)
            }.onSuccess {
                _removeRequestState.emit(Response.Success(Unit))
                _resultsState.update { currentState ->
                    currentState?.let { response ->
                        if (response is Response.Success) {
                            val updatedList = response.data.filter { it.id != requestId }
                            Response.Success(updatedList)
                        } else {
                            response
                        }
                    }
                }
            }.onFailure {
                _removeRequestState.emit(Response.Error(it.message ?: "error"))
            }
        }
    }


    fun clearAllRequests() {
        viewModelScope.launch {
            executeSafely {
                _clearAllState.emit(Response.Loading())
                clearAllUseCase()
            }.onSuccess {
                _clearAllState.emit(Response.Success(Unit))
                getAllRequests()
            }.onFailure {
                _clearAllState.emit(Response.Error(it.message ?: "error"))
            }
        }
    }



}