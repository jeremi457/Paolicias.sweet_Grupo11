package com.example.myappnavigation.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myappnavigation.data.local.dao.ReservaDao
import com.example.myappnavigation.data.local.dao.UsuarioDao
import com.example.myappnavigation.data.local.entity.ReservaEntity
import com.example.myappnavigation.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class, ReservaEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun reservaDao(): ReservaDao

    companion object {          //propio del patrón singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Myapp_database.db" // definimos nombre del archivo SQLite
                )
                    // al subir la versión se recrea la base; hay que volver a registrar el usuario
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}