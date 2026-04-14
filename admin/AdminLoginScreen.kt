package com.shrisaikrupa.stores.ui.screens.admin

import androidx.compose.foundation.background
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
fun AdminLoginScreen(navController: NavController, authVM: AuthViewModel) {
    var email by remember { mutableStateOf("admin@123.com") }
    var password by remember { mutableStateOf("admin123") }
    var error by remember { mutableStateOf("") }
    val authState by authVM.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success && email == "admin@123.com") {
            authVM.resetState()
            navController.navigate(Routes.ADMIN_PANEL) { popUpTo(Routes.ADMIN_LOGIN) { inclusive = true } }
        }
    }

    Column(
        Modifier.fillMaxSize().background(DarkRed).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("⚙️", fontSize = 56.sp)
        Text("ADMIN LOGIN", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Shri Saikrupa Stores", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        Spacer(Modifier.height(32.dp))
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)) {
            Column(Modifier.padding(24.dp)) {
                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text("Admin Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = password, onValueChange = { password = it },
                    label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
                if (authState is AuthState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text((authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (email != "admin@123.com") error = "Not an admin account"
                        else authVM.login(email, password)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkRed)
                ) { Text("ADMIN LOGIN", fontWeight = FontWeight.Bold) }
            }
        }
    }
}