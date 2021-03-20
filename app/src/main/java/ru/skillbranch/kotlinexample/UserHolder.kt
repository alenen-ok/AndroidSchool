package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        User.makeUser(fullName, email = email, password = password)
            .also {
                if (map.checkLogin(it.login)) throw IllegalArgumentException("A user with this email already exists")
                else map[it.login] = it
                return it
            }
    }

    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ): User {
        User.makeUser(fullName, phone = rawPhone)
            .also {
                if (map.checkLogin(it.login)) throw IllegalArgumentException("User with this login already exist.")
                else map[it.login] = it
                return it
            }
    }

    fun requestAccessCode(login: String) {
        if (!map.checkLogin(login)) throw IllegalArgumentException("User with this login not exist.")
        else map.getUser(login)!!.requestAccessCode()
    }

    fun loginUser(login: String, password: String): String? {
        return map.getUser(login)?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    private fun MutableMap<String, User>.checkLogin(login: String): Boolean {
        return if (login.contains('@')) this.containsKey(login.trim())
        else this.containsKey(login.toCorrectPhone())
    }

    private fun MutableMap<String, User>.getUser(login: String): User? {
        return if (login.contains('@')) this[login.trim()]
        else this[login.toCorrectPhone()]
    }

    private fun String.toCorrectPhone(): String = this.replace("[^+\\d]".toRegex(), "")

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }
}