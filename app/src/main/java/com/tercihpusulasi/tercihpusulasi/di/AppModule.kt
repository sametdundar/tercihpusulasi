package com.tercihpusulasi.tercihpusulasi.di

import android.app.Application
import androidx.room.Room
import com.sametdundar.guaranteeapp.roomdatabase.AppDatabase
import com.sametdundar.guaranteeapp.roomdatabase.FormDao
import com.sametdundar.guaranteeapp.roomdatabase.FormRepository
import com.tercihpusulasi.tercihpusulasi.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFormDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "form_database"
        ).build()
    }

    @Provides
    fun provideMyApplication(
        application: Application
    ): MyApplication {
        return application as MyApplication // Type cast to MyApplication
    }

    @Provides
    fun provideFormDao(db: AppDatabase): FormDao {
        return db.formDao()
    }

    @Provides
    @Singleton
    fun provideFormRepository(dao: FormDao): FormRepository {
        return FormRepository(dao)
    }
}
