package com.sample.samplecustomui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.customui.network.data.remote.CustomUiService
import com.customui.network.data.remote.dto.CustomUiResponse
import com.sample.samplecustomui.ui.theme.HTTPRequestWithKtorClientComposeTheme

class MainActivity : ComponentActivity() {

    private val service = CustomUiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customUiItems = produceState<List<CustomUiResponse>>(
            initialValue = emptyList(),
            producer = {
                value = service.getCustomUi()
            }
        )

        HTTPRequestWithKtorClientComposeTheme {
            Surface(color = MaterialTheme.colors.background) {
                LazyColumn {
                    itemsIndexed(items = customUiItems.value[2].uiData) { index, item ->
                        if (!item.uiType.isNullOrEmpty()) {
                            when (item.uiType) {
                                "label" -> {
//                                    Text(
//
//                                    )
                                }
                                "edittext"->{
                                    var text by remember { mutableStateOf("") }
                                    OutlinedTextField(
                                        value = text,
                                        onValueChange = {
                                            text = it
                                        },
                                        placeholder = {item.hint}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}