package ru.lingstra.communications.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String
) {
    companion object {
        val empty = UserEntity("", "")
    }

    fun isEmpty() = this == empty
    fun isNotEmpty() = this != empty
}