package com.example.model

data class User(val id: Int, var status: Enum<UserStatus>)

enum class UserStatus {
    ONLINE
}