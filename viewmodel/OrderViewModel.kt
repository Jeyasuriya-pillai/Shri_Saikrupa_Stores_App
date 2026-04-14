package com.shrisaikrupa.stores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shrisaikrupa.stores.data.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMyOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val uid = auth.currentUser?.uid
                if (uid == null) {
                    _orders.value = emptyList()
                    _isLoading.value = false
                    return@launch
                }
                val snapshot = db.collection("orders")
                    .whereEqualTo("userId", uid)
                    .get().await()
                _orders.value = snapshot.documents.mapNotNull {
                    it.toObject(Order::class.java)?.copy(id = it.id)
                }.sortedByDescending { it.timestamp }
            } catch (e: Exception) {
                _orders.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun loadAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("orders").get().await()
                _orders.value = snapshot.documents.mapNotNull {
                    it.toObject(Order::class.java)?.copy(id = it.id)
                }.sortedByDescending { it.timestamp }
            } catch (e: Exception) {
                _orders.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                db.collection("orders").document(orderId)
                    .update("status", newStatus).await()

                _orders.value = _orders.value.map { order ->
                    if (order.id == orderId) order.copy(status = newStatus)
                    else order
                }
            } catch (e: Exception) {
            }
        }
    }
}