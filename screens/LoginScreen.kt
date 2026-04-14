package com.shrisaikrupa.stores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.*

@Composable
fun LoginScreen(navController: NavController, authVM: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authVM.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            authVM.resetState()
            navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(LightGray).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🛒", fontSize = 56.sp)
        Text("SHRI SAIKRUPA", color = PrimaryRed, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("PROVISION STORES", color = MediumGray, fontSize = 13.sp)
        Spacer(Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Welcome Back", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Login to your account", color = MediumGray, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = password, onValueChange = { password = it },
                    label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (authState is AuthState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text((authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { authVM.login(email, password) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading)
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    else Text("LOGIN", fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row {
            Text("New user? ", color = MediumGray)
            Text("Register", color = PrimaryRed, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate(Routes.REGISTER) })
        }
        Spacer(Modifier.height(8.dp))
        Text("Admin Login", color = Gold, fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { navController.navigate(Routes.ADMIN_LOGIN) })
    }
}