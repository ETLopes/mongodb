package com.example.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

interface Theme {
    val theme: "Light" | "Dark"
}

data class Preferences(
        val color: String,
        val theme: Theme,
)
