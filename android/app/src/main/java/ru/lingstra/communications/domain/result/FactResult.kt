package ru.lingstra.communications.domain.result

import org.joda.time.DateTime
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.domain.models.Test

data class FactResult(
    val time: DateTime,
    val user: UserEntity,
    val test: Test,
    var fullText: Boolean = false
) {

    val resultText: String by lazy {
        val mark = test.questions.map { it.answers.first() }.map { it.mark }.sum()
        test.results.forEach {
            if (mark in it.minMark..it.maxMark) return@lazy it.text
        }
        return@lazy ""
    }
}