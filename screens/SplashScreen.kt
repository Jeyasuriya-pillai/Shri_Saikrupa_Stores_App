package com.shrisaikrupa.stores.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, authVM: AuthViewModel) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        if (authVM.isLoggedIn) navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
        else navController.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(PrimaryRed),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = visible, enter = fadeIn() + scaleIn()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛒", fontSize = 72.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "SHRI SAIKRUPA",
                    color = Gold,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "PROVISION STORES",
                    color = Color.White,
                    fontSize = 16.sp,
                    letterSpacing = 3.sp
                )
            }
        }
    }
}