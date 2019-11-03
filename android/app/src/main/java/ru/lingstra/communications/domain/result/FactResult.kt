package ru.lingstra.communications.domain.result

import org.joda.time.DateTime
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.domain.models.Test

data class FactResult(
    val time: DateTime,
    val user: UserEntity,
    val test: Test
) {
}