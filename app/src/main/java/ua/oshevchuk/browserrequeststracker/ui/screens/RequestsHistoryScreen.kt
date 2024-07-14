@file:OptIn(ExperimentalFoundationApi::class)

package ua.oshevchuk.browserrequeststracker.ui.screens

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ua.oshevchuk.browserrequeststracker.R
import ua.oshevchuk.browserrequeststracker.common.Response
import ua.oshevchuk.browserrequeststracker.ui.components.DotLoader
import ua.oshevchuk.browserrequeststracker.ui.components.OnLifecycleEvent
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import ua.oshevchuk.browserrequeststracker.ui.service.BrowserTrackingService
import ua.oshevchuk.browserrequeststracker.ui.theme.RedFF

@Composable
fun RequestsHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: BrowserTrackingViewModel = hiltViewModel()
) {
    val requestsState = viewModel.resultsState.collectAsState().value
    val clearAlLState = viewModel.clearAllState.collectAsState().value
    val removeRequestState = viewModel.removeRequestState.collectAsState().value
    val context = LocalContext.current
    val isServiceEnabled =
        isAccessibilityServiceEnabled(context, BrowserTrackingService::class.java)
    LaunchedEffect(Unit) {
        if (!isServiceEnabled)
            openAccessibilitySettings(context)
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.getAllRequests()
            }

            else -> {}
        }
    }
    RequestsHistoryScreenContent(
        modifier = modifier,
        onRefresh = {
            viewModel.getAllRequests()
        },
        onRemoveRequest = {
            viewModel.removeRequest(it)
        },
        requestsState = requestsState,
        context = context,
        removeRequestState = removeRequestState,
        clearAllState = clearAlLState,
        onClearAllClicked = {
            viewModel.clearAllRequests()
        }
    )
}

@Composable
fun RequestsHistoryScreenContent(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onRemoveRequest: (id: Int) -> Unit,
    requestsState: Response<List<BrowserRequestEntity>>?,
    context: Context,
    removeRequestState: Response<Unit>?,
    onClearAllClicked: () -> Unit,
    clearAllState: Response<Unit>?
) {
    val requests = remember(requestsState) {
        mutableStateOf<List<BrowserRequestEntity>?>(null)
    }
    val isLoading = remember {
        mutableStateOf(false)
    }

    when (requestsState) {
        is Response.Loading -> {
            isLoading.value = true
        }

        is Response.Success -> {
            isLoading.value = false
            requests.value = requestsState.data
        }

        is Response.Error -> {
            isLoading.value = false
            showToast(text = requestsState.message, context = context)
        }

        else -> {}
    }

    when (removeRequestState) {
        is Response.Loading -> {
            isLoading.value = true
        }

        is Response.Error -> {
            isLoading.value = false
            showToast(text = removeRequestState.message, context = context)
        }

        is Response.Success -> {
            isLoading.value = false
        }

        else -> {}
    }

    if (clearAllState is Response.Error)
        showToast(text = clearAllState.message, context = context)

    val isRefreshing = remember { mutableStateOf(false) }
    val swipeToRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing.value)
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        SwipeRefresh(
            state = swipeToRefreshState,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                if (requests.value != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        items(requests.value ?: listOf(), key = { it.id ?: -1 }) { request ->
                            AnimatedContent(
                                targetState = request,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut() using SizeTransform(false)
                                }, label = ""
                            ) { targetRequest ->
                                RequestItem(
                                    request = targetRequest,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .animateItemPlacement(tween(200)),
                                    onRemove = {
                                        onRemoveRequest(targetRequest.id ?: -1)
                                    }
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(25.dp))
                        }
                    }
                }

                if (!isLoading.value && requests.value?.isEmpty() == true) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(id = R.string.empty_list),
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = { openChrome(context) }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                            Text(
                                text = stringResource(id = R.string.click_to_google),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp), color = Color.Black)
                }
            }
        }
        val isMoreThanOneItem =
            (requests.value ?: listOf()).size > 1

        AnimatedVisibility(visible = isMoreThanOneItem) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val fab = createRefs()
                FloatingActionButton(
                    onClick = { if (removeRequestState !is Response.Loading) onClearAllClicked() },
                    shape = RoundedCornerShape(25.dp),
                    containerColor = RedFF,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 55.dp)
                        .constrainAs(fab.component1()) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    if (clearAllState is Response.Loading)
                        DotLoader(modifier = Modifier.height(15.dp))
                    else
                        Text(
                            text = stringResource(id = R.string.clear_history),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                }
            }
        }
    }
}


fun openChrome(context: Context) {
    val url = "https://www.google.com/"
    try {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.android.chrome")
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}

fun showToast(text: String, context: Context) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    startActivity(context, intent, Bundle())
}

fun isAccessibilityServiceEnabled(
    context: Context,
    service: Class<out AccessibilityService>
): Boolean {
    val enabledServicesSetting = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    if (enabledServicesSetting != null)
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
private fun RequestsHistoryScreenPreview() {
    RequestsHistoryScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp
            ),
        onRefresh = {},
        onRemoveRequest = {},
        requestsState = Response.Success(listOf()),
        context = LocalContext.current,
        removeRequestState = Response.Success(Unit),
        onClearAllClicked = {},
        clearAllState = Response.Success(Unit)
    )

}