package com.shrisaikrupa.stores.ui.screens.admin

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
import com.shrisaikrupa.stores.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(navController: NavController, productVM: ProductViewModel) {
    val products by productVM.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkRed, titleContentColor = Gold
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.ADMIN_PROFILE) }) {
                        Icon(Icons.Default.Person, null, tint = Gold)
                    }
                    IconButton(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) {
                        Icon(Icons.Default.Add, null, tint = Gold)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD_PRODUCT) },
                containerColor = PrimaryRed) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Card(modifier = Modifier.fillMaxWidth().padding(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightGold),
                shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("📦", fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Total Products", color = MediumGray, fontSize = 12.sp)
                        Text("${products.size}", fontSize = 24.sp,
                            fontWeight = FontWeight.Bold, color = DarkRed)
                    }
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    Card(shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = product.imageUrl.ifEmpty { "https://via.placeholder.com/60" },
                                contentDescription = product.name,
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(product.category, color = MediumGray, fontSize = 12.sp)
                                Text("₹${product.price} | Stock: ${product.stock}",
                                    color = PrimaryRed, fontSize = 13.sp)
                            }
                            IconButton(onClick = {
                                navController.navigate("edit_product/${product.id}")
                            }) {
                                Icon(Icons.Default.Edit, null, tint = Gold)
                            }
                            IconButton(onClick = { productVM.deleteProduct(product.id) }) {
                                Icon(Icons.Default.Delete, null, tint = PrimaryRed)
                            }
                        }
                    }
                }
            }
        }
    }
}