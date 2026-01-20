package dev.abhinav.stocktracker

import android.app.Application
import dev.abhinav.stocktracker.di.networkModule
import dev.abhinav.stocktracker.di.databaseModule
import dev.abhinav.stocktracker.di.repositoryModule
import dev.abhinav.stocktracker.di.viewModelModule
import timber.log.Timber
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class StockTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin{
            androidLogger()
            androidContext(this@StockTrackerApplication)
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}