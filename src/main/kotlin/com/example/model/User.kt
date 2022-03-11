package com.example.model

data class User(val id: String, var status: Enum<UserStatus>)

enum class UserStatus {
    ONLINE
}