package com.shrisaikrupa.stores.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    navController: NavController,
    authVM: AuthViewModel,
    productVM: ProductViewModel,
    orderVM: OrderViewModel
) {
    val products by productVM.products.collectAsState()
    val orders by orderVM.orders.collectAsState()

    LaunchedEffect(Unit) { orderVM.loadAllOrders() }

    val totalRevenue = orders.sumOf { it.totalAmount }
    val pendingOrders = orders.count { it.status == "Pending" }
    val deliveredOrders = orders.count { it.status == "Delivered" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Gold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkRed, titleContentColor = Gold
                ),
                actions = {
                    IconButton(onClick = {
                        authVM.logout()
                        navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    }) {
                        Icon(Icons.Default.Logout, null, tint = Gold)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).background(LightGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkRed),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.size(80.dp).clip(CircleShape).background(Gold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkRed)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Administrator", color = Gold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("admin@123.com", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        Surface(shape = RoundedCornerShape(20.dp), color = Gold) {
                            Text(
                                "ADMIN ACCESS",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkRed
                            )
                        }
                    }
                }
            }
            item { Text("Store Overview", fontWeight = FontWeight.Bold, fontSize = 16.sp) }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(Modifier.weight(1f), "📦", "Products", "${products.size}", PrimaryRed)
                    AdminStatCard(Modifier.weight(1f), "📋", "Total Orders", "${orders.size}", DarkRed)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(Modifier.weight(1f), "⏳", "Pending", "$pendingOrders", Color(0xFFE65100))
                    AdminStatCard(Modifier.weight(1f), "✅", "Delivered", "$deliveredOrders", Color(0xFF2E7D32))
                }
            }
            item {
                AdminStatCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = "💰",
                    title = "Total Revenue",
                    value = "₹${"%.0f".format(totalRevenue)}",
                    bgColor = Color(0xFF1565C0)
                )
            }
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Category, null, tint = PrimaryRed)
                            Spacer(Modifier.width(8.dp))
                            Text("Products by Category", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        listOf(
                            "Rice & Masala" to "🌾",
                            "Books & Stationery" to "📚",
                            "Toys" to "🧸",
                            "Electrical" to "💡"
                        ).forEach { (cat, emoji) ->
                            val count = products.count { it.category == cat }
                            Row(
                                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(emoji, fontSize = 20.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text(cat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                        Text("$count products", color = MediumGray, fontSize = 11.sp)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (count > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                                ) {
                                    Text(
                                        if (count > 0) "Available" else "Empty",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp, color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            if (cat != "Electrical") HorizontalDivider(color = LightGray)
                        }
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, null, tint = PrimaryRed)
                    Spacer(Modifier.width(8.dp))
                    Text("All Orders", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.weight(1f))
                    if (orders.isNotEmpty()) {
                        Surface(shape = RoundedCornerShape(20.dp), color = PrimaryRed) {
                            Text(
                                "${orders.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (orders.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📋", fontSize = 40.sp)
                                Text("No orders yet", color = MediumGray)
                            }
                        }
                    }
                }
            } else {
                items(orders.sortedByDescending { it.timestamp }) { order ->
                    AdminOrderCard(order = order, orderVM = orderVM)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun AdminOrderCard(order: com.shrisaikrupa.stores.data.model.Order, orderVM: OrderViewModel) {
    val dateStr = remember(order.timestamp) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(order.timestamp))
    }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Order #${order.id.take(8).uppercase()}",
                        fontWeight = FontWeight.Bold, fontSize = 13.sp
                    )
                    Text(dateStr, color = MediumGray, fontSize = 11.sp)
                    if (order.customerName.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null,
                                tint = MediumGray, modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(order.customerName, color = MediumGray, fontSize = 11.sp)
                        }
                    }
                }

                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when (order.status) {
                            "Delivered" -> Color(0xFF4CAF50)
                            "Cancelled" -> Color(0xFFF44336)
                            "Out for Delivery" -> Color(0xFF1565C0)
                            else -> Gold
                        },
                        onClick = {
                            if (order.status != "Delivered" && order.status != "Cancelled") {
                                showMenu = true
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                order.status,
                                fontSize = 11.sp,
                                color = if (order.status == "Pending") DarkGray else Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (order.status != "Delivered" && order.status != "Cancelled") {
                                Spacer(Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.ArrowDropDown, null,
                                    tint = if (order.status == "Pending") DarkGray else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        if (order.status == "Pending") {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("🚚", fontSize = 16.sp)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Out for Delivery")
                                    }
                                },
                                onClick = {
                                    orderVM.updateOrderStatus(order.id, "Out for Delivery")
                                    showMenu = false
                                }
                            )
                        }
                        if (order.status == "Pending" || order.status == "Out for Delivery") {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("✅", fontSize = 16.sp)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Mark as Delivered")
                                    }
                                },
                                onClick = {
                                    orderVM.updateOrderStatus(order.id, "Delivered")
                                    showMenu = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("❌", fontSize = 16.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Cancel Order", color = Color(0xFFF44336))
                                }
                            },
                            onClick = {
                                orderVM.updateOrderStatus(order.id, "Cancelled")
                                showMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightGray, thickness = 0.8.dp)
            Spacer(Modifier.height(10.dp))
            order.items.forEach { item ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LightGold
                        ) {
                            Text(
                                "x${item.quantity}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 11.sp, color = DarkRed, fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(item.productName, fontSize = 12.sp, color = DarkGray)
                    }
                    Text(
                        "₹${"%.0f".format(item.price * item.quantity)}",
                        fontSize = 12.sp, color = PrimaryRed, fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp), color = LightGray, thickness = 0.8.dp)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💵 COD", color = MediumGray, fontSize = 12.sp)
                Text(
                    "Total: ₹${"%.2f".format(order.totalAmount)}",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryRed
                )
            }

            if (order.address.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.LocationOn, null,
                        tint = MediumGray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(order.address, color = MediumGray, fontSize = 11.sp)
                }
            }

            if (order.phone.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null,
                        tint = MediumGray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(order.phone, color = MediumGray, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(modifier: Modifier, icon: String, title: String, value: String, bgColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(icon, fontSize = 28.sp)
            Spacer(Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(title, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
        }
    }
}