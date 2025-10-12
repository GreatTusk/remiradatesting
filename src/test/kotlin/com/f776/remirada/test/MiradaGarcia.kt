package com.f776.remirada.test

internal object MiradaGarcia {
    val BASE_URL by lazy {
        System.getenv("BASE_URL") ?: "http://localhost:3000"
    }
}