package com.example.minewiki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minewiki.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)
    @Query("SELECT * FROM users_table WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): User?

    @Query("SELECT * FROM users_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}