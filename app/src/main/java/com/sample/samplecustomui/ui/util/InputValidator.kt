package com.sample.samplecustomui.ui.util

import java.util.regex.Pattern

private fun getPhoneNumberError(phoneNumber: String): String? {
    return if (isValidPhoneNumber(phoneNumber)) null
    else "Input is Invalid"
}

private fun getTextValidationError(name: String): String? {
    return if (name.trim() != "") null
    else "Input is Invalid"
}

private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    if (phoneNumber.isEmpty()) return true
    return if (!Pattern.matches("[a-zA-Z]+", phoneNumber)) phoneNumber.length in 10..13
    else false
}

fun String.getValidation(label: String): String? {
    return when (label) {
        "Phone Number" -> getPhoneNumberError(this)
        "City",
        "Name" -> getTextValidationError(this)
        else -> null
    }
}