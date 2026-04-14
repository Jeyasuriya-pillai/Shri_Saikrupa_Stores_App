package com.shrisaikrupa.stores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.shrisaikrupa.stores.data.model.CartItem
import com.shrisaikrupa.stores.data.model.Product
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.*

@Composable
fun HomeScreen(
    navController: NavController,
    productVM: ProductViewModel,
    cartVM: CartViewModel,
    authVM: AuthViewModel,
    profileVM: UserProfileViewModel,
    orderVM: OrderViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val cartItems by cartVM.cartItems.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryRed, selectedTextColor = PrimaryRed,
                        indicatorColor = LightGold
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        BadgedBox(badge = {
                            if (cartItems.isNotEmpty()) Badge { Text("${cartItems.size}") }
                        }) { Icon(Icons.Default.ShoppingCart, "Cart") }
                    },
                    label = { Text("Cart") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryRed, selectedTextColor = PrimaryRed,
                        indicatorColor = LightGold
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryRed, selectedTextColor = PrimaryRed,
                        indicatorColor = LightGold
                    )
                )
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> HomeTab(navController, productVM, cartVM)
                1 -> CartScreen(navController, cartVM, showBottomBar = true)
                2 -> UserProfileScreen(navController, authVM, profileVM, orderVM)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab(navController: NavController, productVM: ProductViewModel, cartVM: CartViewModel) {
    val products by productVM.products.collectAsState()
    val isLoading by productVM.isLoading.collectAsState()
    val selectedCategory by productVM.selectedCategory.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(products, searchQuery) {
        if (searchQuery.isEmpty()) products
        else products.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize().background(LightGray)) {
        TopAppBar(
            title = {
                Column {
                    Text("SHRI SAIKRUPA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Provision Stores", fontSize = 11.sp, color = Gold)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PrimaryRed, titleContentColor = Color.White
            )
        )
        OutlinedTextField(
            value = searchQuery, onValueChange = { searchQuery = it },
            placeholder = { Text("Search products...", color = MediumGray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = PrimaryRed) },
            trailingIcon = {
                if (searchQuery.isNotEmpty())
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, null, tint = MediumGray)
                    }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryRed, unfocusedBorderColor = MediumGray,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )
        LazyRow(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productVM.categories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { productVM.filterByCategory(category); searchQuery = "" },
                    label = { Text(category, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryRed, selectedLabelColor = Color.White
                    )
                )
            }
        }
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryRed)
            }
        } else if (filteredProducts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (searchQuery.isNotEmpty()) "🔍" else "📦", fontSize = 48.sp)
                    Text(
                        if (searchQuery.isNotEmpty()) "No results for \"$searchQuery\""
                        else "No products found",
                        color = MediumGray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { navController.navigate("product_detail/${product.id}") },
                        onAddToCart = {
                            cartVM.addToCart(CartItem(
                                productId = product.id, productName = product.name,
                                productImage = product.imageUrl, price = product.price
                            ))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onProductClick: () -> Unit, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onProductClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth().height(130.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(10.dp)) {
                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(product.category, color = MediumGray, fontSize = 11.sp)
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("₹${product.price}", color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier.size(32.dp).background(PrimaryRed, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}