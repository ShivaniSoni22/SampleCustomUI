package com.sample.samplecustomui.ui.viewModel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.customui.network.data.remote.CustomUiService
import com.customui.network.data.remote.dto.CustomUiResponse
import com.sample.samplecustomui.ui.model.InputWrapper
import com.sample.samplecustomui.ui.util.getValidation

class CustomUiViewModel(application: Application) : AndroidViewModel(application) {

    val name = mutableStateOf(InputWrapper(label = "Name"))
    val city = mutableStateOf(InputWrapper(label = "City"))
    val mobileNumber = mutableStateOf(InputWrapper(label = "Phone Number"))
    val isNavigationAllowed = mutableStateOf(false)
    val isAfterFirstFetch = mutableStateOf(false)
    private val service:CustomUiService = CustomUiService.create()

    fun validateView(wrapper: MutableState<InputWrapper>, input: String){
        wrapper.value = wrapper.value.copy(value = input, errorMsg = input.getValidation(wrapper.value.label))
    }

    fun onSubmitClicked(){
       val nameText = name.value.value.trim()
       val cityText = city.value.value.trim()
       val mobileNumberText = mobileNumber.value.value.trim()
       if (nameText.isNotEmpty() && cityText.isNotEmpty() && mobileNumberText.isNotEmpty()) {
         isNavigationAllowed.value= true
       } else {
          if (nameText.isEmpty())
             name.value = name.value.copy(value = nameText, errorMsg = "Name is Invalid")
          if (cityText.isEmpty())
             city.value = city.value.copy(value = cityText, errorMsg = "City is Invalid")
          if (mobileNumberText.isEmpty())
             mobileNumber.value = mobileNumber.value.copy(value = mobileNumberText,
                errorMsg = "Mobile Number is Invalid"
             )
       }
    }

   suspend fun getCustomUiElements():CustomUiResponse {
       val response = service.getCustomUi()
       isAfterFirstFetch.value = true
       return response
   }

 }