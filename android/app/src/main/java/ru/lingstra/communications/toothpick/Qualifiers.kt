package ru.lingstra.communications.toothpick

import javax.inject.Qualifier

@Qualifier
annotation class DefaultServerPath

@Qualifier
annotation class SuccessInterceptor

@Qualifier
annotation class SuccessOkHttpClient

@Qualifier
annotation class FilterSearch