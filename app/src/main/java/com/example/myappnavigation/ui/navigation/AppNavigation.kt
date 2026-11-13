package com.example.myappnavigation.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myappnavigation.ui.screen.CatalogScreen
import com.example.myappnavigation.ui.screen.Login
import com.example.myappnavigation.ui.screen.Principal
import com.example.myappnavigation.ui.screen.Register
import com.example.myappnavigation.ui.screen.ReservaScreen
import com.example.myappnavigation.ui.screen.ReservationsScreen
import com.example.myappnavigation.ui.screen.ScreenHeroe
import com.example.myappnavigation.ui.screen.UploadReceiptScreen
import com.example.myappnavigation.ui.screen.AdminProductoFormScreen
import com.example.myappnavigation.ui.screen.AdminProductosScreen

object Routes {
    const val SUPERHERO = "superhero"
    const val LOGIN = "login"
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

    // Navegación de la barra inferior: vuelve al menú principal sin apilar pantallas
    fun irA(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Routes.PRINCIPAL)
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SUPERHERO,
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
                onLoginSuccess = { esAdmin ->
                    if (esAdmin) {
                        navController.navigate(Routes.ADMIN_PRODUCTS) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.PRINCIPAL) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
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
                onNavigateToReservations = { navController.navigate(Routes.RESERVATIONS) }
            )
        }

        composable(Routes.CATALOG) {
            CatalogScreen(
                onProductoClick = { producto ->
                    // Al elegir un producto se abre la cotización de ese producto
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
                    // Se reemplaza el formulario por la pantalla de comprobante
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
                onCerrarSesionClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMIN_PRODUCTS) { inclusive = true }
                    }
                }
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