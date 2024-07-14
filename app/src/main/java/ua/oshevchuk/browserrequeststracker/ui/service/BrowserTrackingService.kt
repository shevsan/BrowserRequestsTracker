package ua.oshevchuk.browserrequeststracker.ui.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ua.oshevchuk.browserrequeststracker.common.executeSafely
import ua.oshevchuk.browserrequeststracker.common.extractUrl
import ua.oshevchuk.browserrequeststracker.di.annotations.IoDispatcher
import ua.oshevchuk.browserrequeststracker.domain.usecases.interfaces.AddRequestUseCase
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import javax.inject.Inject

@AndroidEntryPoint
class BrowserTrackingService : AccessibilityService() {

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var scope: CoroutineScope

    @Inject
    lateinit var addRequestUseCase: AddRequestUseCase


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        scope = CoroutineScope(ioDispatcher + SupervisorJob())

        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
            ) {
                val nodeInfo = event.source ?: return
                val url = extractUrl(nodeInfo)

                url?.let {
                    if (it.contains("google.com")) {
                        scope.launch {
                            executeSafely {
                                addRequestUseCase(BrowserRequestEntity(url = it, timestamp = System.currentTimeMillis()))
                            }
                        }
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
