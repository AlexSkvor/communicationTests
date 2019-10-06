package ru.lingstra.avitocopy.system

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class SystemMessage {
    private val notifierRelay = PublishRelay.create<Message>()

    val notifier: Observable<Message> = notifierRelay.hide()

    fun sendSystemMessage(message: Message) = notifierRelay.accept(message)

    fun send(message: String) = notifierRelay.accept(Message(message))

    fun showProgress(visible: Boolean, message: String = "") = notifierRelay.accept(Message(message, visible, Type.Progress))

    data class Message(
        val text: String,
        val progress: Boolean = false,
        val type: Type = Type.Toast)

    sealed class Type {
        object Toast : Type()
        object Progress : Type()
    }

}