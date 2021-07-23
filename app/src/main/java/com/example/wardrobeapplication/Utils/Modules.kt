package com.example.wardrobeapplication.Utils

import androidx.room.Room
import com.example.wardrobeapplication.repository.MainDataStoreFactory
import com.example.wardrobeapplication.repository.MainRepository
import com.example.wardrobeapplication.storage.MainStorageSource
import com.example.wardrobeapplication.storage.room.PairsDatabase
import com.example.wardrobeapplication.storage.room.RoomModule
import com.example.wardrobeapplication.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    private val applicationModules = module {
        single {
            Room.databaseBuilder(get(), PairsDatabase::class.java, "pairs.debug.room.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    private val viewModelModules = module {
        viewModel { MainViewModel(get())}
    }

    private val repoModules = module {
        single { MainRepository(get())}


    }

    private val dataModule = module {
        factory { MainDataStoreFactory(get()) }
    }

    private val storageModule = module {
        factory { MainStorageSource(get()) }
    }

    private val daoModules = module {
        single { RoomModule(get()).getPairsDao() }
    }

    fun getAll() = listOf(
        viewModelModules,
        repoModules,
        applicationModules,
        daoModules,
        dataModule,
        storageModule
    )

}