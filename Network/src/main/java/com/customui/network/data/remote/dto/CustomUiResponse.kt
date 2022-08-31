package com.customui.network.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomUiResponse(
    @SerialName("logo-url") var logoUrl: String? = null,
    @SerialName("heading-text") var headingText: String? = null,
    @SerialName("uidata") var uiData: List<UiData> = listOf()
)