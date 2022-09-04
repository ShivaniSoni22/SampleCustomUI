package com.sample.samplecustomui.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.customui.network.data.remote.dto.UiData
import com.google.accompanist.insets.ProvideWindowInsets
import com.sample.samplecustomui.ui.model.InputWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OutLinedTextLayout(
    item: UiData,
    textInputElement: UiData?,
    inputWrapper: InputWrapper,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    onImeKeyAction: () -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    ProvideWindowInsets {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 40.dp)
        ) {
            OutlinedTextField(
                value = inputWrapper.value,
                onValueChange = {
                    onValueChange(it)
                },
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = { Text(item.value.toString()) },

                placeholder = {
                    Text(
                        text = textInputElement?.hint ?: "",
                        style = TextStyle(
                            color = MaterialTheme.colors.primary,
                            textAlign = TextAlign.Start
                        )
                    )
                },
                isError = inputWrapper.errorMsg != null,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onAny = { onImeKeyAction() }),
                maxLines = 1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                    unfocusedBorderColor = MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.high),
                    textColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
                )
            )
            if (inputWrapper.errorMsg != null) {
                ErrorText(text = inputWrapper.errorMsg)
            }
        }
    }
}

@Composable
fun ErrorText(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color.Red,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}