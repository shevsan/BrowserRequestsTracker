package ua.oshevchuk.browserrequeststracker.ui.screens

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import ua.oshevchuk.browserrequeststracker.common.Response
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import ua.oshevchuk.browserrequeststracker.ui.service.BrowserTrackingService

@Composable
fun RequestHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: BrowserTrackingViewModel = hiltViewModel()
) {
    val requestsState = viewModel.resultsState.collectAsState().value
    val context = LocalContext.current
    val isLoading = remember {
        mutableStateOf(false)
    }
    val serviceEnabled = isAccessibilityServiceEnabled(context, BrowserTrackingService::class.java)
    val requestList = remember {
        mutableStateOf<List<BrowserRequestEntity>?>(null)
    }
    when (requestsState) {
        is Response.Loading -> {
            isLoading.value = true
        }

        is Response.Success -> {
            isLoading.value = false
            requestList.value = requestsState.data
        }

        is Response.Error -> {
            showErrorToast(text = requestsState.message, context = context)
        }

        else -> {}
    }

    if (!serviceEnabled)
        openAccessibilitySettings(context)

    RequestHistoryScreenContent(modifier = modifier, requestList, isLoading = isLoading)
}

fun showErrorToast(text: String, context: Context) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    startActivity(context, intent, Bundle())
}

@Composable
fun RequestHistoryScreenContent(
    modifier: Modifier = Modifier,
    requests: MutableState<List<BrowserRequestEntity>?>,
    isLoading: MutableState<Boolean>
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (!isLoading.value && requests.value != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                items(requests.value ?: listOf()) {
                    RequestItem(request = it, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp))
                }
            }
        }
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }

}

fun isAccessibilityServiceEnabled(context: Context, service: Class<out AccessibilityService>): Boolean {
    val enabledServicesSetting = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    if(enabledServicesSetting != null)
        colonSplitter.setString(enabledServicesSetting)
    else
        return false

    while (colonSplitter.hasNext()) {
        val componentName = colonSplitter.next()
        if (componentName.equals("${context.packageName}/${service.name}", ignoreCase = true)) {
            return true
        }
    }
    return false
}

@Preview(showBackground = true)
@Composable
private fun RequestHistoryScreenPreview() {
    RequestHistoryScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp
            ),
        requests = remember {
            mutableStateOf(listOf())
        },
        isLoading = remember {
            mutableStateOf(false)
        }
    )

}