package com.shrisaikrupa.stores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartVM: CartViewModel, showBottomBar: Boolean = false) {
    val cartItems by cartVM.cartItems.collectAsState()
    val totalPrice by remember { derivedStateOf { cartVM.totalPrice } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart (${cartItems.size})") },
                navigationIcon = {
                    if (!showBottomBar) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed, titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", color = MediumGray, fontSize = 12.sp)
                            Text("₹${"%.2f".format(totalPrice)}",
                                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryRed)
                        }
                        Button(
                            onClick = { navController.navigate(Routes.CHECKOUT) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                            modifier = Modifier.height(48.dp)
                        ) { Text("CHECKOUT", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 64.sp)
                    Text("Your cart is empty", color = MediumGray, fontSize = 16.sp)
                    if (!showBottomBar) {
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)) {
                            Text("Continue Shopping")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = item.productImage.ifEmpty { "https://via.placeholder.com/80" },
                                contentDescription = item.productName,
                                modifier = Modifier.size(72.dp), contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.productName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Qty: ${item.quantity}", color = MediumGray, fontSize = 12.sp)
                                Text("₹${item.price * item.quantity}",
                                    color = PrimaryRed, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { cartVM.removeFromCart(item.id) }) {
                                Icon(Icons.Default.Delete, null, tint = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}