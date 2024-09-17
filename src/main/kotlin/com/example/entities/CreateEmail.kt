package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class CreateEmail(
        val subject: String,
        val body: String,
        val from: String,
        val to: List<String>
)
