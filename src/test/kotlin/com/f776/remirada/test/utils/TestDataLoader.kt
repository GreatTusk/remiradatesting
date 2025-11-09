package com.f776.remirada.test.utils

import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

object TestDataLoader {
    @PublishedApi
    internal val json = Json { ignoreUnknownKeys = true }

    inline fun <reified T> loadTestData(fileName: String): T {
        val path = Paths.get("src/test/resources/testdata/$fileName")
        val jsonString = Files.readString(path)
        return json.decodeFromString<T>(jsonString)
    }
}

