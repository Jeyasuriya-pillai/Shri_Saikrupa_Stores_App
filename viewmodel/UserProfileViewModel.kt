package com.shrisaikrupa.stores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = ""
)

class UserProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile
    private val _saveState = MutableStateFlow<String?>(null)
    val saveState: StateFlow<String?> = _saveState

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            try {
                val uid = auth.currentUser?.uid ?: return@launch
                val firebaseEmail = auth.currentUser?.email ?: ""
                val doc = db.collection("users").document(uid).get().await()
                if (doc.exists()) {
                    _profile.value = UserProfile(
                        name = doc.getString("name") ?: "",
                        email = firebaseEmail,
                        phone = doc.getString("phone") ?: "",
                        address = doc.getString("address") ?: ""
                    )
                } else {
                    _profile.value = UserProfile(
                        name = "",
                        email = firebaseEmail,
                        phone = "",
                        address = ""
                    )
                }
            } catch (e: Exception) {
                _profile.value = UserProfile(email = auth.currentUser?.email ?: "")
            }
        }
    }

    fun saveProfile(name: String, phone: String, address: String) {
        viewModelScope.launch {
            try {
                val uid = auth.currentUser?.uid ?: return@launch
                val firebaseEmail = auth.currentUser?.email ?: ""
                db.collection("users").document(uid).set(
                    mapOf(
                        "name" to name,
                        "phone" to phone,
                        "address" to address,
                        "email" to firebaseEmail
                    )
                ).await()
                _profile.value = _profile.value.copy(
                    name = name,
                    phone = phone,
                    address = address,
                    email = firebaseEmail
                )
                _saveState.value = "Profile saved successfully!"
            } catch (e: Exception) {
                _saveState.value = "Failed to save: ${e.message}"
            }
        }
    }

    fun resetSaveState() { _saveState.value = null }
}