package com.shrisaikrupa.stores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.shrisaikrupa.stores.data.model.CartItem
import com.shrisaikrupa.stores.data.model.Order
import com.shrisaikrupa.stores.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repo = CartRepository()
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    private val _orderState = MutableStateFlow<String?>(null)
    val orderState: StateFlow<String?> = _orderState
    val totalPrice get() = _cartItems.value.sumOf { it.price * it.quantity }

    init { loadCart() }

    fun loadCart() {
        viewModelScope.launch {
            val result = repo.getCartItems()
            if (result.isSuccess) _cartItems.value = result.getOrDefault(emptyList())
        }
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch { repo.addToCart(cartItem); loadCart() }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch { repo.removeFromCart(cartItemId); loadCart() }
    }

    fun placeOrder(address: String, phone: String, customerName: String = "") {
        viewModelScope.launch {
            val order = Order(
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                items = _cartItems.value,
                totalAmount = totalPrice,
                address = address,
                phone = phone,
                customerName = customerName,
                status = "Pending",
                paymentMethod = "Cash on Delivery"
            )
            val result = repo.placeOrder(order)
            _orderState.value = if (result.isSuccess) "Order Placed!" else "Order Failed"
            if (result.isSuccess) loadCart()
        }
    }

    fun resetOrderState() { _orderState.value = null }
}