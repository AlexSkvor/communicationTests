package ru.lingstra.communications.data.data_entities

import android.util.Base64
import java.nio.charset.StandardCharsets

data class GitResponse(
    val type: String,
    val encoding: String,
    val content: String
) {
    val fileText: String
        get() = String(
            Base64.decode(content, Base64.DEFAULT),
            StandardCharsets.UTF_8
        )
}