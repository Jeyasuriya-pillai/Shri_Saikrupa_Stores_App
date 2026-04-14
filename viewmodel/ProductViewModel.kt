package com.shrisaikrupa.stores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrisaikrupa.stores.data.model.Product
import com.shrisaikrupa.stores.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repo = ProductRepository()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _operationState = MutableStateFlow("")
    val operationState: StateFlow<String> = _operationState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("All", "Rice & Masala", "Books & Stationery", "Toys", "Electrical")

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            val seedResult = repo.seedProducts()
            loadProducts()
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.getAllProducts()
            result.onSuccess { _products.value = it }
            _isLoading.value = false
        }
    }

    fun filterByCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            val result = if (category == "All") repo.getAllProducts()
            else repo.getProductsByCategory(category)
            result.onSuccess { _products.value = it }
            _isLoading.value = false
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.addProduct(product)
            result.onSuccess {
                _operationState.value = "Product added!"
                loadProducts()
            }.onFailure {
                _operationState.value = "Error: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.updateProduct(product)
            result.onSuccess {
                _operationState.value = "Product updated!"
                loadProducts()
            }.onFailure {
                _operationState.value = "Error: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.deleteProduct(productId)
            result.onSuccess {
                _operationState.value = "Product deleted!"
                loadProducts()
            }.onFailure {
                _operationState.value = "Error: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun resetOperationState() {
        _operationState.value = ""
    }
}