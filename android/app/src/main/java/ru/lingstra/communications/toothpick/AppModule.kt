package ru.lingstra.communications.toothpick

import android.content.Context
import android.os.Environment
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.*
import ru.lingstra.communications.data.database.providers.*
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
        bind(NavigationManager::class.java).toInstance(NavigationManager())
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
        bind(QuestionDao::class.java).toProvider(QuestionDaoProvider::class.java).singletonInScope()
        bind(AnswerDao::class.java).toProvider(AnswerDaoProvider::class.java).singletonInScope()
        bind(TestPassingDao::class.java).toProvider(TestPassingDaoProvider::class.java)
            .singletonInScope()
        bind(FactAnswerDao::class.java).toProvider(FactAnswerDaoProvider::class.java)
            .singletonInScope()

        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.toString()
        bind(String::class.java).withName(DefaultFilesDir::class.java).toInstance(filesDir)
    }
}