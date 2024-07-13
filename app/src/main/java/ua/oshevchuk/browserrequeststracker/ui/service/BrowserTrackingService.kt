package ua.oshevchuk.browserrequeststracker.ui.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import dagger.hilt.android.AndroidEntryPoint
import ua.oshevchuk.browserrequeststracker.App
import ua.oshevchuk.browserrequeststracker.ui.screens.BrowserTrackingViewModel
import ua.oshevchuk.browserrequeststracker.ui.screens.BrowserTrackingViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class BrowserTrackingService : AccessibilityService() {

    @Inject
    lateinit var viewModelFactory: BrowserTrackingViewModelFactory
    private lateinit var viewModel: BrowserTrackingViewModel

    override fun onCreate() {
        super.onCreate()
        viewModel = ViewModelProvider(store = ViewModelStore(), factory = viewModelFactory).get(BrowserTrackingViewModel::class.java)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
            ) {
                val nodeInfo = event.source ?: return
                val url = viewModel.extractUrl(nodeInfo)

                url?.let {
                    if (it.contains("google.com")) {
                        viewModel.saveUrlToDatabase(it, System.currentTimeMillis())
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted.")
    }

    companion object {
        private const val TAG = "BrowserTrackingService"
    }
}
