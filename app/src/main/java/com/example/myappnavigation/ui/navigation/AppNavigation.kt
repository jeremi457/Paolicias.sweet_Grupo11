package com.example.myappnavigation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myappnavigation.ui.screen.CatalogScreen
import com.example.myappnavigation.ui.screen.Login
import com.example.myappnavigation.ui.screen.Principal
import com.example.myappnavigation.ui.screen.Register
import com.example.myappnavigation.ui.screen.ReservaScreen
import com.example.myappnavigation.ui.screen.ScreenHeroe

object Routes {
    const val SUPERHERO = "superhero"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRINCIPAL = "principal"
    const val CATALOG = "catalog"
    const val QUOTER = "quoter"
    const val RESERVATIONS = "reservations"
    const val UPLOAD_RECEIPT = "upload_receipt"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.SUPERHERO
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
                onLoginSuccess = {
                    navController.navigate(Routes.PRINCIPAL) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
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
                onNavigateToCatalog = { navController.navigate(Routes.CATALOG) },
                onNavigateToQuoter = { navController.navigate(Routes.QUOTER) },
                onNavigateToReservations = { /* Próxima pantalla */ },
                onNavigateToUploadReceipt = { /* Próxima pantalla */ }
            )
        }

        composable(Routes.CATALOG) {
            CatalogScreen(
                onProductoClick = { producto ->
                    // Acción al seleccionar un producto del catálogo
                }
            )
        }

        composable(Routes.QUOTER) {
            ReservaScreen(
                onBackClick = { navController.popBackStack() },
                onReservarClick = { fecha, horario, asistentes, telefono, observaciones, total ->
                    // Siguiente paso: ir a la pantalla de subir comprobante
                    navController.navigate(Routes.UPLOAD_RECEIPT)
                }
            )
        }

        // Próximos composables a implementar: CATALOG, QUOTER, RESERVATIONS, UPLOAD_RECEIPT
    }
}