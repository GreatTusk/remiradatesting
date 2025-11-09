package com.f776.remirada.test.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginTestData(
    val invalidEmail: String,
    val incorrectPassword: String
)

