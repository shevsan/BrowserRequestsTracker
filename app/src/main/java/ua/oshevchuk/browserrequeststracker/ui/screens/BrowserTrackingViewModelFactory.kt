package ua.oshevchuk.browserrequeststracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.ClearAllUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.DeleteRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class BrowserTrackingViewModelFactory @Inject constructor(
    private val addRequestUseCase: Provider<AddRequestUseCase>,
    private val getAllRequestsUseCase: Provider<GetAllRequestsUseCase>,
    private val deleteRequestUseCase: Provider<DeleteRequestUseCase>,
    private val clearAllUseCase: Provider<ClearAllUseCase>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrowserTrackingViewModel::class.java)) {
            return BrowserTrackingViewModel(
                addRequestUseCase.get(),
                getAllRequestsUseCase.get(),
                deleteRequestUseCase.get(),
                clearAllUseCase.get()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
