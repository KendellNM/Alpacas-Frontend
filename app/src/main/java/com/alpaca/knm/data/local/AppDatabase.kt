package com.alpaca.knm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alpaca.knm.data.local.dao.AlpacaDao
import com.alpaca.knm.data.local.dao.GanaderoDao
import com.alpaca.knm.data.local.dao.SolicitudDao
import com.alpaca.knm.data.local.entity.AlpacaEntity
import com.alpaca.knm.data.local.entity.GanaderoEntity
import com.alpaca.knm.data.local.entity.SolicitudEntity

/**
 * Base de datos local Room para modo offline
 */
@Database(
    entities = [
        GanaderoEntity::class,
        AlpacaEntity::class,
        SolicitudEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun ganaderoDao(): GanaderoDao
    abstract fun alpacaDao(): AlpacaDao
    abstract fun solicitudDao(): SolicitudDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alpaca_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
