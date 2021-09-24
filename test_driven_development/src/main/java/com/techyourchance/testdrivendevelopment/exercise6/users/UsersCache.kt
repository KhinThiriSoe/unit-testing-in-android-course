package com.techyourchance.testdrivendevelopment.exercise6.users

interface UsersCache {
    fun cacheUser(user: User?)
    fun getUser(userId: String?): User?
}