package com.example.plugins

import com.example.entities.*
import com.example.service.ArticleService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(articleService: ArticleService) {

    routing {
        post("/article") {
            val request = call.receive<CreateArticle>()
            val article = request.toArticle()

            articleService.create(article)?.let { userId ->
                call.response.headers.append("My-User-Id-Header", userId.toString())
                call.respond(HttpStatusCode.Created, userId.toString())
            }
                    ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
        }

        get("/article/list") {
            val articlesList = articleService.findAll().map(Article::toDto)
            call.respond(articlesList)
        }

        get("/article/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val articleById = articleService.findById(id)?.toDto()
            articleById?.let { call.respond(it) }
                    ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
        }

        put("/article/{id}/edit") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val article = call.receive<CreateArticle>().toArticle()
            val updatedSuccessfully = articleService.updateArticleById(id, article)
            if (updatedSuccessfully) {
                call.respond(HttpStatusCode.OK, "Article was edited")
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/article/{id}/delete") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deletedSuccessfully = articleService.deleteArticleById(id)
            if (deletedSuccessfully) {
                call.respond(HttpStatusCode.OK, "Article was deleted")
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
            }
        }

        // Email routes
        post("/email") {
            val request = call.receive<CreateEmail>()
            val email = request.toEmail()

            articleService.createEmail(email)?.let { emailId ->
                call.response.headers.append("My-Email-Id-Header", emailId.toString())
                call.respond(HttpStatusCode.Created, emailId.toString())
            }
                    ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
        }

        get("/emails") {
            val allEmails = articleService.findAllEmails()
            println("AE CARALHO PORRA: $allEmails")
            val emailsList = articleService.findAllEmails().map(Email::toDtoEmail)
            call.respond(emailsList)
        }

        get("/email/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val emailById = articleService.findEmailById(id)?.toDtoEmail()
            emailById?.let { call.respond(it) }
                    ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
        }

        get("/preferences") {
            val preferences = articleService.getPreferences()
            call.respond(preferences)
        }

        post("/preferences") {
            val preferences = call.receive<Preferences>()
            val updatedSuccessfully = articleService.updatePreferences(preferences)
            if (updatedSuccessfully) {
                call.respond(HttpStatusCode.OK, "Preferences were updated")
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
