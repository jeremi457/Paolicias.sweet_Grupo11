package com.example.myappnavigation.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myappnavigation.data.session.SesionActual
import com.example.myappnavigation.data.session.SesionManager
import com.example.myappnavigation.ui.screen.AdminProductoFormScreen
import com.example.myappnavigation.ui.screen.AdminProductosScreen
import com.example.myappnavigation.ui.screen.CatalogScreen
import com.example.myappnavigation.ui.screen.Login
import com.example.myappnavigation.ui.screen.LoginAdmin
import com.example.myappnavigation.ui.screen.Principal
import com.example.myappnavigation.ui.screen.Register
import com.example.myappnavigation.ui.screen.ReservaScreen
import com.example.myappnavigation.ui.screen.ReservationsScreen
import com.example.myappnavigation.ui.screen.ScreenHeroe
import com.example.myappnavigation.ui.screen.UploadReceiptScreen

object Routes {
    const val SUPERHERO = "superhero"
    const val LOGIN = "login"
    const val ADMIN_LOGIN = "admin_login"
    const val REGISTER = "register"
    const val PRINCIPAL = "principal"
    const val CATALOG = "catalog"
    const val RESERVATIONS = "reservations"

    const val QUOTER = "quoter/{productoId}"
    const val UPLOAD_RECEIPT = "upload_receipt/{reservaId}"
    const val ADMIN_PRODUCTS = "admin_products"
    const val ADMIN_FORM = "admin_form/{productoId}"

    fun adminForm(productoId: Int) = "admin_form/$productoId"
    fun quoter(productoId: Int) = "quoter/$productoId"
    fun uploadReceipt(reservaId: Int) = "upload_receipt/$reservaId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Restaura la sesión guardada en SQLite y decide cuál es la primera pantalla
    var destinoInicial by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        SesionManager.restaurar(context)
        destinoInicial = when {
            SesionActual.esAdmin() -> Routes.ADMIN_PRODUCTS
            SesionActual.activa() -> Routes.PRINCIPAL
            else -> Routes.SUPERHERO
        }
    }

    val inicio = destinoInicial ?: return

    fun irA(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Routes.PRINCIPAL)
            launchSingleTop = true
        }
    }

    fun cerrarSesion() {
        SesionManager.cerrar(context)
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = inicio,
        // Animaciones de transición entre pantallas
        enterTransition = { slideInHorizontally(initialOffsetX = { it / 4 }) + fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it / 4 }) + fadeOut() }
    ) {
        composable(Routes.SUPERHERO) {
            ScreenHeroe(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.LOGIN) {
            Login(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onNavigateToAdminLogin = {
                    navController.navigate(Routes.ADMIN_LOGIN)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.PRINCIPAL) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADMIN_LOGIN) {
            LoginAdmin(
                onLoginExitoso = {
                    navController.navigate(Routes.ADMIN_PRODUCTS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable(Routes.REGISTER) {
            Register(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PRINCIPAL) {
            Principal(
                onReservarClick = { navController.navigate(Routes.CATALOG) },
                onNavigateToCatalog = { navController.navigate(Routes.CATALOG) },
                onNavigateToReservations = { navController.navigate(Routes.RESERVATIONS) },
                onCerrarSesionClick = { cerrarSesion() }
            )
        }

        composable(Routes.CATALOG) {
            CatalogScreen(
                onProductoClick = { producto ->
                    navController.navigate(Routes.quoter(producto.id))
                },
                onNavigateToHome = { irA(Routes.PRINCIPAL) },
                onNavigateToReservations = { irA(Routes.RESERVATIONS) }
            )
        }

        composable(
            route = Routes.QUOTER,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            ReservaScreen(
                productoId = productoId,
                onBackClick = { navController.popBackStack() },
                onReservaGuardada = { reservaId ->
                    navController.navigate(Routes.uploadReceipt(reservaId)) {
                        popUpTo(Routes.CATALOG)
                    }
                }
            )
        }

        composable(Routes.RESERVATIONS) {
            ReservationsScreen(
                onNavigateToHome = { irA(Routes.PRINCIPAL) },
                onNavigateToCatalog = { irA(Routes.CATALOG) },
                onSubirComprobanteClick = { reservaId ->
                    navController.navigate(Routes.uploadReceipt(reservaId))
                }
            )
        }

        composable(
            route = Routes.UPLOAD_RECEIPT,
            arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val reservaId = backStackEntry.arguments?.getInt("reservaId") ?: 0
            UploadReceiptScreen(
                reservaId = reservaId,
                onBackClick = { navController.popBackStack() },
                onComprobanteEnviado = {
                    navController.navigate(Routes.RESERVATIONS) {
                        popUpTo(Routes.PRINCIPAL)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.ADMIN_PRODUCTS) {
            AdminProductosScreen(
                onNuevoClick = { navController.navigate(Routes.adminForm(0)) },
                onEditarClick = { productoId ->
                    navController.navigate(Routes.adminForm(productoId))
                },
                onCerrarSesionClick = { cerrarSesion() }
            )
        }

        composable(
            route = Routes.ADMIN_FORM,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            AdminProductoFormScreen(
                productoId = productoId,
                onBackClick = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }
    }
}
