package com.example.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Email(
        @BsonId val id: Id<Email>? = null,
        val subject: String,
        val body: String,
        val from: String,
        val to: List<String>,
        val dateCreated: Long = System.currentTimeMillis()
)
