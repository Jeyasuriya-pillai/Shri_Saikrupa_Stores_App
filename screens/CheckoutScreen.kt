package com.shrisaikrupa.stores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.CartViewModel
import com.shrisaikrupa.stores.viewmodel.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, cartVM: CartViewModel, profileVM: UserProfileViewModel) {
    val profile by profileVM.profile.collectAsState()
    val cartItems by cartVM.cartItems.collectAsState()
    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var phone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var address by remember(profile.address) { mutableStateOf(profile.address) }
    val orderState by cartVM.orderState.collectAsState()
    var orderPlaced by remember { mutableStateOf(false) }

    LaunchedEffect(orderState) {
        if (orderState == "Order Placed!") { orderPlaced = true; cartVM.resetOrderState() }
    }

    if (orderPlaced) { OrderSuccessScreen(navController); return }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed, titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).background(LightGray)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalShipping, null, tint = PrimaryRed)
                        Spacer(Modifier.width(8.dp))
                        Text("Delivery Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = PrimaryRed) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = PrimaryRed) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = address, onValueChange = { address = it },
                        label = { Text("Delivery Address") },
                        leadingIcon = { Icon(Icons.Default.Home, null, tint = PrimaryRed) },
                        modifier = Modifier.fillMaxWidth(), minLines = 3)
                }
            }
            Card(shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, null, tint = PrimaryRed, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Payment Method", color = MediumGray, fontSize = 12.sp)
                        Text("Cash on Delivery", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF4CAF50)) {
                        Text("COD", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Card(shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Receipt, null, tint = PrimaryRed)
                        Spacer(Modifier.width(8.dp))
                        Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    cartItems.forEach { item ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.productName} x${item.quantity}",
                                fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Text("₹${"%.0f".format(item.price * item.quantity)}",
                                fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Charge", color = MediumGray)
                        Text("FREE", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("₹${"%.2f".format(cartVM.totalPrice)}",
                            fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryRed)
                    }
                }
            }
            Button(
                onClick = { cartVM.placeOrder(address, phone, name) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                enabled = name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()
            ) {
                Icon(Icons.Default.ShoppingCartCheckout, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("PLACE ORDER • COD", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(navController: NavController) {
    Box(Modifier.fillMaxSize().background(LightGray), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("✅", fontSize = 72.sp)
            Text("Order Placed!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
            Text("Your order has been placed successfully.\nWe will deliver it soon!",
                color = MediumGray, fontSize = 14.sp, textAlign = TextAlign.Center)
            Card(shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightGold)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💵", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Payment Method", color = MediumGray, fontSize = 12.sp)
                        Text("Cash on Delivery", fontWeight = FontWeight.Bold, color = DarkGray)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
            ) { Text("CONTINUE SHOPPING", fontWeight = FontWeight.Bold) }
        }
    }
}