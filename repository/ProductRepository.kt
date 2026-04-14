package com.shrisaikrupa.stores.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.shrisaikrupa.stores.data.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val db = FirebaseFirestore.getInstance()
    private val productsRef = db.collection("products")

    suspend fun seedProducts(): Result<Unit> {
        return try {
            val existing = productsRef.limit(1).get().await()
            if (!existing.isEmpty) return Result.success(Unit)
            val products = listOf(
                Product(name="Basmati Rice 5kg", description="Premium long grain basmati rice, aromatic and fluffy.", price=350.0, category="Rice & Masala", stock=50, imageUrl="https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400"),
                Product(name="Tata Salt 1kg", description="Pure iodized salt for daily cooking.", price=22.0, category="Rice & Masala", stock=100, imageUrl="https://images.unsplash.com/photo-1518110925495-5fe2fda0442c?w=400"),
                Product(name="Turmeric Powder 200g", description="Pure haldi powder, rich in colour and flavour.", price=45.0, category="Rice & Masala", stock=80, imageUrl="https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=400"),
                Product(name="Red Chilli Powder 200g", description="Spicy red chilli powder for curries.", price=55.0, category="Rice & Masala", stock=75, imageUrl="https://images.unsplash.com/photo-1583119022894-919a68a3d0e3?w=400"),
                Product(name="Coriander Powder 200g", description="Fresh ground coriander for authentic taste.", price=40.0, category="Rice & Masala", stock=70, imageUrl="https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400"),
                Product(name="Garam Masala 100g", description="Blend of whole spices, perfect for biryanis.", price=65.0, category="Rice & Masala", stock=60, imageUrl="https://images.unsplash.com/photo-1509358271058-acd22cc93898?w=400"),
                Product(name="Sona Masoori Rice 5kg", description="Lightweight everyday rice, easy to digest.", price=280.0, category="Rice & Masala", stock=45, imageUrl="https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=400"),
                Product(name="Classmate Notebook 200pg", description="Single line notebook for students, A4 size.", price=55.0, category="Books & Stationery", stock=120, imageUrl="https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=400"),
                Product(name="Reynolds Ball Pen Pack of 10", description="Smooth writing ball pens, blue ink.", price=60.0, category="Books & Stationery", stock=200, imageUrl="https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=400"),
                Product(name="A4 White Paper 500 sheets", description="80 GSM A4 printing and writing paper.", price=280.0, category="Books & Stationery", stock=40, imageUrl="https://images.unsplash.com/photo-1568667256531-9ecf24a5b55b?w=400"),
                Product(name="Pencil Box Set", description="Geometry box with compass, scale, protractor.", price=85.0, category="Books & Stationery", stock=60, imageUrl="https://images.unsplash.com/photo-1602928309088-9b81f1c8c8bc?w=400"),
                Product(name="Stapler with Pins", description="Mini stapler with 1000 staple pins included.", price=120.0, category="Books & Stationery", stock=35, imageUrl="https://images.unsplash.com/photo-1608614587582-c3c4e62b0e58?w=400"),
                Product(name="Sticky Notes 5 colors", description="Self-adhesive sticky notes, 100 sheets each.", price=75.0, category="Books & Stationery", stock=90, imageUrl="https://images.unsplash.com/photo-1512314889357-e157c22f938d?w=400"),
                Product(name="Ludo Board Game", description="Classic family board game for 2-4 players.", price=199.0, category="Toys", stock=30, imageUrl="https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?w=400"),
                Product(name="Toy Car Set 4 cars", description="Die-cast metal toy cars with pull-back action.", price=250.0, category="Toys", stock=25, imageUrl="https://images.unsplash.com/photo-1594495894542-a46cc73e081a?w=400"),
                Product(name="Building Blocks 50pcs", description="Colorful plastic building blocks for kids.", price=349.0, category="Toys", stock=20, imageUrl="https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=400"),
                Product(name="Cricket Bat and Ball Set", description="Wooden bat with rubber ball, for kids aged 5-10.", price=299.0, category="Toys", stock=15, imageUrl="https://images.unsplash.com/photo-1531415074968-036ba1b575da?w=400"),
                Product(name="Colouring Book with Crayons", description="24 page colouring book with 12 wax crayons.", price=149.0, category="Toys", stock=50, imageUrl="https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=400"),
                Product(name="LED Bulb 9W", description="Energy saving LED bulb, cool white, B22 base.", price=85.0, category="Electrical", stock=100, imageUrl="https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=400"),
                Product(name="Electrical Wire 5m", description="3-core flexible copper wire, 1.5 sq mm.", price=180.0, category="Electrical", stock=60, imageUrl="https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400"),
                Product(name="Modular Switch 6A", description="White modular switch with indicator, ISI marked.", price=45.0, category="Electrical", stock=80, imageUrl="https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=400"),
                Product(name="Extension Board 4 Socket", description="4-socket extension board with 1.5m wire and switch.", price=299.0, category="Electrical", stock=40, imageUrl="https://images.unsplash.com/photo-1558618047-f4f20b05e80e?w=400"),
                Product(name="MCB Switch 32A", description="Miniature circuit breaker for home wiring safety.", price=220.0, category="Electrical", stock=30, imageUrl="https://images.unsplash.com/photo-1621905252507-b35492cc74b4?w=400"),
                Product(name="Ceiling Fan Capacitor", description="2.5 MFD capacitor for standard ceiling fans.", price=60.0, category="Electrical", stock=50, imageUrl="https://images.unsplash.com/photo-1587900437150-d36a68f3beb8?w=400")
            )
            products.forEach { productsRef.add(it).await() }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val snapshot = productsRef.get().await()
            val products = snapshot.documents.mapNotNull {
                it.toObject(Product::class.java)?.copy(id = it.id)
            }
            Result.success(products)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val snapshot = productsRef.whereEqualTo("category", category).get().await()
            val products = snapshot.documents.mapNotNull {
                it.toObject(Product::class.java)?.copy(id = it.id)
            }
            Result.success(products)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            productsRef.add(product).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            productsRef.document(product.id).set(product).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            productsRef.document(productId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}