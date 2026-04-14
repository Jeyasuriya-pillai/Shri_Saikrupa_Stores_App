package com.shrisaikrupa.stores.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shrisaikrupa.stores.data.model.CartItem
import com.shrisaikrupa.stores.data.model.Order
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun cartRef() = db.collection("users")
        .document(auth.currentUser!!.uid).collection("cart")

    suspend fun getCartItems(): Result<List<CartItem>> {
        return try {
            val snapshot = cartRef().get().await()
            val items = snapshot.documents.mapNotNull {
                it.toObject(CartItem::class.java)?.copy(id = it.id)
            }
            Result.success(items)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun addToCart(cartItem: CartItem): Result<Unit> {
        return try {
            val existing = cartRef().whereEqualTo("productId", cartItem.productId).get().await()
            if (existing.isEmpty) {
                cartRef().add(cartItem).await()
            } else {
                val doc = existing.documents[0]
                val currentQty = doc.getLong("quantity")?.toInt() ?: 1
                cartRef().document(doc.id).update("quantity", currentQty + 1).await()
            }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun removeFromCart(cartItemId: String): Result<Unit> {
        return try {
            cartRef().document(cartItemId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun placeOrder(order: Order): Result<Unit> {
        return try {
            db.collection("orders").add(order).await()
            val cartItems = cartRef().get().await()
            cartItems.documents.forEach { it.reference.delete() }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}