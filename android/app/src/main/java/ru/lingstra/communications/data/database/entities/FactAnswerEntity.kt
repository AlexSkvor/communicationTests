package ru.lingstra.communications.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "fact_answers",
    primaryKeys = ["passingId", "answerId"],
    foreignKeys = [
        ForeignKey(
            entity = TestPassingEntity::class,
            parentColumns = ["id"],
            childColumns = ["passingId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ), ForeignKey(
            entity = AnswerEntity::class,
            parentColumns = ["id"],
            childColumns = ["answerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class FactAnswerEntity(
    val passingId: String,
    val answerId: String
)