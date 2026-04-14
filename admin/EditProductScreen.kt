package com.shrisaikrupa.stores.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(navController: NavController, productId: String, productVM: ProductViewModel) {
    val products by productVM.products.collectAsState()
    val product = products.find { it.id == productId }
    val operationState by productVM.operationState.collectAsState()
    var name by remember(product) { mutableStateOf(product?.name ?: "") }
    var description by remember(product) { mutableStateOf(product?.description ?: "") }
    var price by remember(product) { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember(product) { mutableStateOf(product?.stock?.toString() ?: "") }
    var imageUrl by remember(product) { mutableStateOf(product?.imageUrl ?: "") }
    var selectedCategory by remember(product) { mutableStateOf(product?.category ?: "Rice & Masala") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(operationState) {
        if (operationState == "Product updated!") {
            productVM.resetOperationState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkRed, titleContentColor = Gold)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)
            .verticalScroll(rememberScrollState())) {
            Card(shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Edit Product Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it },
                        label = { Text("Product Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = description, onValueChange = { description = it },
                        label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = price, onValueChange = { price = it },
                        label = { Text("Price (₹)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = stock, onValueChange = { stock = it },
                        label = { Text("Stock Quantity") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it },
                        label = { Text("Image URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(value = selectedCategory, onValueChange = {}, readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor())
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Rice & Masala", "Books & Stationery", "Toys", "Electrical").forEach { cat ->
                                DropdownMenuItem(text = { Text(cat) },
                                    onClick = { selectedCategory = cat; expanded = false })
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    product?.let {
                        productVM.updateProduct(it.copy(name = name, description = description,
                            price = price.toDoubleOrNull() ?: it.price,
                            stock = stock.toIntOrNull() ?: it.stock,
                            imageUrl = imageUrl, category = selectedCategory))
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkRed),
                enabled = name.isNotEmpty() && price.isNotEmpty()
            ) { Text("UPDATE PRODUCT", fontWeight = FontWeight.Bold, color = Gold) }
        }
    }
}