package dev.abhinav.stocktracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.abhinav.stocktracker.database.WatchlistDao
import dev.abhinav.stocktracker.database.WatchlistDatabase
import dev.abhinav.stocktracker.remote.ServiceApi
import dev.abhinav.stocktracker.repository.StockRepository
import dev.abhinav.stocktracker.repository.StockWatchlistRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Base URL
    private const val URL = "https://financialmodelingprep.com/"

    // Logging Interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideWatchlistDatabase(@ApplicationContext context: Context): WatchlistDatabase {
        return Room.databaseBuilder(
            context,
            WatchlistDatabase::class.java,
            "watchlist_database"
        ).build()
    }

    // Provides dependency of DAO interface
    @Provides
    @Singleton
    fun provideWatchlistDao(database: WatchlistDatabase): WatchlistDao {
        return database.watchlistDao()
    }

    // Provides dependency of repository class
    @Singleton
    @Provides
    fun provideRepository(
        api: ServiceApi
    ) = StockRepository(api)

    // Provides dependency of repository class
    @Singleton
    @Provides
    fun provideWatchlistRepository(
        api: ServiceApi,
        dao: WatchlistDao
    ) = StockWatchlistRepository(api, dao)

    // Provides retrofit instance of api call
    @Singleton
    @Provides
    fun provideApi() : ServiceApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .client(client)
            .build()
            .create(ServiceApi::class.java)
    }
}