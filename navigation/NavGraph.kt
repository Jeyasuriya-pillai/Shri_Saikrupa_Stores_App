package com.shrisaikrupa.stores.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shrisaikrupa.stores.ui.screens.*
import com.shrisaikrupa.stores.ui.screens.admin.*
import com.shrisaikrupa.stores.viewmodel.*

@Composable
fun NavGraph(navController: NavHostController) {
    val authVM: AuthViewModel = viewModel()
    val productVM: ProductViewModel = viewModel()
    val cartVM: CartViewModel = viewModel()
    val profileVM: UserProfileViewModel = viewModel()
    val orderVM: OrderViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController, authVM) }
        composable(Routes.LOGIN) { LoginScreen(navController, authVM) }
        composable(Routes.REGISTER) { RegisterScreen(navController, authVM) }
        composable(Routes.HOME) {
            HomeScreen(navController, productVM, cartVM, authVM, profileVM, orderVM)
        }
        composable(Routes.PRODUCT_DETAIL) { backStack ->
            val productId = backStack.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController, productId, productVM, cartVM)
        }
        composable(Routes.CART) { CartScreen(navController, cartVM) }
        composable(Routes.CHECKOUT) { CheckoutScreen(navController, cartVM, profileVM) }
        composable(Routes.ADMIN_LOGIN) { AdminLoginScreen(navController, authVM) }
        composable(Routes.ADMIN_PANEL) { AdminPanelScreen(navController, productVM) }
        composable(Routes.ADMIN_PROFILE) {
            AdminProfileScreen(navController, authVM, productVM, orderVM)
        }
        composable(Routes.ADD_PRODUCT) { AddProductScreen(navController, productVM) }
        composable(Routes.EDIT_PRODUCT) { backStack ->
            val productId = backStack.arguments?.getString("productId") ?: ""
            EditProductScreen(navController, productId, productVM)
        }
    }
}
