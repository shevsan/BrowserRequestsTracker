package ua.oshevchuk.browserrequeststracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.GetAllRequestsUseCase
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class BrowserTrackingViewModelFactory @Inject constructor(
    private val addRequestUseCase: Provider<AddRequestUseCase>,
    private val getAllRequestsUseCase: Provider<GetAllRequestsUseCase>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrowserTrackingViewModel::class.java)) {
            return BrowserTrackingViewModel(
                addRequestUseCase.get(),
                getAllRequestsUseCase.get()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
