package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.database.AppDatabase
import com.example.myappnavigation.data.local.entity.ProductoEntity
import com.example.myappnavigation.model.CatalogoData
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {
    var productos by mutableStateOf<List<ProductoEntity>>(emptyList())
        private set
    fun cargarProductos(context: Context) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(appContext).productoDao()
            CatalogoData.sembrarSiVacio(dao)
            productos = dao.obtenerActivos()
        }
    }
}
