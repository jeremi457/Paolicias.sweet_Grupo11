package com.example.myappnavigation.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myappnavigation.data.local.entity.ProductoEntity

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertar(producto: ProductoEntity): Long

    @Insert
    suspend fun insertarTodos(productos: List<ProductoEntity>)

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Query("SELECT * FROM productos ORDER BY id")
    suspend fun obtenerTodos(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE activo = 1 ORDER BY id")
    suspend fun obtenerActivos(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ProductoEntity?

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int
}
