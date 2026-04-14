package com.shrisaikrupa.stores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.shrisaikrupa.stores.data.model.CartItem
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController, productId: String,
    productVM: ProductViewModel, cartVM: CartViewModel
) {
    val products by productVM.products.collectAsState()
    val product = products.find { it.id == productId }
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            snackbarMessage = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Product Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed, titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        product?.let {
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = product.imageUrl.ifEmpty { "https://via.placeholder.com/400" },
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth().height(280.dp),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.padding(16.dp)) {
                    Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(product.category, color = MediumGray, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("₹${product.price}", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
                    Spacer(Modifier.height(12.dp))
                    Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(product.description, color = DarkGray, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("In Stock: ${product.stock} units", color = MediumGray, fontSize = 13.sp)
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            cartVM.addToCart(CartItem(productId = product.id,
                                productName = product.name, productImage = product.imageUrl,
                                price = product.price))
                            snackbarMessage = "Added to cart!"
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                        Spacer(Modifier.width(8.dp))
                        Text("ADD TO CART", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            cartVM.addToCart(CartItem(productId = product.id,
                                productName = product.name, productImage = product.imageUrl,
                                price = product.price))
                            navController.navigate(Routes.CART)
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryRed)
                    ) { Text("BUY NOW", fontWeight = FontWeight.Bold) }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryRed)
        }
    }
}