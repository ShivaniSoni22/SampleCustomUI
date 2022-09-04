package com.sample.samplecustomui.ui.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.customui.network.data.remote.dto.CustomUiResponse
import com.customui.network.data.remote.dto.UiData
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.sample.samplecustomui.ui.compose.OutLinedTextLayout
import com.sample.samplecustomui.ui.compose.PlayLogoAnimation
import com.sample.samplecustomui.ui.theme.HTTPRequestWithKtorClientComposeTheme
import com.sample.samplecustomui.ui.viewModel.CustomUiViewModel
import com.sample.samplecustomui.ui.viewModel.CustomUiViewModelFactory

class CustomUiFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, CustomUiViewModelFactory(requireActivity().application)
        )[CustomUiViewModel::class.java]
    }
    val args: CustomUiFragmentArgs by navArgs()
    var isLayoutVisible = mutableStateOf(false)
    var isAnimationVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLayoutVisible.value = args.isCustomUiVisible
        isAnimationVisible = args.isAnimationVisible
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val customUiItems = produceState(
                    initialValue = CustomUiResponse(),
                    producer = { value = viewModel.getCustomUiElements() }
                )

                HTTPRequestWithKtorClientComposeTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        val uiDataList = customUiItems.value.uiData
                        if (uiDataList.isNotEmpty()) {
                            if (isAnimationVisible) {
                                PlayAnimation(customUiItems)
                            }
                            if (isLayoutVisible.value) {
                                TextInputFields(uiDataList)
                            }
                        } else
                            if (viewModel.isAfterFirstFetch.value) {
                                Text(
                                    text = "Oops!! No items available",
                                    textAlign = TextAlign.Center
                                )
                            }
                    }
                }
            }
        }
    }

    @Composable
    private fun PlayAnimation(
        customUiItems: State<CustomUiResponse>
    ) {
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
        customUiItems.value.logoUrl?.let { url ->
            Glide.with(LocalContext.current).asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
        bitmap?.let {
            PlayLogoAnimation(it, customUiItems.value.headingText ?: "") { isVisible ->
                isLayoutVisible.value = isVisible
            }
        }
    }

    @Composable
    private fun TextInputFields(uiDataList: ArrayList<UiData>) {
        Column {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .navigationBarsWithImePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(items = uiDataList) { index, item ->
                    key(item.key) {
                        RenderForm(item, uiDataList)
                    }
                }
            }
            if (uiDataList.isEmpty()) {
                Text(text = "Oops!! No items available")
            }
        }
    }

    @Composable
    private fun RenderForm(
        item: UiData,
        uiDataList: ArrayList<UiData>
    ) {
        if (!item.uiType.isNullOrEmpty()) {
            val focusManager = LocalFocusManager.current
            when (item.uiType) {
                "label" -> {
                    RenderTextInputLayout(uiDataList, item, focusManager)
                }
                "button" -> {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.onSubmitClicked()
                            if (viewModel.isNavigationAllowed.value)
                                findNavController().navigate(
                                    CustomUiFragmentDirections.actionCustomUiFragmentToBottomSheetFragment(
                                        viewModel.name.value.value,
                                        viewModel.mobileNumber.value.value,
                                        viewModel.city.value.value
                                    )
                                )
                        },
                        modifier = Modifier.padding(vertical = 30.dp)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }

    @Composable
    private fun RenderTextInputLayout(
        uiDataList: ArrayList<UiData>,
        item: UiData,
        focusManager: FocusManager
    ) {
        uiDataList.find { uiData ->
            if (item.key.equals("label_name") && uiData.key.equals("text_name")) {
                OutLinedTextLayout(
                    item = item,
                    textInputElement = uiData,
                    inputWrapper = viewModel.name.value,
                    onValueChange = { viewModel.validateView(viewModel.name, it) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    onImeKeyAction = { focusManager.moveFocus(FocusDirection.Down) }
                )
                true
            } else if (item.key.equals("label_phone") && uiData.key.equals("text_phone")) {
                OutLinedTextLayout(
                    item = item,
                    textInputElement = uiData,
                    inputWrapper = viewModel.mobileNumber.value,
                    onValueChange = { viewModel.validateView(viewModel.mobileNumber, it) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Phone
                    ),
                    onImeKeyAction = { focusManager.moveFocus(FocusDirection.Down) }
                )
                true
            } else if (item.key.equals("label_city") && uiData.key.equals("text_city")) {
                OutLinedTextLayout(
                    item = item,
                    textInputElement = uiData,
                    inputWrapper = viewModel.city.value,
                    onValueChange = { viewModel.validateView(viewModel.city, it) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    onImeKeyAction = { focusManager.clearFocus() }
                )
                true
            } else false
        }
    }

}