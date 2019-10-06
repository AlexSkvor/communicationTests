package ru.lingstra.communications.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String
)