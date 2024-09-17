package com.example.service

import com.example.entities.Article
import com.example.entities.Email
import com.example.entities.Preferences
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

class ArticleService {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("article")
    private val emailDatabase = client.getDatabase("email")
    private val preferencesDatabase = client.getDatabase("preferences")
    private val articleCollection = database.getCollection<Article>()
    private val emailCollection = emailDatabase.getCollection<Email>()
    private val preferencesCollection = database.getCollection<Preferences>()

    // Article methods
    fun create(article: Article): Id<Article>? {
        articleCollection.insertOne(article)
        return article.id
    }

    fun findAll(): List<Article> = articleCollection.find().toList()

    fun findById(id: String): Article? {
        val bsonId: Id<Article> = ObjectId(id).toId()
        return articleCollection.findOne(Article::id eq bsonId)
    }

    fun updateArticleById(id: String, request: Article): Boolean =
            findById(id)?.let { article ->
                val updateResult =
                        articleCollection.replaceOne(
                                article.copy(title = request.title, body = request.body)
                        )
                updateResult.modifiedCount == 1L
            }
                    ?: false

    fun deleteArticleById(id: String): Boolean {
        val bsonId: Id<Article> = ObjectId(id).toId()
        val deleteResult = articleCollection.deleteOneById(bsonId)
        return deleteResult.deletedCount == 1L
    }

    // Email methods
    fun createEmail(email: Email): Id<Email>? {
        emailCollection.insertOne(email)
        return email.id
    }

    fun findAllEmails(): List<Email> {
        println("findAllEmails")
        println(emailCollection.find())
        println("findAllEmails")
        return emailCollection.find().toList()
    }

    fun findEmailById(id: String): Email? {
        val bsonId: Id<Email> = ObjectId(id).toId()
        return emailCollection.findOne(Email::id eq bsonId)
    }

    fun getPreferences(): List<String> {
        return preferencesCollection.findOne()?.let { preferences ->
            listOf(preferences.color, preferences.theme.theme)
        }
                ?: emptyList()
    }

    fun updatePreferences(preferences: Preferences): Boolean {
        val updateResult = preferencesCollection.replaceOne(preferences)
        return updateResult.modifiedCount == 1L
    }

    fun release() {
        client.close()
    }
}
