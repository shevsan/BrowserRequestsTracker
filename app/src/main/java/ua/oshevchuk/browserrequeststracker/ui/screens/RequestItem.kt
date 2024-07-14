@file:OptIn(ExperimentalMaterial3Api::class)

package ua.oshevchuk.browserrequeststracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.oshevchuk.browserrequeststracker.R
import ua.oshevchuk.browserrequeststracker.ui.enitites.BrowserRequestEntity
import kotlin.time.Duration.Companion.seconds

@Composable
fun RequestItem(
    modifier: Modifier = Modifier,
    request: BrowserRequestEntity,
    onRemove: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(0.1.seconds)
                    onRemove()
                }
                true
            } else {
                false
            }

        }
    )
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val toastText = stringResource(id = R.string.url_has_been_copied)
    SwipeToDismissBox(modifier = modifier, state = swipeToDismissBoxState, backgroundContent = {}) {
        Card(shape = RoundedCornerShape(20.dp), onClick = {
            clipboardManager.setText(AnnotatedString(request.url))
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 15.dp),
            ) {
                Text(
                    text = request.extractQuery() ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = request.url,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${request.getTime()} ${request.getDate()}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RequestItemPreview() {
    RequestItem(
        modifier = Modifier.fillMaxWidth(),
        request = BrowserRequestEntity.generateFakeItem(),
        onRemove = {}
    )
}