package com.lovelessgeek.housemanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lovelessgeek.housemanager.data.db.util.DateConverter

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: run {
                synchronized(LocalDatabase::class) {
                    buildDatabase(
                        context
                    )
                        .apply { INSTANCE = this }
                }
            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "HouseManager.db"
            ).build()
        }
    }
}