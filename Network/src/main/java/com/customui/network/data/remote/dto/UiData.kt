package com.customui.network.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UiData(
    @SerialName("uitype") var uiType: String? = null,
    @SerialName("value") var value: String? = null,
    @SerialName("key") var key: String? = null,
    @SerialName("hint") var hint: String? = null
)