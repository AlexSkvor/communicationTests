package ru.lingstra.avitocopy.domain.hand_shakes

import java.util.*

data class HandShakesViewState(
    val answerList: List<User> = listOf(),
    val startTime: Long = Date().time,
    val endTime: Long = Date().time,
    val loaded: Boolean = false,
    val loadingInProcess: Boolean = false,
    val queries: Int = 0
)