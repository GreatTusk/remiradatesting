package com.f776.remirada.test.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterTestData(
    val testPassword: String,
    val sendrBaseUrl: String
)

