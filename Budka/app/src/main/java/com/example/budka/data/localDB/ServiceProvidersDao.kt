/*
 * *
 *  * Created by Ali Ashkeyev on 08.05.2022, 1:36
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 08.05.2022, 1:36
 *
 */

package com.example.budka.data.localDB

import android.content.Context
import androidx.room.*
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.ServiceProviderResponse
import com.example.budka.utils.Constants
@Dao
interface ServiceProvidersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ServiceProvider>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: ServiceProvider?)

    @Query("SELECT * FROM " + Constants.SERVICE_PROVIDERS_TABLE + " " + "LIMIT 20")
    fun getAll(): List<ServiceProvider>
}

@Database(entities = [ServiceProvider::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ServiceProvidersDatabase : RoomDatabase() {
    abstract fun serviceProviderDao(): ServiceProvidersDao


    companion object {
        private var INSTANCE: ServiceProvidersDatabase? = null
        fun getDatabase(context: Context): ServiceProvidersDatabase {

            if (INSTANCE == null) {
                INSTANCE = createDb(context)
            }
            return INSTANCE!!
        }

        private fun createDb(context: Context): ServiceProvidersDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ServiceProvidersDatabase::class.java,
                "serviceProviders.db"
            ).allowMainThreadQueries().fallbackToDestructiveMigration()
                .build()
        }
    }
}