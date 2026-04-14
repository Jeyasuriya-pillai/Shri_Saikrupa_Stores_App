package com.shrisaikrupa.stores.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrisaikrupa.stores.data.model.Order
import com.shrisaikrupa.stores.navigation.Routes
import com.shrisaikrupa.stores.ui.theme.*
import com.shrisaikrupa.stores.viewmodel.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    authVM: AuthViewModel,
    profileVM: UserProfileViewModel,
    orderVM: OrderViewModel
) {
    LaunchedEffect(Unit) {
        profileVM.loadProfile()
        orderVM.loadMyOrders()
    }

    val profile by profileVM.profile.collectAsState()
    val orders by orderVM.orders.collectAsState()
    val isLoadingOrders by orderVM.isLoading.collectAsState()
    val saveState by profileVM.saveState.collectAsState()

    var editMode by remember { mutableStateOf(false) }
    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var phone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var address by remember(profile.address) { mutableStateOf(profile.address) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saveState) {
        if (saveState != null) {
            snackbarHostState.showSnackbar(saveState!!)
            profileVM.resetSaveState()
            editMode = false
        }
    }

    LaunchedEffect(profile) {
        if (!editMode) {
            name = profile.name
            phone = profile.phone
            address = profile.address
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryRed,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        if (editMode) {
                            profileVM.saveProfile(name, phone, address)
                        } else {
                            editMode = true
                        }
                    }) {
                        Icon(
                            if (editMode) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = null,
                            tint = Gold
                        )
                    }
                    IconButton(onClick = {
                        authVM.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryRed),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Gold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (profile.name.isNotEmpty())
                                    profile.name.first().uppercaseChar().toString()
                                else
                                    profile.email.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryRed
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = profile.name.ifEmpty { "Welcome!" },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = profile.email,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = PrimaryRed)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Personal Details",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.weight(1f))
                            if (!editMode) {
                                TextButton(onClick = { editMode = true }) {
                                    Text("Edit", color = PrimaryRed, fontSize = 13.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        if (editMode) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, null, tint = PrimaryRed)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryRed
                                )
                            )
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone Number") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, null, tint = PrimaryRed)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryRed
                                )
                            )
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Address") },
                                leadingIcon = {
                                    Icon(Icons.Default.Home, null, tint = PrimaryRed)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryRed
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        editMode = false
                                        name = profile.name
                                        phone = profile.phone
                                        address = profile.address
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MediumGray
                                    )
                                ) { Text("Cancel") }
                                Button(
                                    onClick = { profileVM.saveProfile(name, phone, address) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                                ) { Text("Save", fontWeight = FontWeight.Bold) }
                            }
                        } else {
                            ProfileInfoRow(Icons.Default.Email, "Email", profile.email)
                            ProfileInfoRow(
                                Icons.Default.Phone, "Phone",
                                profile.phone.ifEmpty { "Tap Edit to add" }
                            )
                            ProfileInfoRow(
                                Icons.Default.Home, "Address",
                                profile.address.ifEmpty { "Tap Edit to add" }
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ShoppingBag, null, tint = PrimaryRed)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Previous Orders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    if (orders.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = PrimaryRed
                        ) {
                            Text(
                                "${orders.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            if (isLoadingOrders) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PrimaryRed)
                            Spacer(Modifier.height(8.dp))
                            Text("Loading orders...", color = MediumGray, fontSize = 13.sp)
                        }
                    }
                }
            } else if (orders.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🛍️", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No orders yet",
                                color = MediumGray,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Your past orders will appear here",
                                color = MediumGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(orders) { order ->
                    OrderCard(order = order)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, null, tint = PrimaryRed, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Column {
            Text(label, color = MediumGray, fontSize = 11.sp)
            Spacer(Modifier.height(2.dp))
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGray)
        }
    }
    HorizontalDivider(color = LightGray, thickness = 0.8.dp)
}

@Composable
fun OrderCard(order: Order) {
    val dateStr = remember(order.timestamp) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(order.timestamp))
    }

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
                Column {
                    Text(
                        "Order #${order.id.take(8).uppercase()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(dateStr, color = MediumGray, fontSize = 11.sp)
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (order.status) {
                        "Delivered" -> Color(0xFF4CAF50)
                        "Cancelled" -> Color(0xFFF44336)
                        else -> Gold
                    }
                ) {
                    Text(
                        order.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = if (order.status == "Pending") DarkGray else Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightGray, thickness = 0.8.dp)
            Spacer(Modifier.height(10.dp))
            order.items.forEach { item ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LightGold
                        ) {
                            Text(
                                "x${item.quantity}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                color = DarkRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            item.productName,
                            fontSize = 13.sp,
                            color = DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        "₹${"%.0f".format(item.price * item.quantity)}",
                        fontSize = 13.sp,
                        color = PrimaryRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = LightGray, thickness = 0.8.dp)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Payments, null,
                        tint = MediumGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Cash on Delivery", color = MediumGray, fontSize = 12.sp)
                }
                Text(
                    "₹${"%.2f".format(order.totalAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = PrimaryRed
                )
            }
            if (order.address.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.LocationOn, null,
                        tint = MediumGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        order.address.take(80),
                        color = MediumGray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}