package com.example.entities

fun Email.toDtoEmail(): CreateEmail =
        CreateEmail(subject = this.subject, body = this.body, from = this.from, to = this.to)

fun CreateEmail.toEmail(): Email =
        Email(subject = this.subject, body = this.body, from = this.from, to = this.to)
