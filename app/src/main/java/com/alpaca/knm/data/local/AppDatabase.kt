package com.alpaca.knm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alpaca.knm.data.local.dao.AlpacaDao
import com.alpaca.knm.data.local.dao.GanaderoDao
import com.alpaca.knm.data.local.dao.PendingSolicitudDao
import com.alpaca.knm.data.local.dao.SolicitudDao
import com.alpaca.knm.data.local.entity.AlpacaEntity
import com.alpaca.knm.data.local.entity.GanaderoEntity
import com.alpaca.knm.data.local.entity.PendingSolicitudEntity
import com.alpaca.knm.data.local.entity.SolicitudEntity

@Database(
    entities = [
        GanaderoEntity::class,
        AlpacaEntity::class,
        SolicitudEntity::class,
        PendingSolicitudEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun ganaderoDao(): GanaderoDao
    abstract fun alpacaDao(): AlpacaDao
    abstract fun solicitudDao(): SolicitudDao
    abstract fun pendingSolicitudDao(): PendingSolicitudDao
    
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
