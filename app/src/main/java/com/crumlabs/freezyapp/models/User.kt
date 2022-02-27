package com.crumlabs.freezyapp.models

data class User(
        val name: String? = null,
        val email: String? = null,
        val phone: String? = null,
        val country: String? = null,
        val state: String? = null,
        val city: String? = null,
        ) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}