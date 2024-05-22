package com.example.appcuriosity

class User {
    private var mail: String? = null
    private var first: Boolean = false

    constructor(mail: String?){
        this.mail = mail
        this.first = false
    }

    fun getFirst(): Boolean { return first}
    fun setFirst(value:Boolean) { first=value}
    fun getMail(): String { return mail.toString() }
}