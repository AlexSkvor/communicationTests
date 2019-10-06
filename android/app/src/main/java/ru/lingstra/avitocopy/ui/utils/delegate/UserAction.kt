package ru.lingstra.avitocopy.ui.utils.delegate

sealed class UserAction<T>(private val logMessage: String) {
    data class ItemPressed<T>(val item: T) : UserAction<T>("ItemPressed $item")
    data class ItemEdit<T>(val item: T) : UserAction<T>("ItemEdit $item")

    override fun toString(): String = logMessage
    fun action() = this
}