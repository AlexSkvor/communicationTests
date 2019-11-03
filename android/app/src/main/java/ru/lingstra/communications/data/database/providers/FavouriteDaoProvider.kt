package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.FavouriteDao
import javax.inject.Inject
import javax.inject.Provider

class FavouriteDaoProvider @Inject constructor(private val appDatabase: AppDatabase) :
    Provider<FavouriteDao> {
    override fun get(): FavouriteDao = appDatabase.favouriteDao()
}