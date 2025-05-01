package com.sam.quickkeys.repository

import com.sam.quickkeys.data.UserDao
import com.sam.quickkeys.model.User


class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
}