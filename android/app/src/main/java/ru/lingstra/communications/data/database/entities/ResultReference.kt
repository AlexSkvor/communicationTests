package ru.lingstra.communications.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "result_references",
    foreignKeys = [
        ForeignKey(
            entity = TestEntity::class,
            parentColumns = ["id"],
            childColumns = ["testId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class ResultReferenceEntity(
    @PrimaryKey val id: String,
    val testId: String,
    val minMark: Int,
    val maxMark: Int,
    val resultDescription: String
)