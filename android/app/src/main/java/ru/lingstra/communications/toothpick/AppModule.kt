package ru.lingstra.communications.toothpick

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.ResultReferenceDao
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.providers.AppDatabaseProvider
import ru.lingstra.communications.data.database.providers.ResultReferenceDaoProvider
import ru.lingstra.communications.data.database.providers.TestDaoProvider
import ru.lingstra.communications.data.database.providers.UserDaoProvider
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.data.prefs.AppPrefsStorage
import ru.lingstra.communications.system.*
import ru.lingstra.communications.system.network.*
import ru.lingstra.communications.system.schedulers.AppSchedulers
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import toothpick.config.Module

class AppModule(context: Context, serverPath: String) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(AppPrefs::class.java).to(AppPrefsStorage::class.java).singletonInScope()
        bind(ResourceManager::class.java).singletonInScope()
        bind(SystemMessage::class.java).toInstance(SystemMessage())
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(String::class.java).withName(DefaultServerPath::class.java).toInstance(serverPath)

        bind(Interceptor::class.java)
            .withName(SuccessInterceptor::class.java)
            .to(NetworkInterceptor::class.java)
            .singletonInScope()

        bind(OkHttpClient::class.java)
            .withName(SuccessOkHttpClient::class.java)
            .toProvider(HttpClientProvider::class.java)
            .singletonInScope()

        bind(NetworkApi::class.java)
            .toProvider(ApiProvider::class.java)
            .singletonInScope()

        //DB binding
        bind(AppDatabase::class.java).toProvider(AppDatabaseProvider::class.java).singletonInScope()
        bind(UserDao::class.java).toProvider(UserDaoProvider::class.java).singletonInScope()
        bind(TestDao::class.java).toProvider(TestDaoProvider::class.java).singletonInScope()
        bind(ResultReferenceDao::class.java).toProvider(ResultReferenceDaoProvider::class.java)
            .singletonInScope()
    }
}