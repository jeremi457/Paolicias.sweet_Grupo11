package com.example.myappnavigation.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myappnavigation.data.local.dao.ProductoDao
import com.example.myappnavigation.data.local.dao.ReservaDao
import com.example.myappnavigation.data.local.dao.SesionDao
import com.example.myappnavigation.data.local.dao.UsuarioDao
import com.example.myappnavigation.data.local.entity.ProductoEntity
import com.example.myappnavigation.data.local.entity.ReservaEntity
import com.example.myappnavigation.data.local.entity.SesionEntity
import com.example.myappnavigation.data.local.entity.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        ReservaEntity::class,
        ProductoEntity::class,
        SesionEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun reservaDao(): ReservaDao
    abstract fun productoDao(): ProductoDao
    abstract fun sesionDao(): SesionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Myapp_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
