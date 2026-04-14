package com.shrisaikrupa.stores.data.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val customerName: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val address: String = "",
    val phone: String = "",
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMethod: String = "Cash on Delivery"
)