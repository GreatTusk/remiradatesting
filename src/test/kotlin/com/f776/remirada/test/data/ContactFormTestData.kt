package com.f776.remirada.test.data

import kotlinx.serialization.Serializable

@Serializable
data class ContactFormData(
    val name: String,
    val email: String,
    val phone: String,
    val message: String
)

@Serializable
data class ContactFormTestData(
    val validForm: ContactFormData
)

